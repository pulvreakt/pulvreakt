package it.unibo.pulvreakt.runtime.spawner

import it.unibo.pulvreakt.core.Behaviour
import it.unibo.pulvreakt.runtime.componentsref.ActuatorsRef
import it.unibo.pulvreakt.runtime.componentsref.CommunicationRef
import it.unibo.pulvreakt.runtime.componentsref.SensorsRef
import it.unibo.pulvreakt.runtime.componentsref.StateRef
import it.unibo.pulvreakt.runtime.utils.BehaviourLogicType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import mu.KotlinLogging

internal class BehaviourSpawnable<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    private val behaviour: Behaviour<S, C, SS, AS, O>?,
    private val behaviourLogic: BehaviourLogicType<S, C, SS, AS, O>?,
    private val behaviourStateRef: StateRef<S>,
    private val behaviourCommRef: CommunicationRef<C>,
    private val behaviourSensorsRef: SensorsRef<SS>,
    private val behaviourActuatorsRef: ActuatorsRef<AS>,
    private val scope: CoroutineScope,
) : Spawnable {
    private var jobRef: Job? = null
    private val logger = KotlinLogging.logger(this::class.simpleName!!)

    override fun spawn(): Job {
        jobRef?.cancel()
        jobRef = scope.launch {
            logger.debug { "Spawning Behaviour component" }
            behaviour?.let {
                try {
                    it.initialize()
                    behaviourLogic
                        ?.invoke(it, behaviourStateRef, behaviourCommRef, behaviourSensorsRef, behaviourActuatorsRef)
                    it.finalize()
                } catch (e: CancellationException) {
                    logger.debug(e) { "Behaviour component fiber cancelled" }
                }
            }
        }
        return jobRef!!
    }

    override suspend fun kill() {
        jobRef?.let {
            it.cancelAndJoin()
            behaviour?.finalize()
        }
    }
}
