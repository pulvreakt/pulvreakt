package it.unibo.pulvreakt.runtime.spawner

import it.unibo.pulvreakt.core.SensorsContainer
import it.unibo.pulvreakt.runtime.componentsref.BehaviourRef
import it.unibo.pulvreakt.runtime.utils.SensorsLogicType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import mu.KotlinLogging

internal class SensorsSpawnable<SS : Any>(
    private val sensors: SensorsContainer?,
    private val sensorsLogic: SensorsLogicType<SS>?,
    private val sensorsToBehaviourRef: BehaviourRef<SS>,
    private val scope: CoroutineScope,
) : Spawnable {
    private var jobRef: Job? = null
    private val logger = KotlinLogging.logger(this::class.simpleName!!)

    override fun spawn(): Job {
        jobRef?.cancel()
        jobRef = scope.launch {
            logger.debug { "Spawning sensor component" }
            sensors?.let {
                try {
                    it.initialize()
                    sensorsLogic?.invoke(it, sensorsToBehaviourRef)
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
            sensors?.finalize()
        }
    }
}
