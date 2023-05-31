package it.unibo.pulvreakt.core.component

import arrow.core.Either
import it.unibo.pulvreakt.core.utils.PulvreaktInjected
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

/**
 * Represents a component of the system.
 * @param T the type of the component.
 * Each component is characterized by a [name] and a [type].
 */
interface Component<T : Any> : Initializable, PulvreaktInjected {
    val name: String
    val type: ComponentType

    /**
     * Sets up the links with other components.
     */
    fun setupComponentLink(vararg components: Component<*>)

    /**
     * Sends a [message] to the component with type [componentKClass] leveraging the given [serializer].
     */
    suspend fun <P : Any, C : Component<P>> send(
        componentKClass: KClass<C>,
        message: P,
        serializer: KSerializer<P>,
    ): Either<String, Unit>

    /**
     * Receives messages from the component with type [componentKClass] leveraging the given [serializer].
     */
    suspend fun <P : Any, C : Component<P>> receive(
        componentKClass: KClass<C>,
        serializer: KSerializer<P>,
    ): Either<String, Flow<P>>

    /**
     * Executes the component logic.
     */
    suspend fun execute(): Either<String, Unit>
}
