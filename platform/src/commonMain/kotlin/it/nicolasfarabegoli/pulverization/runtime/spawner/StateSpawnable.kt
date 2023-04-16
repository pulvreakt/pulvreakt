package it.nicolasfarabegoli.pulverization.runtime.spawner

import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.utils.StateLogicType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

internal class StateSpawnable<S : Any>(
    private val state: State<S>?,
    private val stateLogic: StateLogicType<S>?,
    private val stateToBehaviourRef: BehaviourRef<S>,
    private val scope: CoroutineScope,
) : Spawnable {
    private var jobRef: Job? = null

    override fun spawn(): Job {
        jobRef?.cancel()
        jobRef = scope.launch {
            state?.let {
                it.initialize()
                stateLogic?.invoke(it, stateToBehaviourRef)
                it.finalize()
            }
        }
        return jobRef!!
    }

    override suspend fun kill() {
        jobRef?.let {
            it.cancelAndJoin()
            state?.finalize()
        }
    }
}
