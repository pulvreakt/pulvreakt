package it.nicolasfarabegoli.pulverization.runtime.componentsref

import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.LocalCommunicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlaceProvider
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ComponentRef.OperationMode.Local
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ComponentRef.OperationMode.Remote
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Models the concept of local/remote reference to another component in a pulverized context.
 */
interface ComponentRef<S : Any> {
    /**
     * Represent the operation mode of the [ComponentRef].
     */
    enum class OperationMode {
        Remote, Local
    }

    /**
     * The current operation mode.
     */
    var operationMode: OperationMode

    /**
     * Method used to setup the component reference.
     * By default, this method no nothing.
     */
    suspend fun setup() {}

    /**
     * Method used to release resource created by the component, if any.
     */
    suspend fun finalize() {}

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

@Suppress("EmptyFunctionBlock")
internal class NoOpComponentRef<S : Any> : ComponentRef<S> {
    override suspend fun sendToComponent(message: S) { }
    override suspend fun receiveFromComponent(): Flow<S> = emptyFlow()
    override suspend fun receiveLastFromComponent(): S? = null
    override var operationMode: ComponentRef.OperationMode = Local
}

internal class ComponentRefImpl<S : Any>(
    private val serializer: KSerializer<S>,
    private val binding: Pair<ComponentType, ComponentType>,
) : ComponentRef<S>, KoinComponent {

    override fun getKoin(): Koin = PulverizationKoinModule.koinApp?.koin ?: error("No Koin app defined")

    private val remotePlaceProvider: RemotePlaceProvider by inject()
    private val remoteCommunicator: Communicator by inject()
    private var localCommunicator: Communicator = LocalCommunicator()

    private var last: S? = null

    companion object {
        inline operator fun <reified S : Any> invoke(
            binding: Pair<ComponentType, ComponentType>,
        ): ComponentRefImpl<S> = ComponentRefImpl(serializer(), binding)
    }

    override suspend fun setup() {
        localCommunicator.setup(binding, null)
        remoteCommunicator.setup(binding, remotePlaceProvider[binding.second])
    }

    override suspend fun sendToComponent(message: S) {
        val communicator = if (operationMode == Local) localCommunicator else remoteCommunicator
        communicator.fireMessage(Json.encodeToString(serializer, message).encodeToByteArray())
    }

    override suspend fun receiveFromComponent(): Flow<S> {
        return merge(
            localCommunicator.receiveMessage().map { Local to it },
            remoteCommunicator.receiveMessage().map { Remote to it },
        ).filter { (mode, _) -> mode == operationMode }
            .map { (_, content) -> content }
            .map { Json.decodeFromString(serializer, it.decodeToString()) }.onEach { last = it }
    }

    override suspend fun receiveLastFromComponent(): S? = last

    override var operationMode: ComponentRef.OperationMode = Local
}
