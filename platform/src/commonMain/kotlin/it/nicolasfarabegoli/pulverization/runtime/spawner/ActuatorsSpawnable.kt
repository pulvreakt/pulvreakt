package it.nicolasfarabegoli.pulverization.runtime.spawner

import co.touchlab.kermit.Logger
import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.utils.ActuatorsLogicType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

internal class ActuatorsSpawnable<AS : Any>(
    private val actuators: ActuatorsContainer?,
    private val actuatorsLogic: ActuatorsLogicType<AS>?,
    private val actuatorsToBehaviorRef: BehaviourRef<AS>,
    private val scope: CoroutineScope,
) : Spawnable {
    private var jobRef: Job? = null
    private val logger = Logger.withTag(this::class.simpleName!!)

    override fun spawn(): Job {
        jobRef?.cancel()
        jobRef = scope.launch {
            logger.d { "Spawning Actuators component" }
            actuators?.let {
                try {
                    it.initialize()
                    actuatorsLogic?.invoke(it, actuatorsToBehaviorRef)
                    it.finalize()
                } catch (e: CancellationException) {
                    logger.d(e) { "Actuators component fiber cancelled" }
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
