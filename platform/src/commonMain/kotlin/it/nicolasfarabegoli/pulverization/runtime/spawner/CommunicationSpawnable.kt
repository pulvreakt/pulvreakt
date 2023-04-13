package it.nicolasfarabegoli.pulverization.runtime.spawner

import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.utils.CommunicationLogicType
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

internal class CommunicationSpawnable<C : Any>(
    private val communication: Communication<C>?,
    private val commLogic: CommunicationLogicType<C>?,
    private val commToBehaviourRef: BehaviourRef<C>,
) : Spawnable {
    private var jobRef: Job? = null

    override suspend fun spawn(): Job = coroutineScope {
        jobRef = launch {
            communication?.let {
                it.initialize()
                commLogic?.invoke(it, commToBehaviourRef)
                it.finalize()
            }
        }
        return@coroutineScope jobRef!!
    }

    override suspend fun kill() {
        jobRef?.let {
            it.cancelAndJoin()
            communication?.finalize()
        }
    }
}
