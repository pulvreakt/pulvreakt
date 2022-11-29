package it.nicolasfarabegoli.pulverization.runtime.communication

import it.nicolasfarabegoli.pulverization.core.ActuatorsComponent
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

internal class LocalCommunicator(override val binding: Binding) : Communicator {

    private lateinit var receiver: ReceiveChannel<ByteArray>
    private lateinit var sender: SendChannel<ByteArray>

    init {
        initialize(binding)
    }

    private fun initialize(binding: Binding) {
        when (binding.first) {
            StateComponent -> {
                sender = LocalCommunicatorManager.behaviourStateInstance
                receiver = LocalCommunicatorManager.stateInstance
            }

            CommunicationComponent -> {
                sender = LocalCommunicatorManager.behaviourCommunicationInstance
                receiver = LocalCommunicatorManager.communicationInstance
            }

            SensorsComponent -> {
                sender = LocalCommunicatorManager.behaviourSensorsInstance
                receiver = LocalCommunicatorManager.sensorsInstance
            }

            ActuatorsComponent -> {
                sender = LocalCommunicatorManager.behaviourActuatorsInstance
                receiver = LocalCommunicatorManager.actuatorsInstance
            }

            BehaviourComponent -> {
                when (binding.second) {
                    StateComponent -> {
                        sender = LocalCommunicatorManager.stateInstance
                        receiver = LocalCommunicatorManager.behaviourStateInstance
                    }

                    CommunicationComponent -> {
                        sender = LocalCommunicatorManager.communicationInstance
                        receiver = LocalCommunicatorManager.behaviourCommunicationInstance
                    }

                    SensorsComponent -> {
                        sender = LocalCommunicatorManager.sensorsInstance
                        receiver = LocalCommunicatorManager.behaviourSensorsInstance
                    }

                    ActuatorsComponent -> {
                        sender = LocalCommunicatorManager.actuatorsInstance
                        receiver = LocalCommunicatorManager.behaviourActuatorsInstance
                    }

                    BehaviourComponent -> error("The behaviour could not have a self reference")
                }
            }
        }
    }

    override suspend fun fireMessage(message: ByteArray) {
        sender.send(message)
    }

    override fun receiveMessage(): Flow<ByteArray> {
        return receiver.consumeAsFlow()
    }
}
