package it.nicolasfarabegoli.pulverization.runtime.spawner

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ComponentsRuntimeContainer
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.ComponentsRefsContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * Manager of the spawning of components execution.
 */
class SpawnerManager<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    componentsRuntimeContainer: ComponentsRuntimeContainer<S, C, SS, AS, O>,
    componentsRefsContainer: ComponentsRefsContainer<S, C, SS, AS>,
) {
    private var activeComponents = emptySet<ComponentType>()
    private val scope = CoroutineScope(Dispatchers.Default)

    private val behaviourSpawnable = BehaviourSpawnable(
        componentsRuntimeContainer.behaviourRuntime?.behaviourComponent,
        componentsRuntimeContainer.behaviourRuntime?.behaviourLogic,
        componentsRefsContainer.behaviourRefs.stateRef,
        componentsRefsContainer.behaviourRefs.communicationRef,
        componentsRefsContainer.behaviourRefs.sensorsRef,
        componentsRefsContainer.behaviourRefs.actuatorsRef,
        scope,
    )
    private val stateSpawnable = StateSpawnable(
        componentsRuntimeContainer.stateRuntime?.stateComponent,
        componentsRuntimeContainer.stateRuntime?.stateLogic,
        componentsRefsContainer.stateToBehaviourRef,
        scope,
    )
    private val commSpawnable = CommunicationSpawnable(
        componentsRuntimeContainer.communicationRuntime?.communicationComponent,
        componentsRuntimeContainer.communicationRuntime?.communicationLogic,
        componentsRefsContainer.communicationToBehaviourRef,
        scope,
    )
    private val sensorsSpawnable = SensorsSpawnable(
        componentsRuntimeContainer.sensorsRuntime?.sensorsComponent,
        componentsRuntimeContainer.sensorsRuntime?.sensorsLogic,
        componentsRefsContainer.sensorsToBehaviourRef,
        scope,
    )
    private val actuatorsSpawnable = ActuatorsSpawnable(
        componentsRuntimeContainer.actuatorsRuntime?.actuatorsComponent,
        componentsRuntimeContainer.actuatorsRuntime?.actuatorsLogic,
        componentsRefsContainer.actuatorsToBehaviourRef,
        scope,
    )

    /**
     * Spawn a fiber corresponding to the given [component].
     */
    suspend fun spawn(component: ComponentType) = operation(component, true)

    /**
     * Kill the fiber corresponding to the given [component].
     */
    suspend fun kill(component: ComponentType) = operation(component, false)

    /**
     * The current active components.
     */
    fun activeComponents(): Set<ComponentType> = activeComponents

    /**
     * Kill all the active components.
     */
    suspend fun killAll() {
        behaviourSpawnable.kill()
        stateSpawnable.kill()
        commSpawnable.kill()
        sensorsSpawnable.kill()
        actuatorsSpawnable.kill()
        activeComponents = emptySet()
    }

    private suspend fun operation(component: ComponentType, shouldSpawn: Boolean) {
        when (component) {
            is Behaviour -> if (shouldSpawn) behaviourSpawnable.spawn() else behaviourSpawnable.kill()
            is State -> if (shouldSpawn) stateSpawnable.spawn() else stateSpawnable.kill()
            is Communication -> if (shouldSpawn) commSpawnable.spawn() else commSpawnable.kill()
            is Sensors -> if (shouldSpawn) sensorsSpawnable.spawn() else sensorsSpawnable.kill()
            is Actuators -> if (shouldSpawn) actuatorsSpawnable.spawn() else actuatorsSpawnable.kill()
        }
        if (shouldSpawn) activeComponents += component else activeComponents -= component
    }
}
