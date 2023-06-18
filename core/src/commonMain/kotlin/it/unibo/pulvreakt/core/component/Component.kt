package it.unibo.pulvreakt.core.component

import arrow.core.Either
import it.unibo.pulvreakt.core.utils.PulvreaktInjected

/**
 * Represents a component of the system.
 * @param T the type of the component.
 * Each component is characterized by a [name] and a [type].
 */
interface Component : Initializable, PulvreaktInjected {
//    val name: String
//    val type: ComponentType<T>
//
//    /**
//     * Sets up the links with other components.
//     */
//    fun setupComponentLink(vararg components: Component<*>)
//
//    /**
//     * Sends a [message] [toComponent] leveraging the given [serializer].
//     */
//    suspend fun <P : Any> send(
//        toComponent: ComponentType<P>,
//        message: P,
//        serializer: KSerializer<P>,
//    ): Either<String, Unit>
//
//    /**
//     * Receives messages [fromComponent] leveraging the given [serializer].
//     */
//    suspend fun <P : Any> receive(
//        fromComponent: ComponentType<P>,
//        serializer: KSerializer<P>,
//    ): Either<String, Flow<P>>

    /**
     * Executes the component logic.
     */
    suspend fun execute(): Either<String, Unit>
}
