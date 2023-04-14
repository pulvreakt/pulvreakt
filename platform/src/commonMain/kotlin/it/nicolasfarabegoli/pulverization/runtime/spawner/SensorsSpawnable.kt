package it.nicolasfarabegoli.pulverization.runtime.spawner

import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.utils.SensorsLogicType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

internal class SensorsSpawnable<SS : Any>(
    private val sensors: SensorsContainer?,
    private val sensorsLogic: SensorsLogicType<SS>?,
    private val sensorsToBehaviourRef: BehaviourRef<SS>,
) : Spawnable {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var jobRef: Job? = null
    override fun spawn(): Job {
        jobRef = scope.launch {
            sensors?.let {
                it.initialize()
                sensorsLogic?.invoke(it, sensorsToBehaviourRef)
                it.finalize()
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
