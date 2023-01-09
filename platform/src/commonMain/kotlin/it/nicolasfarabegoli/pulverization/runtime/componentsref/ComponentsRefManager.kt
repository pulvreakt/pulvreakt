package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.core.ActuatorsComponent
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.LocalCommunicator
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

internal fun <S : Any> createStateRef(
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

internal fun <C : Any> createCommunicationRef(
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

internal data class ComponentsRefInstances<S : Any, C : Any, SS : Any, AS : Any>(
    val stateRef: StateRef<S>,
    val communicationRef: CommunicationRef<C>,
    val sensorsRef: SensorsRef<SS>,
    val actuatorsRef: ActuatorsRef<AS>,
)

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
