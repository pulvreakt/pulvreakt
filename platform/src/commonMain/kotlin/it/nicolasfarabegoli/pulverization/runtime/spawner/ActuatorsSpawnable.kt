package it.nicolasfarabegoli.pulverization.runtime.spawner

import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.utils.ActuatorsLogicType
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

internal class ActuatorsSpawnable<AS : Any>(
    private val actuators: ActuatorsContainer?,
    private val actuatorsLogic: ActuatorsLogicType<AS>?,
    private val actuatorsToBehaviorRef: BehaviourRef<AS>,
) : Spawnable {
    private var jobRef: Job? = null

    override suspend fun spawn(): Job = coroutineScope {
        jobRef = launch {
            actuators?.let {
                it.initialize()
                actuatorsLogic?.invoke(it, actuatorsToBehaviorRef)
                it.finalize()
            }
        }
        return@coroutineScope jobRef!!
    }

    override suspend fun kill() {
        jobRef?.let {
            it.cancelAndJoin()
            actuators?.finalize()
        }
    }
}
