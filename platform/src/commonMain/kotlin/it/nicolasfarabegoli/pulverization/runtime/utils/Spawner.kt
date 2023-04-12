package it.nicolasfarabegoli.pulverization.runtime.utils

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ComponentsRuntimeContainer
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.ComponentsRefsContainer
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

internal class Spawner<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    private val componentsRuntimeContainer: ComponentsRuntimeContainer<S, C, SS, AS, O>,
    private val componentsRefsContainer: ComponentsRefsContainer<S, C, SS, AS>,
) {
    private var spawnedComponents = mapOf<ComponentType, Job>()

    suspend fun spawn(componentType: ComponentType): Unit = coroutineScope {
        when (componentType) {
            Behaviour -> {
                if (spawnedComponents[Behaviour] != null) return@coroutineScope
                val jobRef = launch {
                    componentsRuntimeContainer.behaviourRuntime?.let {
                        val logic = it.behaviourLogic
                        val component = it.behaviourComponent
                        val stateRef = componentsRefsContainer.behaviourRefs.stateRef
                        val commRef = componentsRefsContainer.behaviourRefs.communicationRef
                        val sensorsRef = componentsRefsContainer.behaviourRefs.sensorsRef
                        val actuatorsRef = componentsRefsContainer.behaviourRefs.actuatorsRef
                        component.initialize()
                        logic(component, stateRef, commRef, sensorsRef, actuatorsRef)
                        component.finalize()
                    }
                }
                spawnedComponents = spawnedComponents + (Behaviour to jobRef)
            }
            State -> {
                if (spawnedComponents[State] != null) return@coroutineScope
                val jobRef = launch {
                    componentsRuntimeContainer.stateRuntime?.let {
                        val logic = it.stateLogic
                        val component = it.stateComponent
                        val behaviourRef = componentsRefsContainer.stateToBehaviourRef
                        component.initialize()
                        logic(component, behaviourRef)
                        component.finalize()
                    }
                }
                spawnedComponents = spawnedComponents + (State to jobRef)
            }
            Communication -> {
                if (spawnedComponents[Communication] != null) return@coroutineScope
                val jobRef = launch {
                    componentsRuntimeContainer.communicationRuntime?.let {
                        val logic = it.communicationLogic
                        val component = it.communicationComponent
                        val behaviourRef = componentsRefsContainer.communicationToBehaviourRef
                        component.initialize()
                        logic(component, behaviourRef)
                        component.finalize()
                    }
                }
                spawnedComponents = spawnedComponents + (Communication to jobRef)
            }
            Sensors -> {
                if (spawnedComponents[Sensors] != null) return@coroutineScope
                val jobRef = launch {
                    componentsRuntimeContainer.sensorsRuntime?.let {
                        val logic = it.sensorsLogic
                        val component = it.sensorsComponent
                        val behaviourRef = componentsRefsContainer.sensorsToBehaviourRef
                        component.initialize()
                        logic(component, behaviourRef)
                        component.finalize()
                    }
                }
                spawnedComponents = spawnedComponents + (Sensors to jobRef)
            }
            Actuators -> {
                if (spawnedComponents[Actuators] != null) return@coroutineScope
                val jobRef = launch {
                    componentsRuntimeContainer.actuatorsRuntime?.let {
                        val logic = it.actuatorsLogic
                        val component = it.actuatorsComponent
                        val behaviourRef = componentsRefsContainer.actuatorsToBehaviourRef
                        component.initialize()
                        logic(component, behaviourRef)
                        component.finalize()
                    }
                }
                spawnedComponents = spawnedComponents + (Actuators to jobRef)
            }
        }
    }

    fun kill(componentType: ComponentType) {
        spawnedComponents[componentType]?.let {
            it.cancel()
            spawnedComponents = spawnedComponents - componentType
        }
    }
}
