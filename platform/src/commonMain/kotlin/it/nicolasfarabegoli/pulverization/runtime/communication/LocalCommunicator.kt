package it.nicolasfarabegoli.pulverization.runtime.communication

import it.nicolasfarabegoli.pulverization.core.ActuatorsComponent
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class LocalCommunicator : Communicator, KoinComponent {

    override fun getKoin(): Koin = PulverizationKoinModule.koinApp?.koin ?: error("No Koin app defined")

    private val commManager: CommManager by inject()
    private lateinit var inbox: MutableSharedFlow<ByteArray>
    private lateinit var outbox: MutableSharedFlow<ByteArray>

    override suspend fun setup(binding: Binding, remotePlace: RemotePlace?) {
        if (binding.first == binding.second) error("The binding '$binding' is an invalid configuration")
        val (receiver, sender) = when (binding) {
            StateComponent to BehaviourComponent ->
                commManager.stateInstance to commManager.behaviourStateInstance

            CommunicationComponent to BehaviourComponent ->
                commManager.communicationInstance to commManager.behaviourCommunicationInstance

            SensorsComponent to BehaviourComponent ->
                commManager.sensorsInstance to commManager.behaviourSensorsInstance

            ActuatorsComponent to BehaviourComponent ->
                commManager.actuatorsInstance to commManager.behaviourActuatorsInstance

            BehaviourComponent to StateComponent ->
                commManager.behaviourStateInstance to commManager.stateInstance

            BehaviourComponent to CommunicationComponent ->
                commManager.behaviourCommunicationInstance to commManager.communicationInstance

            BehaviourComponent to SensorsComponent ->
                commManager.behaviourSensorsInstance to commManager.sensorsInstance

            BehaviourComponent to ActuatorsComponent ->
                commManager.behaviourActuatorsInstance to commManager.actuatorsInstance

            else -> error("The binding: $binding is an invalid configuration")
        }
        outbox = sender
        inbox = receiver
    }

    override suspend fun finalize() {
        // Do nothing here.
    }

    override suspend fun fireMessage(message: ByteArray) = coroutineScope { outbox.emit(message) }

    override fun receiveMessage(): SharedFlow<ByteArray> = inbox.asSharedFlow()
}
