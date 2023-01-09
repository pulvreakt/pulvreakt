package it.nicolasfarabegoli.pulverization.runtime.dsl

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.ActuatorsComponent
import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.core.StateComponent
import it.nicolasfarabegoli.pulverization.dsl.LogicalDeviceConfiguration
import it.nicolasfarabegoli.pulverization.dsl.getDeploymentUnit
import it.nicolasfarabegoli.pulverization.runtime.communication.CommManager
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlaceProvider
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ActuatorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.CommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ComponentRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.SensorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.StateRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.createActuatorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.createCommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.createSensorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.createStateRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.setupBehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.context.createContext
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.koinApplication
import org.koin.dsl.module

typealias StateLogicType<S> = suspend (State<S>, BehaviourRef<S>) -> Unit
typealias ActuatorsLogicType<AS> = suspend (ActuatorsContainer, BehaviourRef<AS>) -> Unit
typealias SensorsLogicType<SS> = suspend (SensorsContainer, BehaviourRef<SS>) -> Unit
typealias CommunicationLogicType<C> = suspend (Communication<C>, BehaviourRef<C>) -> Unit
typealias BehaviourLogicType<S, C, SS, AS, R> =
    suspend (Behaviour<S, C, SS, AS, R>, StateRef<S>, CommunicationRef<C>, SensorsRef<SS>, ActuatorsRef<AS>) -> Unit

@PublishedApi
internal class AnySerializer<S> : KSerializer<S> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("kotlin.Any", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): S {
        TODO("Not yet implemented")
    }

    override fun serialize(encoder: Encoder, value: S) {
        TODO("Not yet implemented")
    }
}

@PublishedApi
internal inline fun <reified T> getSerializer(): KSerializer<T> = when (T::class) {
    Any::class -> AnySerializer()
    else -> serializer()
}

/**
 * Configure the platform based on the [configuration] of a logical device.
 */
inline fun <reified S, reified C, reified SS, reified AS, reified R> pulverizationPlatform(
    configuration: LogicalDeviceConfiguration,
    init: PulverizationPlatformScope<S, C, SS, AS, R>.() -> Unit,
): PulverizationPlatformScope<S, C, SS, AS, R> where S : Any, C : Any, SS : Any, AS : Any, R : Any {
    return PulverizationPlatformScope<S, C, SS, AS, R>(
        getSerializer(),
        getSerializer(),
        getSerializer(),
        getSerializer(),
        configuration,
    ).apply(init)
}

/**
 * DSL scope for configure the platform with all components logic and the type of communicator.
 */
