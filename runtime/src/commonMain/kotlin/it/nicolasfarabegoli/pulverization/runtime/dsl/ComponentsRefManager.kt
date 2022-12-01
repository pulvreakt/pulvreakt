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
    communicatorSupplier: () -> Communicator,
): StateRef<S> {
    return when (determinePlace(allComponents, deploymentUnit, StateComponent)) {
        Remote -> StateRef.create(serializer, communicatorSupplier())
        Local -> StateRef.create(serializer, LocalCommunicator())
        NoExists -> StateRef.createDummy()
    }
}

internal fun <C : CommunicationPayload> createCommunicationRef(
    serializer: KSerializer<C>,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicatorSupplier: () -> Communicator,
): CommunicationRef<C> {
    if (!allComponents.contains(CommunicationComponent)) return CommunicationRef.createDummy()
    return when (determinePlace(allComponents, deploymentUnit, CommunicationComponent)) {
        Remote -> CommunicationRef.create(serializer, communicatorSupplier())
        Local -> CommunicationRef.create(serializer, LocalCommunicator())
        NoExists -> CommunicationRef.createDummy()
    }
}

internal fun <SS : Any> createSensorsRef(
    serializer: KSerializer<SS>,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicatorSupplier: () -> Communicator,
): SensorsRef<SS> {
    if (!allComponents.contains(CommunicationComponent)) return SensorsRef.createDummy()
    return when (determinePlace(allComponents, deploymentUnit, SensorsComponent)) {
        Remote -> SensorsRef.create(serializer, communicatorSupplier())
        Local -> SensorsRef.create(serializer, LocalCommunicator())
        NoExists -> SensorsRef.createDummy()
    }
}

internal fun <AS : Any> createActuatorsRef(
    serializer: KSerializer<AS>,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicatorSupplier: () -> Communicator,
): ActuatorsRef<AS> {
    if (!allComponents.contains(CommunicationComponent)) return ActuatorsRef.createDummy()
    return when (determinePlace(allComponents, deploymentUnit, ActuatorsComponent)) {
        Remote -> ActuatorsRef.create(serializer, communicatorSupplier())
        Local -> ActuatorsRef.create(serializer, LocalCommunicator())
        NoExists -> ActuatorsRef.createDummy()
    }
}

internal inline fun <reified S, reified C, reified SS, reified AS> setupComponentsRef(
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    noinline communicatorSupplier: () -> Communicator = { error("No communicator given") },
): ComponentsRefInstances<S, C, SS, AS> where S : StateRepresentation, C : CommunicationPayload, SS : Any, AS : Any =
    setupComponentsRef(
        serializer(),
        serializer(),
        serializer(),
        serializer(),
        allComponents,
        deploymentUnit,
        communicatorSupplier,
    )

internal fun <S, C, SS, AS> setupComponentsRef(
    stateSer: KSerializer<S>,
    commSer: KSerializer<C>,
    senseSer: KSerializer<SS>,
    actSer: KSerializer<AS>,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicatorSupplier: () -> Communicator = { error("No communicator given") },
): ComponentsRefInstances<S, C, SS, AS> where S : StateRepresentation, C : CommunicationPayload, SS : Any, AS : Any {
    val stateRef = createStateRef(stateSer, allComponents, deploymentUnit, communicatorSupplier)
    val commRef = createCommunicationRef(commSer, allComponents, deploymentUnit, communicatorSupplier)
    val sensorsRef = createSensorsRef(senseSer, allComponents, deploymentUnit, communicatorSupplier)
    val actuatorsRef = createActuatorsRef(actSer, allComponents, deploymentUnit, communicatorSupplier)
    return ComponentsRefInstances(stateRef, commRef, sensorsRef, actuatorsRef)
}

internal fun <S : Any> setupBehaviourRef(
    serializer: KSerializer<S>,
    component: PulverizedComponentType,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicatorSupplier: () -> Communicator = { error("No communicator given") },
): BehaviourRef<S> {
    if (!allComponents.contains(BehaviourComponent)) error("The Behavior must be defined!")
    return if (deploymentUnit.contains(BehaviourComponent)) BehaviourRef.create(
        serializer,
        component,
        LocalCommunicator(),
    )
    else BehaviourRef.create(serializer, component, communicatorSupplier())
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
    return if (deploymentUnit.contains(component)) Local
    else Remote
}
