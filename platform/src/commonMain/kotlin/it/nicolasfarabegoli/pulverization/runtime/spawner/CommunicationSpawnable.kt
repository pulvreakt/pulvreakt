package it.nicolasfarabegoli.pulverization.runtime.spawner

import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.utils.CommunicationLogicType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

internal class CommunicationSpawnable<C : Any>(
    private val communication: Communication<C>?,
    private val commLogic: CommunicationLogicType<C>?,
    private val commToBehaviourRef: BehaviourRef<C>,
    private val scope: CoroutineScope,
) : Spawnable {
    private var jobRef: Job? = null

    override fun spawn(): Job {
        jobRef?.cancel()
        jobRef = scope.launch {
            communication?.let {
                it.initialize()
                commLogic?.invoke(it, commToBehaviourRef)
                it.finalize()
            }
        }
        return jobRef!!
    }

    override suspend fun kill() {
        jobRef?.let {
            it.cancelAndJoin()
            communication?.finalize()
        }
    }
}
