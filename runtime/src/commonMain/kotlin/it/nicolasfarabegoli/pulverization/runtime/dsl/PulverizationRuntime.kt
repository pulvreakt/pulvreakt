package it.nicolasfarabegoli.pulverization.runtime.dsl

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.ActuatorsComponent
import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationPayload
import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.core.StateComponent
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
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
import it.nicolasfarabegoli.pulverization.runtime.context.createContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.native.concurrent.ThreadLocal

typealias StateLogicType<S> = suspend (State<S>, BehaviourRef<S>) -> Unit
typealias ActuatorsLogicType<AS> = suspend (ActuatorsContainer, BehaviourRef<AS>) -> Unit
typealias SensorsLogicType<SS> = suspend (SensorsContainer, BehaviourRef<SS>) -> Unit
typealias CommunicationLogicType<C> = suspend (Communication<C>, BehaviourRef<C>) -> Unit
typealias BehaviourLogicType<S, C, SS, AS, R> =
    suspend (Behaviour<S, C, SS, AS, R>, StateRef<S>, CommunicationRef<C>, SensorsRef<SS>, ActuatorsRef<AS>) -> Unit

/**
 * Configure the platform based on the [configuration] of a logical device.
 */
inline fun <reified S, reified C, reified SS, reified AS, reified R> pulverizationPlatform(
    configuration: LogicalDeviceConfiguration,
    init: PulverizationPlatformScope<S, C, SS, AS, R>.() -> Unit,
) where S : StateRepresentation, C : CommunicationPayload, SS : Any, AS : Any, R : Any =
    PulverizationPlatformScope<S, C, SS, AS, R>(
        serializer(),
        serializer(),
        serializer(),
        serializer(),
        configuration,
    ).apply(init)

/**
 * Module to load all the dependencies in the framework.
 * Relying on Koin as dependency injection framework.
 */
@ThreadLocal
object PulverizationKoinModule {
    /**
     * The koin app.
     */
    var koinApp: KoinApplication? = null
}

/**
 * Represent the absence of value.
 * This specific type aim to replace the 'Nothing' type which is not serializable.
 * Use this type in all the place where a serializable type is required but no concrete type is necessarily.
 */
@Serializable
object NoVal

/**
 * Represent the absence of value.
 * This specific type aim to replace the 'Nothing' type which is not serializable.
 * Use this type in all the place where a [StateRepresentation] is required but no concrete type is necessarily.
 */
@Serializable
object NoState : StateRepresentation

/**
 * Represent the absence of value.
 * This specific type aim to replace the 'Nothing' type which is not serializable.
 * Use this type in all the place where a [CommunicationPayload] is required but no concrete type is necessarily.
 */
@Serializable
object NoComm : CommunicationPayload

/**
 * DSL scope for configure the platform with all components logic and the type of communicator.
 */
