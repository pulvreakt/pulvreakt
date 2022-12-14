package it.nicolasfarabegoli.pulverization.runtime.dsl

import io.kotest.assertions.throwables.shouldNotThrowUnit
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.ActuatorsComponent
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent
import it.nicolasfarabegoli.pulverization.dsl.Cloud
import it.nicolasfarabegoli.pulverization.dsl.Edge
import it.nicolasfarabegoli.pulverization.dsl.getDeploymentUnit
import it.nicolasfarabegoli.pulverization.dsl.getDeviceConfiguration
import it.nicolasfarabegoli.pulverization.dsl.pulverizationConfig
import it.nicolasfarabegoli.pulverization.runtime.communication.CommManager
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlaceProvider
import it.nicolasfarabegoli.pulverization.runtime.componentsref.createCommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.createStateRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.setupBehaviourRef
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class ComponentsRefManagerTest : FreeSpec() {
    private val module = module {
        single { CommManager() }
        factory<RemotePlaceProvider> {
            return@factory object : RemotePlaceProvider {
                override val context: Context by inject()
                override fun get(type: PulverizedComponentType): RemotePlace? = null
            }
        }
    }

    init {
        val config = pulverizationConfig {
            logicalDevice("device-1") {
                BehaviourComponent and
                    StateComponent and
                    CommunicationComponent and
                    SensorsComponent and
                    ActuatorsComponent deployableOn Edge
            }
            logicalDevice("device-2") {
                StateComponent and BehaviourComponent deployableOn Cloud
                CommunicationComponent deployableOn Edge
            }
        }
        "The component ref manager" - {
            "based on the given configuration" - {
                "with all local device" - {
                    "should create all components ref with a local communicator" {
                        PulverizationKoinModule.koinApp = koinApplication { modules(module) }
                        // Local communicator
                        val commManager = PulverizationKoinModule.koinApp?.koin?.get<CommManager>()!!

                        val deviceConfig = config.getDeviceConfiguration("device-1")!!
                        val deploymentUnit = deviceConfig.getDeploymentUnit(
                            StateComponent,
                            BehaviourComponent,
                            SensorsComponent,
                            ActuatorsComponent,
                            CommunicationComponent,
                        )!!.deployableComponents

                        val stateRef =
                            createStateRef(StatePayload.serializer(), deviceConfig.components, deploymentUnit, null)
                        stateRef.setup()

                        val job = launch {
                            val result =
                                Json.decodeFromString<StatePayload>(commManager.stateInstance.first().decodeToString())
                            result shouldBe StatePayload(1)
                        }
                        stateRef.sendToComponent(StatePayload(1))
                        job.join()
                    }
                }
                "with a partial pulverization" - {
                    "should create all remote components with the given communicator" {
                        val flow = MutableSharedFlow<ByteArray>(1)
                        val communicator = RemoteCommunicator(flow)
                        val deviceConfig = config.getDeviceConfiguration("device-2")!!
                        val deploymentUnit =
                            deviceConfig.getDeploymentUnit(BehaviourComponent, StateComponent)!!.deployableComponents
                        val communicationRef = createCommunicationRef(
                            CommPayload.serializer(),
                            deviceConfig.components,
                            deploymentUnit,
                            communicator,
                        )
                        val job = launch {
                            val result = flow.first()
                            Json.decodeFromString<CommPayload>(result.decodeToString()) shouldBe CommPayload(2)
                        }
                        communicationRef.sendToComponent(CommPayload(2))
                        job.join()
                    }
                }
            }
            "should throw an exception if no communicator is given when needed" {
                shouldThrowUnit<IllegalStateException> {
                    val deviceConfig = config.getDeviceConfiguration("device-2")!!
                    val deploymentUnit =
                        deviceConfig.getDeploymentUnit(BehaviourComponent, StateComponent)!!.deployableComponents
                    createCommunicationRef(CommPayload.serializer(), deviceConfig.components, deploymentUnit, null)
                }
            }
            "when creating the behaviour ref" - {
                "should throw an exception if no behaviour is present in the config" {
                    val exception = shouldThrowUnit<IllegalStateException> {
                        setupBehaviourRef<StatePayload>(StateComponent, emptySet(), emptySet(), null)
                    }
                    exception.message shouldContain "The Behaviour must be defined!"
                }
                "it should be created remote" {
                    shouldNotThrowUnit<Exception> {
                        val flow = MutableSharedFlow<ByteArray>(1)
                        val allComponents = setOf(BehaviourComponent, StateComponent)
                        val behaviorRef = setupBehaviourRef<StatePayload>(
                            StateComponent,
                            allComponents,
                            setOf(StateComponent),
                            RemoteCommunicator(flow),
                        )
                        val job = launch {
                            val result = flow.first()
                            Json.decodeFromString<StatePayload>(result.decodeToString()) shouldBe StatePayload(1)
                        }
                        behaviorRef.sendToComponent(StatePayload(1))
                        job.join()
                    }
                }
                "it should throw an exception if we try to create a behaviour ref linked to a non-existing component" {
                    val exception = shouldThrowUnit<IllegalStateException> {
                        val allComponents = setOf(BehaviourComponent, StateComponent)
                        setupBehaviourRef<CommPayload>(
                            CommunicationComponent,
                            allComponents,
                            setOf(StateComponent),
                            RemoteCommunicator(MutableSharedFlow(1)),
                        )
                    }
                    exception.message shouldContain "this component doesn't appear on the configuration"
                }
                "an exception should be thrown if no communicator is given" {
                    val exception = shouldThrowUnit<IllegalStateException> {
                        val allComponents = setOf(BehaviourComponent, StateComponent)
                        setupBehaviourRef<StatePayload>(
                            StateComponent,
                            allComponents,
                            setOf(StateComponent),
                            null,
                        )
                    }
                    exception.message shouldContain "No communicator given"
                }
            }
        }
    }
}
