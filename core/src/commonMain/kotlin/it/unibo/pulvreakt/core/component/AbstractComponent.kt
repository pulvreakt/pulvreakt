package it.unibo.pulvreakt.core.component

import arrow.core.Either
import arrow.core.firstOrNone
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right
import it.unibo.pulvreakt.core.communicator.Communicator
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
import kotlin.reflect.KClass

/**
 * Predefined [Component] which handle out-of-the-box the [Communicator] needed to interact with other components.
 */
abstract class AbstractComponent<T : Any> : Component<T> {
    final override lateinit var di: DI
    protected val context by instance<Context>()
    private val unitManager by instance<ComponentModeReconfigurator>()
    private val communicatorFactory: () -> Communicator by provider()
    private lateinit var communicators: Map<Component<*>, Communicator>
    private lateinit var unitManagerJob: Job
    private lateinit var links: Set<Component<*>>

    override suspend fun initialize(): Either<String, Unit> = coroutineScope {
        either {
            ensure(::di.isInitialized) { "To use the component, the `setupInjector` method should be called first" }
            ensure(::links.isInitialized) { "The component must be initialized before with `setupComponentLink`" }
            communicators = links.associateBy { this@AbstractComponent }
                .mapValues { (source, destination) ->
                    communicatorFactory().apply {
                        setupInjector(this@AbstractComponent.di)
                        communicatorSetup(source, destination)
                    }
                }
            unitManagerJob = launch {
                unitManager.receiveModeUpdates().collect { (component, mode) ->
                    communicators[component]?.setMode(mode)
                }
            }
        }
    }

    final override fun setupInjector(kodein: DI) {
        di = kodein
    }

    override fun setupComponentLink(vararg components: Component<*>) {
        if (!::links.isInitialized) {
            links = emptySet()
        }
        links += components
    }

    override suspend fun finalize(): Either<String, Unit> = either {
        ensure(::communicators.isInitialized) { "The finalize method must be called after the initialize one" }
        if (::unitManagerJob.isInitialized) {
            unitManagerJob.cancel()
        }
        communicators.forEach { (_, communicator) -> communicator.finalize() }
    }

    final override suspend fun <P : Any, C : Component<P>> send(
        componentKClass: KClass<C>,
        message: P,
        serializer: KSerializer<P>,
    ): Either<String, Unit> = either {
        isDependencyInjectionInitialized().bind()
        ensure(::communicators.isInitialized) { "The send method must be called after the initialize one" }
        val communicator = communicators.getCommunicator(componentKClass).bind()
        communicator.sendToComponent(Json.encodeToString(serializer, message).encodeToByteArray())
    }

    final override suspend fun <P : Any, C : Component<P>> receive(
        componentKClass: KClass<C>,
        serializer: KSerializer<P>,
    ): Either<String, Flow<P>> = either {
        isDependencyInjectionInitialized().bind()
        ensure(::communicators.isInitialized) { "The receive method must be called after the initialize one" }
        val communicator = communicators.getCommunicator(componentKClass).bind()
        val flow = communicator.receiveFromComponent().bind()
        return flow.map { Json.decodeFromString(serializer, it.decodeToString()) }.right()
    }

    private fun isDependencyInjectionInitialized(): Either<String, Unit> = either {
        ensure(::di.isInitialized) { "Before start using, the `setupInjector` must be called" }
    }

    private fun <C : Component<*>> Map<Component<*>, Communicator>.getCommunicator(
        component: KClass<C>,
    ): Either<String, Communicator> =
        filterKeys { component.isInstance(it) }
            .values
            .firstOrNone()
            .toEither { "Communicator not available for component ${component.simpleName}" }

    companion object {
        /**
         * Sends a message to [C] component with message type [P].
         */
        suspend inline fun <reified P : Any, reified C : Component<P>> AbstractComponent<*>.send(
            message: P,
        ): Either<String, Unit> = send(C::class, message, serializer())

        /**
         * Receives a message from [C] component with message type [P].
         */
        suspend inline fun <reified P : Any, reified C : Component<P>> AbstractComponent<*>.receive(): Either<String, Flow<P>> =
            receive(C::class, serializer())
    }
}
