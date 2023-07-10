package it.unibo.pulvreakt.core.component

import arrow.core.Either
import arrow.core.firstOrNone
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right
import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.component.errors.ComponentError
import it.unibo.pulvreakt.core.context.Context
import it.unibo.pulvreakt.core.reconfiguration.component.ComponentModeReconfigurator
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.kodein.di.DI
import org.kodein.di.instance
import org.kodein.di.provider

/**
 * Predefined [Component] which handle out-of-the-box the [Communicator] needed to interact with other components.
 */
abstract class AbstractComponent : Component {
    override lateinit var di: DI
    protected val context by instance<Context>()
    private val unitManager by instance<ComponentModeReconfigurator>()
    private val communicatorFactory: () -> Communicator by provider()
    private lateinit var communicators: Map<ComponentRef, Communicator>
    private lateinit var unitManagerJob: Job
    private val links = mutableSetOf<ComponentRef>()

    override fun getRef(): ComponentRef = ComponentRef.create(this)

    override suspend fun initialize(): Either<ComponentError, Unit> = coroutineScope {
        either {
            ensure(::di.isInitialized) { ComponentError.InjectorNotInitialized }
            ensure(links.isNotEmpty()) { ComponentError.WiringNotInitialized }
            communicators = links.associateWith { component ->
                val communicator = communicatorFactory()
                with(communicator) {
                    setupInjector(this@AbstractComponent.di)
                    communicatorSetup(this@AbstractComponent.getRef(), component)
                        .mapLeft { ComponentError.WrapCommunicatorError(it) }
                        .bind()
                }
                communicator
            }
            unitManagerJob = launch {
                unitManager.receiveModeUpdates().collect { (component, mode) ->
                    communicators[component]?.setMode(mode)
                }
            }
        }
    }

    override suspend fun finalize(): Either<ComponentError, Unit> = either {
        ensure(::communicators.isInitialized) { ComponentError.FinalizedBeforeInitialization }
        if (::unitManagerJob.isInitialized) {
            unitManagerJob.cancel()
        }
        communicators.forEach { (_, communicator) -> communicator.finalize() }
    }

    override fun setupWiring(vararg components: ComponentRef) {
        links += components.toSet()
    }

    override suspend fun <P : Any> send(toComponent: ComponentRef, message: P, serializer: KSerializer<P>): Either<ComponentError, Unit> = either {
        isDependencyInjectionInitialized().bind()
        ensure(::communicators.isInitialized) { ComponentError.ComponentNotInitialized }
        val communicator = communicators.getCommunicator(toComponent).bind()
        communicator.sendToComponent(Json.encodeToString(serializer, message).encodeToByteArray())
    }

    override suspend fun <P : Any> receive(fromComponent: ComponentRef, serializer: KSerializer<P>): Either<ComponentError, Flow<P>> = either {
        isDependencyInjectionInitialized().bind()
        ensure(::communicators.isInitialized) { ComponentError.ComponentNotInitialized }
        val communicator = communicators.getCommunicator(fromComponent).bind()
        val flow = communicator.receiveFromComponent().mapLeft { ComponentError.WrapCommunicatorError(it) }.bind()
        return flow.map { Json.decodeFromString(serializer, it.decodeToString()) }.right()
    }

    override fun setupInjector(kodein: DI) {
        di = kodein
    }

    private fun Map<ComponentRef, Communicator>.getCommunicator(
        component: ComponentRef,
    ): Either<ComponentError, Communicator> = filterKeys { it == component }
        .values
        .firstOrNone()
        .toEither { ComponentError.ComponentNotRegistered(component) }

    private fun isDependencyInjectionInitialized(): Either<ComponentError, Unit> = either {
        ensure(::di.isInitialized) { ComponentError.InjectorNotInitialized }
    }

    companion object {
        /**
         * Helper method to send a [message] [toComponent] without specifying the serialization.
         */
        suspend inline fun <reified P : Any> Component.send(
            toComponent: ComponentRef,
            message: P,
        ): Either<ComponentError, Unit> = send(toComponent, message, serializer())

        /**
         * Helper method to receive messages [fromComponent] without specifying the serialization.
         */
        suspend inline fun <reified P : Any> Component.receive(
            fromComponent: ComponentRef,
        ): Either<ComponentError, Flow<P>> = receive(fromComponent, serializer())
    }
}
