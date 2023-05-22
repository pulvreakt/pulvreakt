package it.unibo.pulvreakt.runtime.spawner

import it.unibo.pulvreakt.core.Communication
import it.unibo.pulvreakt.runtime.componentsref.BehaviourRef
import it.unibo.pulvreakt.runtime.utils.CommunicationLogicType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import mu.KotlinLogging

internal class CommunicationSpawnable<C : Any>(
    private val communication: Communication<C>?,
    private val commLogic: CommunicationLogicType<C>?,
    private val commToBehaviourRef: BehaviourRef<C>,
    private val scope: CoroutineScope,
) : Spawnable {
    private var jobRef: Job? = null
    private val logger = KotlinLogging.logger(this::class.simpleName!!)

    override fun spawn(): Job {
        jobRef?.cancel()
        jobRef = scope.launch {
            logger.debug { "Spawning Communication component" }
            communication?.let {
                try {
                    it.initialize()
                    commLogic?.invoke(it, commToBehaviourRef)
                    it.finalize()
                } catch (e: CancellationException) {
                    logger.debug(e) { "Sensor component fiber cancelled" }
                }
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
