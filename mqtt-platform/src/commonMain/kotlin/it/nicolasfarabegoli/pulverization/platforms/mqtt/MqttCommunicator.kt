package it.nicolasfarabegoli.pulverization.platforms.mqtt

import it.nicolasfarabegoli.pulverization.dsl.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.model.show
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlace
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlaceProvider
import it.nicolasfarabegoli.pulverization.runtime.context.ExecutionContext
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Implement the [Communicator] interface relying on MQTT as a platform for communications.
 */
expect class MqttCommunicator(
    hostname: String = "localhost",
    port: Int = 1882,
    username: String = "guest",
    password: String = "guest",
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : Communicator

/**
 * Default representation for a [RemotePlaceProvider] used by the [MqttCommunicator].
 */
fun defaultMqttRemotePlace(): RemotePlaceProvider = object : RemotePlaceProvider, KoinComponent {
    override fun getKoin(): Koin = PulverizationKoinModule.koinApp?.koin ?: error("No Koin app defined")
    override val context: ExecutionContext by inject()

    override fun get(type: ComponentType): RemotePlace {
        return RemotePlace(context.deviceID, type.show())
    }
}
