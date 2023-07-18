package it.unibo.pulvreakt.core.component.pulverisation

import arrow.core.Either
import arrow.core.raise.either
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.core.component.errors.ComponentError
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Represents the Communication component in the pulverization model.
 */
abstract class Communication<Comm : Any>(
    private val serializer: KSerializer<CommunicationPayload<Comm>>,
) : AbstractPulverisedComponent() {
    /**
     * Sends to the other linked devices the given [message].
     */
    abstract suspend fun send(message: CommunicationPayload<Comm>)

    /**
     * Receives the communication from the other linked devices.
     */
    abstract suspend fun receive(): Flow<CommunicationPayload<Comm>>

    final override fun getRef(): ComponentRef = ComponentRef.create(this, ComponentType.Communication)

    override suspend fun execute(): Either<ComponentError, Unit> = coroutineScope {
        either {
            val behaviourRef = getComponentByType(ComponentType.Behaviour).bind()
            val receiveJob = launch {
                receive().collect {
                    send(behaviourRef, it, serializer).bind()
                }
            }

            val sendJob = launch {
                receive(behaviourRef, serializer).bind().collect {
                    send(it)
                }
            }

            receiveJob.join()
            sendJob.join()
        }
    }
}

/**
 * Represents the payload of a communication.
 * @param Comm the type of payload communication.
 * @param deviceId the id of the device that sent the communication.
 * @param payload the payload of the communication.
 */
@Serializable
data class CommunicationPayload<Comm : Any>(val deviceId: Int, val payload: Comm)
