package it.unibo.pulvreakt.runtime.spawner

import it.unibo.pulvreakt.core.State
import it.unibo.pulvreakt.runtime.componentsref.BehaviourRef
import it.unibo.pulvreakt.runtime.utils.StateLogicType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import mu.KotlinLogging

internal class StateSpawnable<S : Any>(
    private val state: State<S>?,
    private val stateLogic: StateLogicType<S>?,
    private val stateToBehaviourRef: BehaviourRef<S>,
    private val scope: CoroutineScope,
) : Spawnable {
    private var jobRef: Job? = null
    private val logger = KotlinLogging.logger(this::class.simpleName!!)

    override fun spawn(): Job {
        jobRef?.cancel()
        jobRef = scope.launch {
            logger.debug { "Spawning State component" }
            state?.let {
                try {
                    it.initialize()
                    stateLogic?.invoke(it, stateToBehaviourRef)
                    it.finalize()
                } catch (e: CancellationException) {
                    logger.debug(e) { "State component fiber cancelled" }
                }
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
