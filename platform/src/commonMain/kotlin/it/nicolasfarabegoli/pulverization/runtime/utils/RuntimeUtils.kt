package it.nicolasfarabegoli.pulverization.runtime.utils

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ActuatorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.CommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ComponentRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.SensorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.StateRef
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.DeploymentUnitRuntimeConfiguration
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.BehaviourRefsContainer
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.ComponentsRefsContainer
import kotlinx.serialization.KSerializer

internal infix fun <F, S, R> Pair<F?, S?>.takeAllNotNull(body: (F, S) -> R): R? {
    // The local assignment is necessary because a problem with smart cast in Kotlin
    val f = first
    val s = second
    return if (f != null && s != null) body(f, s) else null
}

internal fun <S : Any, C : Any, SS : Any, AS : Any, O : Any>
    DeploymentUnitRuntimeConfiguration<S, C, SS, AS, O>.createComponentsRefs(
        startup: Map<ComponentType, ComponentRef.OperationMode>,
        stateSer: KSerializer<S>,
        commSer: KSerializer<C>,
        sensorsSer: KSerializer<SS>,
        actuatorsSer: KSerializer<AS>,
    ): ComponentsRefsContainer<S, C, SS, AS> {
    val missing = setOf(Behaviour, Communication, State, Sensors, Actuators) - deviceSpecification.components
    return ComponentsRefsContainer(
        BehaviourRefsContainer(
            if (State in missing) StateRef.createDummy() else StateRef.create(stateSer)
                .apply { setOperationMode(startup[State] ?: error("Missing start mode")) },
            if (Communication in missing) CommunicationRef.createDummy() else CommunicationRef.create(commSer)
                .apply { setOperationMode(startup[Communication] ?: error("Missing start mode")) },
            if (Sensors in missing) SensorsRef.createDummy() else SensorsRef.create(sensorsSer)
                .apply { setOperationMode(startup[Sensors] ?: error("Missing start mode")) },
            if (Actuators in missing) ActuatorsRef.createDummy() else ActuatorsRef.create(actuatorsSer)
                .apply { setOperationMode(startup[Actuators] ?: error("Missing start mode")) },
        ),
        // TODO(put start mode below)
        BehaviourRef.create(stateSer, State),
        BehaviourRef.create(commSer, Communication),
        BehaviourRef.create(sensorsSer, Sensors),
        BehaviourRef.create(actuatorsSer, Actuators),
    )
}
