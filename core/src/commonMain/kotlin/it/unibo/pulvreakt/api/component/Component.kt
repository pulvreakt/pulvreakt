package it.unibo.pulvreakt.api.component

import arrow.core.Either
import it.unibo.pulvreakt.api.initializable.InjectAwareResource
import it.unibo.pulvreakt.api.initializable.ManagedResource
import it.unibo.pulvreakt.errors.component.ComponentError
import it.unibo.pulvreakt.errors.component.ComponentError.ComponentNotRegistered
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer

/**
 * Models a deployable component.
 * The component models a sub-function of a **Logical Device**.
 * A component can communicate with other components through the [send] and [receive] methods.
 * The link between components is established by the [setupWiring] that **MUST** be called before the [execute] method
 * which starts the execution of the component logic.
 * All the operations are asynchronous and non-blocking and they may fail with a [ComponentError].
 */
interface Component : ManagedResource<ComponentError>, InjectAwareResource {
    /**
     * Setup all the other [components] to which this component will communicate.
     * The [components] represents the symbolic references to the other components allowing a distributed communication.
     * This method is generally called by the runtime when the pulverization model is adopted.
     * In case of custom **Logical Device** partitioning this method should be called manually.
     */
    fun setupWiring(vararg components: ComponentRef)

    /**
     * Returns a symbolic reference to this component.
     * This method is used internally by the runtime and should not be called by the user.
     */
    fun getRef(): ComponentRef

    /**
     * Sends a [message] of type [P] [toComponent] by serializing it with the given [serializer].
     * This method can fail with a [ComponentNotRegistered] if the [toComponent] is not registered as link of this component.
     */
    suspend fun <P : Any> send(toComponent: ComponentRef, message: P, serializer: KSerializer<in P>): Either<ComponentError, Unit>

    /**
     * Receives a message of type [P] [fromComponent] by deserializing it with the given [serializer].
     * If the message pushed in the flow cannot be serialized with the given [serializer] the flow will fail with an exception.
     * This method can fail with a [ComponentNotRegistered] if the [fromComponent] is not registered as link of this component.
     */
    suspend fun <P : Any> receive(fromComponent: ComponentRef, serializer: KSerializer<out P>): Either<ComponentError, Flow<P>>

    /**
     * Starts the execution of the component logic.
     * The execution may fail with a [ComponentError].
     * Before calling this method the [setupWiring] method MUST be called.
     */
    suspend fun execute(): Either<ComponentError, Unit>
}
