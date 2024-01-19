package it.unibo.pulvreakt.api.component

import arrow.core.Either
import arrow.core.firstOrNone
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.pulvreakt.api.communication.Channel
import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.api.reconfiguration.component.ComponentModeReconfigurator
import it.unibo.pulvreakt.errors.component.ComponentError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
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
 * Preconfigured abstract implementation of [Component].
 * This class provides in the companion object some helper methods to send and receive messages without specifying the serialization.
 * Preconfigures the component defining the [initialize] and [finalize] methods.
 *
 * This abstract class implements all the machinery needed for a component to work,
 * letting the user focusing only on the implementation of the [execute] method.
 * The [execute] method can fail with a [ComponentError] and it is executed in a coroutine scope.
 * An example of generic component implementation is the following:
 *
 * ```kotlin
 * class MyComponent : AbstractComponent() {
 *     override suspend fun execute(): Either<ComponentError, Unit> = either {
 *         // component logic implementation
 *     }
 * }
 * ```
 * When a pulverization model is adopted, used the component-specific class provided in the `pulverization` package.
 */
abstract class AbstractComponent<ID : Any> : Component {
    override lateinit var di: DI
    protected val context by instance<Context<ID>>()
    private val reconfigurator by instance<ComponentModeReconfigurator>()
    private val communicatorFactory: () -> Channel by provider()
    private lateinit var communicators: Map<ComponentRef, Channel>
    private lateinit var unitManagerJob: Job
    protected val links = mutableSetOf<ComponentRef>()
    private val logger = KotlinLogging.logger(this::class.simpleName!!)
    private val scope = CoroutineScope(Dispatchers.Default + Job())

    override fun getRef(): ComponentRef = ComponentRef.create(this)

    override suspend fun initialize(): Either<ComponentError, Unit> = coroutineScope {
        either {
            ensure(::di.isInitialized) { ComponentError.InjectorNotInitialized }
            ensure(links.isNotEmpty()) { ComponentError.WiringNotInitialized }
            communicators = links.associateWith { component ->
                val communicator = communicatorFactory()
                with(communicator) {
                    setupInjector(this@AbstractComponent.di)
                    channelSetup(this@AbstractComponent.getRef(), component)
                        .mapLeft { ComponentError.WrapCommunicatorError(it) }
                        .bind()
                }
                communicator
            }
            unitManagerJob = scope.launch {
                logger.debug { "Starting collecting new update" }
                reconfigurator.receiveModeUpdates().collect { (component, mode) ->
                    logger.debug { "Received mode update for $component: $mode" }
                    communicators[component]?.setMode(mode)
                }
            }
            logger.debug { "Component [${this@AbstractComponent::class.simpleName}] initialization concluded" }
        }
    }

    override suspend fun finalize(): Either<ComponentError, Unit> = either {
        ensure(::communicators.isInitialized) { ComponentError.FinalizedBeforeInitialization }
        if (::unitManagerJob.isInitialized) {
            unitManagerJob.cancel()
        }
        scope.coroutineContext.cancelChildren()
        communicators.forEach { (_, communicator) -> communicator.finalize() }
    }

    override fun setupWiring(vararg components: ComponentRef) {
        links += components.toSet()
    }

    final override suspend fun <P : Any> send(toComponent: ComponentRef, message: P, serializer: KSerializer<in P>): Either<ComponentError, Unit> =
        either {
            isDependencyInjectionInitialized().bind()
            ensure(::communicators.isInitialized) { ComponentError.ComponentNotInitialized }
            val communicator = communicators.getCommunicator(toComponent).bind()
            communicator.sendToComponent(Json.encodeToString(serializer, message).encodeToByteArray())
        }

    final override suspend fun <P : Any> receive(fromComponent: ComponentRef, serializer: KSerializer<out P>): Either<ComponentError, Flow<P>> =
        either {
            isDependencyInjectionInitialized().bind()
            ensure(::communicators.isInitialized) { ComponentError.ComponentNotInitialized }
            val communicator = communicators.getCommunicator(fromComponent).bind()
            val flow = communicator.receiveFromComponent().mapLeft { ComponentError.WrapCommunicatorError(it) }.bind()
            return flow.map { Json.decodeFromString(serializer, it.decodeToString()) }.right()
        }

    override fun setupInjector(kodein: DI) {
        di = kodein
    }

    private fun Map<ComponentRef, Channel>.getCommunicator(
        component: ComponentRef,
    ): Either<ComponentError, Channel> = filterKeys { it == component }
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
        suspend inline fun <reified P : Any> Component.send(toComponent: ComponentRef, message: P): Either<ComponentError, Unit> =
            send(toComponent, message, serializer())

        /**
         * Helper method to receive messages [fromComponent] without specifying the serialization.
         */
        suspend inline fun <reified P : Any> Component.receive(fromComponent: ComponentRef): Either<ComponentError, Flow<P>> =
            receive(fromComponent, serializer())
    }
}
