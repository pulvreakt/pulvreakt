package it.unibo.pulvreakt.core.component.pulverisation

import it.unibo.pulvreakt.core.component.AbstractComponent
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.ComponentType
import kotlinx.coroutines.flow.Flow

/**
 * Represents the Communication component in the pulverization model.
 */
abstract class Communication<Comm : Any> : AbstractComponent() {
    /**
     * Sends to the other linked devices the given [message].
     */
    abstract suspend fun send(message: Comm)

    /**
     * Receives the communication from the other linked devices.
     */
    abstract suspend fun receive(): Flow<Comm>

    final override fun getRef(): ComponentRef = ComponentRef.create(this, ComponentType.Communication)
}
