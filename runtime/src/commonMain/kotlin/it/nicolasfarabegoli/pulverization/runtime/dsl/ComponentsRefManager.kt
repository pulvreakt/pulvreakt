package it.nicolasfarabegoli.pulverization.runtime.dsl

import it.nicolasfarabegoli.pulverization.core.CommunicationPayload
import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.core.StateComponent
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.componentsref.CommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.StateRef

inline fun <reified S : StateRepresentation> createStateRef(
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
    communicatorSupplier: () -> Communicator,
): StateRef<S> {
    if (!allComponents.contains(StateComponent)) return StateRef.createDummy()
    TODO()
}

inline fun <reified C : CommunicationPayload> createCommunicationRef(
    allComponents: Set<PulverizedComponentType>,
    deploymentUnit: Set<PulverizedComponentType>,
): CommunicationRef<C> = TODO()
