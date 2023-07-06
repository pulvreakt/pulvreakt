package it.unibo.pulvreakt.core.component

import arrow.core.Either
import it.unibo.pulvreakt.core.component.errors.ComponentError
import it.unibo.pulvreakt.core.utils.Initializable
import it.unibo.pulvreakt.core.utils.PulvreaktInjected
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer

/**
 * Represents a component of the system.
 */
interface Component<T : Any> : Initializable<ComponentError>, PulvreaktInjected {
    /**
     * Setup all the other [components] to which this component will communicate.
     */
    fun setupWiring(vararg components: ComponentRef<*>)

    /**
     * Returns a symbolic reference to this component.
     * This method is used internally by the runtime and should not be called by the user.
     *
     * To get a reference to a component from a type, use [ComponentRef.create].
     */
    fun getRef(): ComponentRef<T> = ComponentRef.create(this)

    /**
     * Sends a [message] [toComponent] by serializing it with the given [serializer].
     */
    suspend fun send(
        toComponent: ComponentRef<*>,
        message: T,
        serializer: KSerializer<T>,
    ): Either<ComponentError, Unit>

    /**
     * Receives a message [fromComponent] by deserializing it with the given [serializer].
     * @param P the type of the message to receive.
     */
    suspend fun <P : Any> receive(
        fromComponent: ComponentRef<P>,
        serializer: KSerializer<P>,
    ): Either<ComponentError, Flow<P>>

    /**
     * Executes the component logic.
     */
    suspend fun execute(): Either<ComponentError, Unit>
}
