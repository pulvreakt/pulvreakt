package example.components

import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class NeighboursMessage(val device: String, val location: Gps)

expect class CommunicationComp : Communication<NeighboursMessage>

suspend fun communicationLogic(comm: Communication<NeighboursMessage>, behaviour: BehaviourRef<NeighboursMessage>) =
    coroutineScope {
        val sendJob = launch {
            behaviour.receiveFromComponent().collect {
                comm.send(it)
            }
        }
        val receiveJob = launch {
            comm.receive().collect {
                behaviour.sendToComponent(it)
            }
        }
        sendJob.join()
        receiveJob.join()
    }
