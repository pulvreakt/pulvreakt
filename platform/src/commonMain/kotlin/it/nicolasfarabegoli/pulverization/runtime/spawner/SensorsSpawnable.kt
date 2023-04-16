package it.nicolasfarabegoli.pulverization.runtime.spawner

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

    override fun spawn(): Job {
        jobRef?.cancel()
        jobRef = scope.launch {
            println("Sensor spawned")
            sensors?.let {
                try {
                    it.initialize()
                    sensorsLogic?.invoke(it, sensorsToBehaviourRef)
                    it.finalize()
                } catch (e: CancellationException) {
                    println("Sensors job is cancelled $e")
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
