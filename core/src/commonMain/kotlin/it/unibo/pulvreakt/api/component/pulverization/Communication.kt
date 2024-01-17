package it.unibo.pulvreakt.api.component.pulverization

import arrow.core.Either
import arrow.core.raise.either
import it.unibo.pulvreakt.api.component.ComponentKind
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.errors.component.ComponentError
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Represents the Communication component in the pulverization model.
 *
 * The communication component is used to [send] and [receive] messages from the other devices.
 */
abstract class Communication<ID : Any, Comm : Any>(
    private val serializer: KSerializer<CommunicationPayload<ID, Comm>>,
) : AbstractPulverizedComponent<ID>() {
    /**
     * Sends to the other linked devices the given [message].
     */
    abstract suspend fun send(message: CommunicationPayload<ID, Comm>)

    /**
     * Receives the communication from the other linked devices.
     */
    abstract suspend fun receive(): Flow<CommunicationPayload<ID, Comm>>

    final override fun getRef(): ComponentRef = ComponentRef.create(this, ComponentKind.Communication)

    override suspend fun execute(): Either<ComponentError, Unit> =
        coroutineScope {
            either {
                val behaviourRef = getComponentByType(ComponentKind.Behavior).bind()
                val receiveJob =
                    launch {
                        receive().collect {
                            send(behaviourRef, it, serializer).bind()
                        }
                    }

                val sendJob =
                    launch {
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
data class CommunicationPayload<ID : Any, Comm : Any>(val deviceId: ID, val payload: Comm)
