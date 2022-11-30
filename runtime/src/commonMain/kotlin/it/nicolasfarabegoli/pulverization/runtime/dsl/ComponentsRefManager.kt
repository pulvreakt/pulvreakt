package it.nicolasfarabegoli.pulverization.runtime.dsl

import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationPayload
import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.core.StateComponent
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ActuatorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.CommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.SensorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.StateRef
import kotlinx.serialization.KSerializer

internal fun <S : StateRepresentation> createStateRef(
    serializer: KSerializer<S>,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicatorSupplier: () -> Communicator = { error("No communication supplier given") },
): StateRef<S> {
    if (!allComponents.contains(StateComponent)) return StateRef.createDummy()
    return when (determinePlace(allComponents, deploymentUnit)) {
        Remote -> StateRef.create(serializer, communicatorSupplier())
        Local -> TODO()
    }
}

internal fun <C : CommunicationPayload> createCommunicationRef(
    serializer: KSerializer<C>,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicatorSupplier: () -> Communicator = { error("No communication supplier given") },
): CommunicationRef<C> {
    if (!allComponents.contains(CommunicationComponent)) return CommunicationRef.createDummy()
    return when (determinePlace(allComponents, deploymentUnit)) {
        Remote -> CommunicationRef.create(serializer, communicatorSupplier())
        Local -> TODO()
    }
}

internal fun <SS : Any> createSensorsRef(
    serializer: KSerializer<SS>,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicatorSupplier: () -> Communicator = { error("No communication supplier given") },
): SensorsRef<SS> {
    if (!allComponents.contains(CommunicationComponent)) return SensorsRef.createDummy()
    return when (determinePlace(allComponents, deploymentUnit)) {
        Remote -> SensorsRef.create(serializer, communicatorSupplier())
        Local -> TODO()
    }
}

internal fun <AS : Any> createActuatorsRef(
    serializer: KSerializer<AS>,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicatorSupplier: () -> Communicator = { error("No communication supplier given") },
): ActuatorsRef<AS> {
    if (!allComponents.contains(CommunicationComponent)) return ActuatorsRef.createDummy()
    return when (determinePlace(allComponents, deploymentUnit)) {
        Remote -> ActuatorsRef.create(serializer, communicatorSupplier())
        Local -> TODO()
    }
}

internal fun <S, C, SS, AS> setupComponentsRef(
    stateSer: KSerializer<S>,
    commSer: KSerializer<C>,
    senseSer: KSerializer<SS>,
    actSer: KSerializer<AS>,
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicatorSupplier: () -> Communicator,
): ComponentsRefInstances<S, C, SS, AS> where S : StateRepresentation, C : CommunicationPayload, SS : Any, AS : Any {
    val stateRef = createStateRef(stateSer, allComponents, deploymentUnit, communicatorSupplier)
    val commRef = createCommunicationRef(commSer, allComponents, deploymentUnit, communicatorSupplier)
    val sensorsRef = createSensorsRef(senseSer, allComponents, deploymentUnit, communicatorSupplier)
    val actuatorsRef = createActuatorsRef(actSer, allComponents, deploymentUnit, communicatorSupplier)
    return ComponentsRefInstances(stateRef, commRef, sensorsRef, actuatorsRef)
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

internal fun determinePlace(
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
): Placement {
    val remotes = allComponents - deploymentUnit
    println(remotes)
    TODO()
}
