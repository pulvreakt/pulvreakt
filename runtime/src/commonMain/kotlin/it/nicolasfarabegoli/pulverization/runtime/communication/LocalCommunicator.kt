package it.nicolasfarabegoli.pulverization.runtime.communication

import it.nicolasfarabegoli.pulverization.core.ActuatorsComponent
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal class LocalCommunicator(override val binding: Binding) : Communicator {

    private lateinit var receiver: MutableSharedFlow<ByteArray>
    private lateinit var sender: MutableSharedFlow<ByteArray>

    init {
        initialize(binding)
    }

    private fun initialize(binding: Binding) {
        when (binding.first) {
            StateComponent -> {
                if (binding.second == StateComponent) error("The binding could not have a self-reference")
                sender = LocalCommunicatorManager.behaviourStateInstance
                receiver = LocalCommunicatorManager.stateInstance
            }

            CommunicationComponent -> {
                if (binding.second == CommunicationComponent) error("The binding could not have a self-reference")
                sender = LocalCommunicatorManager.behaviourCommunicationInstance
                receiver = LocalCommunicatorManager.communicationInstance
            }

            SensorsComponent -> {
                if (binding.second == SensorsComponent) error("The binding could not have a self-reference")
                sender = LocalCommunicatorManager.behaviourSensorsInstance
                receiver = LocalCommunicatorManager.sensorsInstance
            }

            ActuatorsComponent -> {
                if (binding.second == ActuatorsComponent) error("The binding could not have a self-reference")
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

    override suspend fun fireMessage(message: ByteArray) = coroutineScope { sender.emit(message) }

    override fun receiveMessage(): SharedFlow<ByteArray> = receiver.asSharedFlow()
}
