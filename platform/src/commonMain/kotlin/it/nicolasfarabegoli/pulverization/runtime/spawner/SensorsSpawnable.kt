package it.nicolasfarabegoli.pulverization.runtime.spawner

import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.utils.SensorsLogicType
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

internal class SensorsSpawnable<SS : Any>(
    private val sensors: SensorsContainer?,
    private val sensorsLogic: SensorsLogicType<SS>?,
    private val sensorsToBehaviourRef: BehaviourRef<SS>,
) : Spawnable {
    private var jobRef: Job? = null
    override suspend fun spawn(): Job = coroutineScope {
        jobRef = launch {
            sensors?.let {
                it.initialize()
                sensorsLogic?.invoke(it, sensorsToBehaviourRef)
                it.finalize()
            }
        }
        return@coroutineScope jobRef!!
    }

    override suspend fun kill() {
        jobRef?.let {
            it.cancelAndJoin()
            sensors?.finalize()
        }
    }
}
