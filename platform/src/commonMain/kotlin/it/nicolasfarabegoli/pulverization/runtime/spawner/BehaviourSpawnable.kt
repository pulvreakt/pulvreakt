package it.nicolasfarabegoli.pulverization.runtime.spawner

import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ActuatorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.CommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.SensorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.StateRef
import it.nicolasfarabegoli.pulverization.runtime.utils.BehaviourLogicType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

internal class BehaviourSpawnable<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    private val behaviour: Behaviour<S, C, SS, AS, O>?,
    private val behaviourLogic: BehaviourLogicType<S, C, SS, AS, O>?,
    private val behaviourStateRef: StateRef<S>,
    private val behaviourCommRef: CommunicationRef<C>,
    private val behaviourSensorsRef: SensorsRef<SS>,
    private val behaviourActuatorsRef: ActuatorsRef<AS>,
) : Spawnable {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var jobRef: Job? = null

    override fun spawn(): Job {
        jobRef = scope.launch {
            behaviour?.let {
                it.initialize()
                behaviourLogic
                    ?.invoke(it, behaviourStateRef, behaviourCommRef, behaviourSensorsRef, behaviourActuatorsRef)
                it.finalize()
            }
        }
        return jobRef!!
    }

    override suspend fun kill() {
        jobRef?.let {
            it.cancelAndJoin()
            behaviour?.finalize()
        }
    }
}
