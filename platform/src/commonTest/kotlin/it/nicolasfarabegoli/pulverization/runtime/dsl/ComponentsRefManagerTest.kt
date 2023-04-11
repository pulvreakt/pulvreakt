package it.nicolasfarabegoli.pulverization.runtime.dsl

import io.kotest.assertions.throwables.shouldNotThrowUnit
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.dsl.Cloud
import it.nicolasfarabegoli.pulverization.dsl.Edge
import it.nicolasfarabegoli.pulverization.dsl.getDeploymentUnit
import it.nicolasfarabegoli.pulverization.dsl.getDeviceConfiguration
import it.nicolasfarabegoli.pulverization.dsl.pulverizationConfig
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State
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
                override fun get(type: ComponentType): RemotePlace? = null
            }
        }
    }

    init {
        val config = pulverizationConfig {
            logicalDevice("device-1") {
                Behaviour and
                    State and
                    Communication and
                    Sensors and
                    Actuators deployableOn Edge
            }
            logicalDevice("device-2") {
                State and Behaviour deployableOn Cloud
                Communication deployableOn Edge
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
                            State,
                            Behaviour,
                            Sensors,
                            Actuators,
                            Communication,
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
                            deviceConfig.getDeploymentUnit(Behaviour, State)!!.deployableComponents
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
                        deviceConfig.getDeploymentUnit(Behaviour, State)!!.deployableComponents
                    createCommunicationRef(CommPayload.serializer(), deviceConfig.components, deploymentUnit, null)
                }
            }
            "when creating the behaviour ref" - {
                "should throw an exception if no behaviour is present in the config" {
                    val exception = shouldThrowUnit<IllegalStateException> {
                        setupBehaviourRef<StatePayload>(State, emptySet(), emptySet(), null)
                    }
                    exception.message shouldContain "The Behaviour must be defined!"
                }
                "it should be created remote" {
                    shouldNotThrowUnit<Exception> {
                        val flow = MutableSharedFlow<ByteArray>(1)
                        val allComponents = setOf(Behaviour, State)
                        val behaviorRef = setupBehaviourRef<StatePayload>(
                            State,
                            allComponents,
                            setOf(State),
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
                        val allComponents = setOf(Behaviour, State)
                        setupBehaviourRef<CommPayload>(
                            Communication,
                            allComponents,
                            setOf(State),
                            RemoteCommunicator(MutableSharedFlow(1)),
                        )
                    }
                    exception.message shouldContain "this component doesn't appear on the configuration"
                }
                "an exception should be thrown if no communicator is given" {
                    val exception = shouldThrowUnit<IllegalStateException> {
                        val allComponents = setOf(Behaviour, State)
                        setupBehaviourRef<StatePayload>(
                            State,
                            allComponents,
                            setOf(State),
                            null,
                        )
                    }
                    exception.message shouldContain "No communicator given"
                }
            }
        }
    }
}
