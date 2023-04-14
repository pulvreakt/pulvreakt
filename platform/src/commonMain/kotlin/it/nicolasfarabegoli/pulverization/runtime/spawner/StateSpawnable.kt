package it.nicolasfarabegoli.pulverization.runtime.spawner

import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.utils.StateLogicType
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

internal class StateSpawnable<S : Any>(
    private val state: State<S>?,
    private val stateLogic: StateLogicType<S>?,
    private val stateToBehaviourRef: BehaviourRef<S>,
) : Spawnable {
    private var jobRef: Job? = null

    override suspend fun spawn(): Job = coroutineScope {
        jobRef = launch {
            state?.let {
                it.initialize()
                stateLogic?.invoke(it, stateToBehaviourRef)
                it.finalize()
            }
        }
        return@coroutineScope jobRef!!
    }

    override suspend fun kill() {
        jobRef?.let {
            it.cancelAndJoin()
            state?.finalize()
        }
    }
}
