package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

/**
 * Models the concept of local/remote reference to another component in a pulverized context.
 */
interface ComponentRef<S : Any> {
    /**
     * Method used to setup the component reference.
     * By default, this method no nothing.
     */
    suspend fun setup() {}

    /**
     * Send a [message] to the component.
     */
    suspend fun sendToComponent(message: S)

    /**
     * Receive a message from the component as a [Flow] of messages.
     */
    suspend fun receiveFromComponent(): Flow<S>

    /**
     * Receive the last published message (if any) from the component.
     */
    suspend fun receiveLastFromComponent(): S?
}

internal class NoOpComponentRef<S : Any> : ComponentRef<S> {
    override suspend fun sendToComponent(message: S) = TODO("Not yet implemented")
    override suspend fun receiveFromComponent(): Flow<S> = TODO("Not yet implemented")
    override suspend fun receiveLastFromComponent(): S = TODO("Not yet implemented")
}

internal class ComponentRefImpl<S : Any>(
    private val serializer: KSerializer<S>,
    private val binding: Pair<PulverizedComponentType, PulverizedComponentType>,
    private val communicator: Communicator,
) : ComponentRef<S> {

    private var last: S? = null

    companion object {
        inline operator fun <reified S : Any> invoke(
            binding: Pair<PulverizedComponentType, PulverizedComponentType>,
            communicator: Communicator,
        ): ComponentRefImpl<S> = ComponentRefImpl(serializer(), binding, communicator)
    }

    override suspend fun setup() {
        communicator.setup(binding)
    }

    override suspend fun sendToComponent(message: S) {
        communicator.fireMessage(Json.encodeToString(serializer, message).encodeToByteArray())
    }

    override suspend fun receiveFromComponent(): Flow<S> {
        return communicator.receiveMessage().map { Json.decodeFromString(serializer, it.decodeToString()) }
            .onEach { last = it }
    }

    override suspend fun receiveLastFromComponent(): S? = last
}
