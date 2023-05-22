package it.unibo.pulvreakt.runtime.spawner

import it.unibo.pulvreakt.core.ActuatorsContainer
import it.unibo.pulvreakt.runtime.componentsref.BehaviourRef
import it.unibo.pulvreakt.runtime.utils.ActuatorsLogicType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import mu.KotlinLogging

internal class ActuatorsSpawnable<AS : Any>(
    private val actuators: ActuatorsContainer?,
    private val actuatorsLogic: ActuatorsLogicType<AS>?,
    private val actuatorsToBehaviorRef: BehaviourRef<AS>,
    private val scope: CoroutineScope,
) : Spawnable {
    private var jobRef: Job? = null
    private val logger = KotlinLogging.logger(this::class.simpleName!!)

    override fun spawn(): Job {
        jobRef?.cancel()
        jobRef = scope.launch {
            logger.debug { "Spawning Actuators component" }
            actuators?.let {
                try {
                    it.initialize()
                    actuatorsLogic?.invoke(it, actuatorsToBehaviorRef)
                    it.finalize()
                } catch (e: CancellationException) {
                    logger.debug(e) { "Actuators component fiber cancelled" }
                }
            }
        }
        return jobRef!!
    }

    override suspend fun kill() {
        jobRef?.let {
            it.cancelAndJoin()
            actuators?.finalize()
        }
    }
}
