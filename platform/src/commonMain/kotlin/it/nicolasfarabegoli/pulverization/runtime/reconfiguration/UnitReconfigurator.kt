package it.nicolasfarabegoli.pulverization.runtime.reconfiguration

import it.nicolasfarabegoli.pulverization.core.Initializable
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ActuatorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.CommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.SensorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.StateRef
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ReconfigurationRules

internal data class BehaviourRefsContainer<S : Any, C : Any, SS : Any, AS : Any>(
    val stateRef: StateRef<S>,
    val communicationRef: CommunicationRef<C>,
    val sensorsRef: SensorsRef<SS>,
    val actuatorsRef: ActuatorsRef<AS>,
)

internal data class ComponentsRefsContainer<S : Any, C : Any, SS : Any, AS : Any>(
    val behaviourRefs: BehaviourRefsContainer<S, C, SS, AS>,
    val stateRef: BehaviourRef<S>,
    val communicationRef: BehaviourRef<C>,
    val sensorsRef: BehaviourRef<SS>,
    val actuatorsRef: BehaviourRef<AS>,
)

/**
 * Class used for the reconfiguration of the deployment unit.
 * [reconfigurator] the component which propagate the new deployment.
 * [rules] the set of rules that should be checked.
 * [componentsRef] component references used to switch mode based on the rules.
 */
internal class UnitReconfigurator<S : Any, C : Any, SS : Any, AS : Any>(
    private val reconfigurator: Reconfigurator,
    private val rules: ReconfigurationRules?,
    private val componentsRef: ComponentsRefsContainer<S, C, SS, AS>,
) : Initializable {
    override suspend fun initialize() {
        super.initialize()
    }

    override suspend fun finalize() {
        super.finalize()
    }

    // TODO(implement methods)
}
