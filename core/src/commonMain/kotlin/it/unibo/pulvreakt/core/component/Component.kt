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
interface Component<T : Any> : Initializable, PulvreaktInjected {
    fun setupWiring(vararg components: ComponentRef<*>)

    fun getRef(): ComponentRef<T> = ComponentRef.create(this)

    suspend fun send(
        toComponent: ComponentRef<*>,
        message: T,
        serializer: KSerializer<T>,
    ): Either<ComponentError, Unit>

    suspend fun <P : Any> receive(
        fromComponent: ComponentRef<P>,
        serializer: KSerializer<P>,
    ): Either<ComponentError, Flow<P>>

    /**
     * Executes the component logic.
     */
    suspend fun execute(): Either<ComponentError, Unit>
}
