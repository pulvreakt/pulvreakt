package it.nicolasfarabegoli.pulverization.runtime.dsl

import it.nicolasfarabegoli.pulverization.core.ActuatorsComponent
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationPayload
import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.LocalCommunicator
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ActuatorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.CommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.SensorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.StateRef
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

internal fun <S : StateRepresentation> createStateRef(
    serializer: KSerializer<S>,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicator: Communicator?,
): StateRef<S> {
    if (!allComponents.contains(StateComponent)) return StateRef.createDummy()
    return when (determinePlace(allComponents, deploymentUnit, StateComponent)) {
        Remote -> StateRef.create(serializer, communicator ?: error("No communicator given"))
        Local -> StateRef.create(serializer, LocalCommunicator())
        NoExists -> StateRef.createDummy()
    }
}

internal fun <C : CommunicationPayload> createCommunicationRef(
    serializer: KSerializer<C>,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicator: Communicator?,
): CommunicationRef<C> {
    if (!allComponents.contains(CommunicationComponent)) return CommunicationRef.createDummy()
    return when (determinePlace(allComponents, deploymentUnit, CommunicationComponent)) {
        Remote -> CommunicationRef.create(serializer, communicator ?: error("No communicator given"))
        Local -> CommunicationRef.create(serializer, LocalCommunicator())
        NoExists -> CommunicationRef.createDummy()
    }
}

internal fun <SS : Any> createSensorsRef(
    serializer: KSerializer<SS>,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicator: Communicator?,
): SensorsRef<SS> {
    if (!allComponents.contains(SensorsComponent)) return SensorsRef.createDummy()
    return when (determinePlace(allComponents, deploymentUnit, SensorsComponent)) {
        Remote -> SensorsRef.create(serializer, communicator ?: error("No communicator given"))
        Local -> SensorsRef.create(serializer, LocalCommunicator())
        NoExists -> SensorsRef.createDummy()
    }
}

internal fun <AS : Any> createActuatorsRef(
    serializer: KSerializer<AS>,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicator: Communicator?,
): ActuatorsRef<AS> {
    if (!allComponents.contains(ActuatorsComponent)) return ActuatorsRef.createDummy()
    return when (determinePlace(allComponents, deploymentUnit, ActuatorsComponent)) {
        Remote -> ActuatorsRef.create(serializer, communicator ?: error("No communicator given"))
        Local -> ActuatorsRef.create(serializer, LocalCommunicator())
        NoExists -> ActuatorsRef.createDummy()
    }
}

internal inline fun <reified S, reified C, reified SS, reified AS> setupComponentsRef(
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    noinline communicator: () -> Communicator?,
): ComponentsRefInstances<S, C, SS, AS> where S : StateRepresentation, C : CommunicationPayload, SS : Any, AS : Any =
    setupComponentsRef(
        serializer(),
        serializer(),
        serializer(),
        serializer(),
        allComponents,
        deploymentUnit,
        communicator,
    )

internal fun <S, C, SS, AS> setupComponentsRef(
    stateSer: KSerializer<S>,
    commSer: KSerializer<C>,
    senseSer: KSerializer<SS>,
    actSer: KSerializer<AS>,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicator: () -> Communicator?,
): ComponentsRefInstances<S, C, SS, AS> where S : StateRepresentation, C : CommunicationPayload, SS : Any, AS : Any {
    val stateRef = createStateRef(stateSer, allComponents, deploymentUnit, communicator())
    val commRef = createCommunicationRef(commSer, allComponents, deploymentUnit, communicator())
    val sensorsRef = createSensorsRef(senseSer, allComponents, deploymentUnit, communicator())
    val actuatorsRef = createActuatorsRef(actSer, allComponents, deploymentUnit, communicator())
    return ComponentsRefInstances(stateRef, commRef, sensorsRef, actuatorsRef)
}

internal inline fun <reified S : Any> setupBehaviourRef(
    component: PulverizedComponentType,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicator: Communicator?,
): BehaviourRef<S> = setupBehaviourRef(serializer(), component, allComponents, deploymentUnit, communicator)

internal fun <S : Any> setupBehaviourRef(
    serializer: KSerializer<S>,
    component: PulverizedComponentType,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicator: Communicator?,
): BehaviourRef<S> {
    if (!allComponents.contains(BehaviourComponent)) error("The Behaviour must be defined!")
    if (!allComponents.contains(component)) {
        error("Trying to create a BehaviourRef with $component but this component doesn't appear on the configuration")
    }
    return if (deploymentUnit.contains(BehaviourComponent)) {
        BehaviourRef.create(
            serializer,
            component,
            LocalCommunicator(),
        )
    } else {
        BehaviourRef.create(serializer, component, communicator ?: error("No communicator given"))
    }
}

internal data class ComponentsRefInstances<S, C, SS, AS>(
    val stateRef: StateRef<S>,
    val communicationRef: CommunicationRef<C>,
    val sensorsRef: SensorsRef<SS>,
    val actuatorsRef: ActuatorsRef<AS>,
) where S : StateRepresentation, C : CommunicationPayload, SS : Any, AS : Any

internal sealed interface Placement
internal object Remote : Placement
internal object Local : Placement
internal object NoExists : Placement

internal fun determinePlace(
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    component: PulverizedComponentType,
): Placement {
    if (!allComponents.contains(component)) return NoExists
    return if (deploymentUnit.contains(component)) {
        Local
    } else {
        Remote
    }
}
