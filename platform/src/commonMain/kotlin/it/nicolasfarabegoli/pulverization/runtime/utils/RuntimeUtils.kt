package it.nicolasfarabegoli.pulverization.runtime.utils

import it.nicolasfarabegoli.pulverization.dsl.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.model.State
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ActuatorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.CommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ComponentRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ComponentRef.OperationMode.Local
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ComponentRef.OperationMode.Remote
import it.nicolasfarabegoli.pulverization.runtime.componentsref.SensorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.StateRef
import it.nicolasfarabegoli.pulverization.runtime.dsl.model.DeploymentUnitRuntimeConfiguration
import it.nicolasfarabegoli.pulverization.runtime.dsl.model.Host
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.BehaviourRefsContainer
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.ComponentsRefsContainer
import kotlinx.serialization.KSerializer

internal infix fun <F, S, R> Pair<F?, S?>.takeAllNotNull(body: (F, S) -> R): R? {
    // The local assignment is necessary because a problem with smart cast in Kotlin
    val f = first
    val s = second
    return if (f != null && s != null) body(f, s) else null
}

/**
 * Creates the components ref.
 */
fun <S, C, SS, AS, O> DeploymentUnitRuntimeConfiguration<S, C, SS, AS, O>.createComponentsRefs(
    stateSer: KSerializer<S>,
    commSer: KSerializer<C>,
    sensorsSer: KSerializer<SS>,
    actuatorsSer: KSerializer<AS>,
): ComponentsRefsContainer<S, C, SS, AS> where S : Any, C : Any, SS : Any, AS : Any, O : Any {
    val missing = setOf(Behaviour, Communication, State, Sensors, Actuators) - deviceSpecification.components
    return ComponentsRefsContainer(
        BehaviourRefsContainer(
            if (State in missing) StateRef.createDummy() else StateRef.create(stateSer),
            if (Communication in missing) CommunicationRef.createDummy() else CommunicationRef.create(commSer),
            if (Sensors in missing) SensorsRef.createDummy() else SensorsRef.create(sensorsSer),
            if (Actuators in missing) ActuatorsRef.createDummy() else ActuatorsRef.create(actuatorsSer),
        ),
        BehaviourRef.create(stateSer, State),
        BehaviourRef.create(commSer, Communication),
        BehaviourRef.create(sensorsSer, Sensors),
        BehaviourRef.create(actuatorsSer, Actuators),
    )
}

/**
 * Setup the behaviour references mode.
 */
fun <S : Any, C : Any, SS : Any, AS : Any> ComponentsRefsContainer<S, C, SS, AS>.setupBehaviourMode(
    component: ComponentType,
    mode: ComponentRef.OperationMode,
) {
    when (component) {
        is Behaviour -> { /* Do nothing here */
        }

        is State -> {
            stateToBehaviourRef.operationMode = mode; behaviourRefs.stateRef.operationMode = mode
        }

        is Communication -> {
            communicationToBehaviourRef.operationMode = mode
            behaviourRefs.communicationRef.operationMode = mode
        }

        is Sensors -> {
            sensorsToBehaviourRef.operationMode = mode
            behaviourRefs.sensorsRef.operationMode = mode
        }

        is Actuators -> {
            actuatorsToBehaviourRef.operationMode = mode
            behaviourRefs.actuatorsRef.operationMode = mode
        }
    }
}

/**
 * Setup the components refs.
 */
suspend fun <S : Any, C : Any, SS : Any, AS : Any> ComponentsRefsContainer<S, C, SS, AS>.setupRefs() {
    behaviourRefs.stateRef.setup()
    behaviourRefs.communicationRef.setup()
    behaviourRefs.sensorsRef.setup()
    behaviourRefs.actuatorsRef.setup()
    stateToBehaviourRef.setup()
    communicationToBehaviourRef.setup()
    sensorsToBehaviourRef.setup()
    actuatorsToBehaviourRef.setup()
}

internal fun <S : Any, C : Any, SS : Any, AS : Any> ComponentsRefsContainer<S, C, SS, AS>.setupOperationMode(
    startHosts: Map<ComponentType, Host>,
    currentHost: Host,
) {
    val localComponents = startHosts.filter { (_, host) -> host == currentHost }.map { (component, _) -> component }
    val remoteComponents = startHosts.filter { (_, host) -> host != currentHost }.map { (component, _) -> component }

    if (Behaviour in localComponents) {
        localComponents.forEach { setupBehaviourMode(it, Local) }
        remoteComponents.forEach { setupBehaviourMode(it, Remote) }
    } else {
        localComponents.forEach { setupBehaviourMode(it, Remote) }
    }
}
