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

internal class LocalCommunicator : Communicator {

    private lateinit var inbox: MutableSharedFlow<ByteArray>
    private lateinit var outbox: MutableSharedFlow<ByteArray>

    override suspend fun setup(binding: Binding) {
        if (binding.first == binding.second) error("The binding '$binding' is an invalid configuration")
        val (receiver, sender) = when (binding) {
            StateComponent to BehaviourComponent ->
                CommManager.stateInstance to CommManager.behaviourStateInstance

            CommunicationComponent to BehaviourComponent ->
                CommManager.communicationInstance to CommManager.behaviourCommunicationInstance

            SensorsComponent to BehaviourComponent ->
                CommManager.sensorsInstance to CommManager.behaviourSensorsInstance

            ActuatorsComponent to BehaviourComponent ->
                CommManager.actuatorsInstance to CommManager.behaviourActuatorsInstance

            BehaviourComponent to StateComponent ->
                CommManager.behaviourStateInstance to CommManager.stateInstance

            BehaviourComponent to CommunicationComponent ->
                CommManager.behaviourCommunicationInstance to CommManager.communicationInstance

            BehaviourComponent to SensorsComponent ->
                CommManager.behaviourSensorsInstance to CommManager.sensorsInstance

            BehaviourComponent to ActuatorsComponent ->
                CommManager.behaviourActuatorsInstance to CommManager.actuatorsInstance

            else -> error("The binding: $binding is an invalid configuration")
        }
        outbox = sender
        inbox = receiver
    }

    override suspend fun fireMessage(message: ByteArray) = coroutineScope { outbox.emit(message) }

    override fun receiveMessage(): SharedFlow<ByteArray> = inbox.asSharedFlow()
}