class PulverizationPlatformScope<S, C, SS : Any, AS : Any, R : Any>(
    private val stateSer: KSerializer<S>,
    private val commSer: KSerializer<C>,
    private val senseSer: KSerializer<SS>,
    private val actSer: KSerializer<AS>,
    private val deviceConfig: LogicalDeviceConfiguration,
) where S : StateRepresentation, C : CommunicationPayload {

    private var communicator: Communicator? = null
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
        communicator = provider()
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
            val (sr, cm, ss, act) =
                setupComponentsRef(stateSer, commSer, senseSer, actSer, allComponents, deploymentUnit, communicator)
            allComponentsRef.addAll(setOf(sr, cm, ss, act))
            launch { setOf(sr, cm, ss, act).forEach { it.setup() }; logic(comp, sr, cm, ss, act) }
        }
        val communicationJob = communicationLogic to communicationComponent takeAllNotNull { logic, comp ->
            val behaviourRef =
                setupBehaviourRef(commSer, CommunicationComponent, allComponents, deploymentUnit, communicator)
            allComponentsRef += behaviourRef
            launch { behaviourRef.setup(); logic(comp, behaviourRef) }
        }
        val actuatorsJob = actuatorsLogic to actuatorsComponent takeAllNotNull { logic, comp ->
            val behaviourRef =
                setupBehaviourRef(actSer, ActuatorsComponent, allComponents, deploymentUnit, communicator)
            allComponentsRef += behaviourRef
            launch { behaviourRef.setup(); logic(comp, behaviourRef) }
        }
        val sensorsJob = sensorsLogic to sensorsComponent takeAllNotNull { logic, comp ->
            val behaviourRef =
                setupBehaviourRef(senseSer, SensorsComponent, allComponents, deploymentUnit, communicator)
            allComponentsRef += behaviourRef
            launch { behaviourRef.setup(); logic(comp, behaviourRef) }
        }
        val stateJob = stateLogic to stateComponent takeAllNotNull { logic, comp ->
            val behaviourRef =
                setupBehaviourRef(stateSer, StateComponent, allComponents, deploymentUnit, communicator)
            allComponentsRef += behaviourRef
            launch { behaviourRef.setup(); logic(comp, behaviourRef) }
        }
        setOf(stateJob, behaviourJob, actuatorsJob, sensorsJob, communicationJob).filterNotNull().toSet()
    }

    /**
     * Stop the platform and release all the resources allocated.
     */
    suspend fun stop() {
        allComponentsRef.forEach { it.finalize() }
    }

    companion object {
        /**
         * This method configure the [behaviour] to be used and the corresponding [logic].
         */
        fun <S, C, SS, AS, R> PulverizationPlatformScope<S, C, SS, AS, R>.behaviourLogic(
            behaviour: Behaviour<S, C, SS, AS, R>,
            logic: BehaviourLogicType<S, C, SS, AS, R>,
        ) where S : StateRepresentation, C : CommunicationPayload, SS : Any, AS : Any, R : Any {
            configuredComponents += BehaviourComponent
            behaviourComponent = behaviour
            behaviourLogic = logic
        }

        /**
         * This method configure the [communication] to be used and the corresponding [logic].
         */
        fun <C> PulverizationPlatformScope<NoState, C, NoVal, NoVal, NoVal>.communicationLogic(
            communication: Communication<C>,
            logic: CommunicationLogicType<C>,
        ) where C : CommunicationPayload {
            configuredComponents += CommunicationComponent
            communicationComponent = communication
            communicationLogic = logic
        }

        /**
         * This method configure the [actuators] to be used and the corresponding [logic].
         */
        fun <AS : Any> PulverizationPlatformScope<NoState, NoComm, NoVal, AS, NoVal>.actuatorsLogic(
            actuators: ActuatorsContainer,
            logic: ActuatorsLogicType<AS>,
        ) {
            configuredComponents += ActuatorsComponent
            actuatorsComponent = actuators
            actuatorsLogic = logic
        }

        /**
         * This method configure the [sensors] to be used and the corresponding [logic].
         */
        fun <SS : Any> PulverizationPlatformScope<NoState, NoComm, SS, NoVal, NoVal>.sensorsLogic(
            sensors: SensorsContainer,
            logic: SensorsLogicType<SS>,
        ) {
            configuredComponents += SensorsComponent
            sensorsComponent = sensors
            sensorsLogic = logic
        }

        /**
         * This method configure the [state] to be used and the corresponding [logic].
         */
        fun <S> PulverizationPlatformScope<S, NoComm, NoVal, NoVal, NoVal>.stateLogic(
            state: State<S>,
            logic: StateLogicType<S>,
        ) where S : StateRepresentation {
            configuredComponents += StateComponent
            stateComponent = state
            stateLogic = logic
        }
    }
}

internal infix fun <F, S, R> Pair<F?, S?>.takeAllNotNull(body: (F, S) -> R): R? {
    // The local assignment is necessary because a problem with smart cast in Kotlin
    val f = first
    val s = second
    return if (f != null && s != null) body(f, s) else null
}