class PulverizationPlatformScope<S : Any, C : Any, SS : Any, AS : Any, R : Any>(
    private val stateSer: KSerializer<S>,
    private val commSer: KSerializer<C>,
    private val senseSer: KSerializer<SS>,
    private val actSer: KSerializer<AS>,
    private val deviceConfig: LogicalDeviceConfiguration,
) {
    private var communicator: () -> Communicator? = { null }
    private var remotePlaceProvider: () -> RemotePlaceProvider = {
        object : RemotePlaceProvider, KoinComponent {
            override val context: Context by inject()
            override fun get(type: PulverizedComponentType): RemotePlace? = null
        }
    }

    private var behaviourLogic: BehaviourLogicType<S, C, SS, AS, R>? = null
    private var communicationLogic: CommunicationLogicType<C>? = null
    private var actuatorsLogic: ActuatorsLogicType<AS>? = null
    private var sensorsLogic: SensorsLogicType<SS>? = null
    private var stateLogic: StateLogicType<S>? = null

    private var behaviourComponent: Behaviour<S, C, SS, AS, R>? = null
    private var communicationComponent: Communication<C>? = null
    private var actuatorsComponent: ActuatorsContainer? = null
    private var sensorsComponent: SensorsContainer? = null
    private var stateComponent: State<S>? = null

    private var context: Context? = null

    private val configuredComponents: MutableSet<PulverizedComponentType> = mutableSetOf()
    private val allComponentsRef: MutableSet<ComponentRef<*>> = mutableSetOf()

    private suspend fun setupKoinModule() {
        val context = this.context ?: createContext()
        val module = module {
            single { context }
            single { CommManager() }
            factory { remotePlaceProvider() }
        }
        PulverizationKoinModule.koinApp = koinApplication {
            modules(module)
        }
    }

    /**
     * Setup a custom context in which the pulverization framework will work.
     */
    suspend fun withContext(init: ContextBuilderScope.() -> Unit) {
        context = ContextBuilderScope().apply(init).build()
    }

    /**
     * Setup the [Communication] which will be used to enable the intra-component-communication.
     * The [Communication] is given through the [provider].
     */
    fun withPlatform(provider: () -> Communicator) {
        communicator = provider
    }

    /**
     * Setup the [RemotePlaceProvider].
     */
    fun withRemotePlace(provider: () -> RemotePlaceProvider) {
        remotePlaceProvider = provider
    }

    /**
     * This method start the platform spawning a coroutine for each active component.
     */
    suspend fun start(): Set<Job> = coroutineScope {
        setupKoinModule()

        val allComponents = deviceConfig.components
        val deploymentUnit = deviceConfig.getDeploymentUnit(configuredComponents)?.deployableComponents
            ?: error("The configured components doesn't match the configuration")

        val behaviourJob = behaviourLogic to behaviourComponent takeAllNotNull { logic, comp ->
            val sr = createStateRef(stateSer, allComponents, deploymentUnit, communicator())
            val cm = createCommunicationRef(commSer, allComponents, deploymentUnit, communicator())
            val ss = createSensorsRef(senseSer, allComponents, deploymentUnit, communicator())
            val act = createActuatorsRef(actSer, allComponents, deploymentUnit, communicator())
            allComponentsRef.addAll(setOf(sr, cm, ss, act))
            launch { setOf(sr, cm, ss, act).forEach { it.setup() }; comp.initialize(); logic(comp, sr, cm, ss, act) }
        }
        val communicationJob = communicationLogic to communicationComponent takeAllNotNull { logic, comp ->
            val behaviourRef =
                setupBehaviourRef(commSer, CommunicationComponent, allComponents, deploymentUnit, communicator())
            allComponentsRef += behaviourRef
            launch { behaviourRef.setup(); comp.initialize(); logic(comp, behaviourRef) }
        }
        val actuatorsJob = actuatorsLogic to actuatorsComponent takeAllNotNull { logic, comp ->
            val behaviourRef =
                setupBehaviourRef(actSer, ActuatorsComponent, allComponents, deploymentUnit, communicator())
            allComponentsRef += behaviourRef
            launch { behaviourRef.setup(); comp.initialize(); logic(comp, behaviourRef) }
        }
        val sensorsJob = sensorsLogic to sensorsComponent takeAllNotNull { logic, comp ->
            val behaviourRef =
                setupBehaviourRef(senseSer, SensorsComponent, allComponents, deploymentUnit, communicator())
            allComponentsRef += behaviourRef
            launch { behaviourRef.setup(); comp.initialize(); logic(comp, behaviourRef) }
        }
        val stateJob = stateLogic to stateComponent takeAllNotNull { logic, comp ->
            val behaviourRef =
                setupBehaviourRef(stateSer, StateComponent, allComponents, deploymentUnit, communicator())
            allComponentsRef += behaviourRef
            launch { behaviourRef.setup(); comp.initialize(); logic(comp, behaviourRef) }
        }
        setOf(stateJob, behaviourJob, actuatorsJob, sensorsJob, communicationJob).filterNotNull().toSet()
    }

    /**
     * Stop the platform and release all the resources allocated.
     */
    suspend fun stop() {
        setOf(behaviourComponent, stateComponent, communicationComponent, sensorsComponent, actuatorsComponent)
            .filterNotNull().forEach { it.finalize() }
        allComponentsRef.forEach { it.finalize() }
    }

    /**
     * This method configure the [behaviour] to be used and the corresponding [logic].
     */
    fun behaviourLogic(behaviour: Behaviour<S, C, SS, AS, R>, logic: BehaviourLogicType<S, C, SS, AS, R>) {
        configuredComponents += BehaviourComponent
        behaviourComponent = behaviour
        behaviourLogic = logic
    }

    /**
     * This method configure the [communication] to be used and the corresponding [logic].
     */
    fun communicationLogic(communication: Communication<C>, logic: CommunicationLogicType<C>) {
        configuredComponents += CommunicationComponent
        communicationComponent = communication
        communicationLogic = logic
    }

    /**
     * This method configure the [actuators] to be used and the corresponding [logic].
     */
    fun actuatorsLogic(actuators: ActuatorsContainer, logic: ActuatorsLogicType<AS>) {
        configuredComponents += ActuatorsComponent
        actuatorsComponent = actuators
        actuatorsLogic = logic
    }

    /**
     * This method configure the [sensors] to be used and the corresponding [logic].
     */
    fun sensorsLogic(sensors: SensorsContainer, logic: SensorsLogicType<SS>) {
        configuredComponents += SensorsComponent
        sensorsComponent = sensors
        sensorsLogic = logic
    }

    /**
     * This method configure the [state] to be used and the corresponding [logic].
     */
    fun stateLogic(state: State<S>, logic: StateLogicType<S>) {
        configuredComponents += StateComponent
        stateComponent = state
        stateLogic = logic
    }
}

internal infix fun <F, S, R> Pair<F?, S?>.takeAllNotNull(body: (F, S) -> R): R? {
    // The local assignment is necessary because a problem with smart cast in Kotlin
    val f = first
    val s = second
    return if (f != null && s != null) body(f, s) else null
}
