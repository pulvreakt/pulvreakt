package it.nicolasfarabegoli.pulverization.runtime.communication

import it.nicolasfarabegoli.pulverization.dsl.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.model.State
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

    override val remotePlaceProvider: RemotePlaceProvider by inject()

    override suspend fun setup(binding: Binding, remotePlace: RemotePlace?) {
        if (binding.first == binding.second) error("The binding '$binding' is an invalid configuration")
        val (receiver, sender) = when (binding) {
            State to Behaviour ->
                commManager.stateInstance to commManager.behaviourStateInstance

            Communication to Behaviour ->
                commManager.communicationInstance to commManager.behaviourCommunicationInstance

            Sensors to Behaviour ->
                commManager.sensorsInstance to commManager.behaviourSensorsInstance

            Actuators to Behaviour ->
                commManager.actuatorsInstance to commManager.behaviourActuatorsInstance

            Behaviour to State ->
                commManager.behaviourStateInstance to commManager.stateInstance

            Behaviour to Communication ->
                commManager.behaviourCommunicationInstance to commManager.communicationInstance

            Behaviour to Sensors ->
                commManager.behaviourSensorsInstance to commManager.sensorsInstance

            Behaviour to Actuators ->
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
