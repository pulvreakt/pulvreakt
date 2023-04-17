package it.nicolasfarabegoli.pulverization.runtime.spawner

import co.touchlab.kermit.Logger
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.utils.SensorsLogicType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

internal class SensorsSpawnable<SS : Any>(
    private val sensors: SensorsContainer?,
    private val sensorsLogic: SensorsLogicType<SS>?,
    private val sensorsToBehaviourRef: BehaviourRef<SS>,
    private val scope: CoroutineScope,
) : Spawnable {
    private var jobRef: Job? = null
    private val logger = Logger.withTag(this::class.simpleName!!)

    override fun spawn(): Job {
        jobRef?.cancel()
        jobRef = scope.launch {
            logger.d { "Spawning sensor component" }
            sensors?.let {
                try {
                    it.initialize()
                    sensorsLogic?.invoke(it, sensorsToBehaviourRef)
                    it.finalize()
                } catch (e: CancellationException) {
                    logger.d(e) { "Sensor component fiber cancelled" }
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
