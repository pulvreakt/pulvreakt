package it.unibo.pulvreakt.core.component

import it.unibo.pulvreakt.core.communicator.Communicator

/**
 * Predefined [Component] which handle out-of-the-box the [Communicator] needed to interact with other components.
 */
// abstract class AbstractComponent<T : Any> : Component<T> {
//    final override lateinit var di: DI
//    protected val context by instance<Context>()
//    private val unitManager by instance<ComponentModeReconfigurator>()
//    private val communicatorFactory: () -> Communicator by provider()
//    private lateinit var communicators: Map<Component<*>, Communicator>
//    private lateinit var unitManagerJob: Job
//    private lateinit var links: Set<Component<*>>
//
//    override suspend fun initialize(): Either<String, Unit> = coroutineScope {
//        either {
//            ensure(::di.isInitialized) { "To use the component, the `setupInjector` method should be called first" }
//            ensure(::links.isInitialized) { "The component must be initialized before with `setupComponentLink`" }
//            communicators = links.associateBy { this@AbstractComponent }
//                .mapValues { (source, destination) ->
//                    communicatorFactory().apply {
//                        setupInjector(this@AbstractComponent.di)
//                        communicatorSetup(source, destination)
//                    }
//                }
//            unitManagerJob = launch {
//                unitManager.receiveModeUpdates().collect { (component, mode) ->
//                    communicators[component]?.setMode(mode)
//                }
//            }
//        }
//    }
//
//    final override fun setupInjector(kodein: DI) {
//        di = kodein
//    }
//
//    override fun setupComponentLink(vararg components: Component<*>) {
//        if (!::links.isInitialized) {
//            links = emptySet()
//        }
//        links += components
//    }
//
//    override suspend fun finalize(): Either<String, Unit> = either {
//        ensure(::communicators.isInitialized) { "The finalize method must be called after the initialize one" }
//        if (::unitManagerJob.isInitialized) {
//            unitManagerJob.cancel()
//        }
//        communicators.forEach { (_, communicator) -> communicator.finalize() }
//    }
//
//    final override suspend fun <P : Any> send(
//        toComponent: ComponentType<P>,
//        message: P,
//        serializer: KSerializer<P>,
//    ): Either<String, Unit> = either {
//        isDependencyInjectionInitialized().bind()
//        ensure(::communicators.isInitialized) { "The send method must be called after the initialize one" }
//        val communicator = communicators.getCommunicator(toComponent).bind()
//        communicator.sendToComponent(Json.encodeToString(serializer, message).encodeToByteArray())
//    }
//
//    final override suspend fun <P : Any> receive(
//        fromComponent: ComponentType<P>,
//        serializer: KSerializer<P>,
//    ): Either<String, Flow<P>> = either {
//        isDependencyInjectionInitialized().bind()
//        ensure(::communicators.isInitialized) { "The receive method must be called after the initialize one" }
//        val communicator = communicators.getCommunicator(fromComponent).bind()
//        val flow = communicator.receiveFromComponent().bind()
//        return flow.map { Json.decodeFromString(serializer, it.decodeToString()) }.right()
//    }
//
//    private fun isDependencyInjectionInitialized(): Either<String, Unit> = either {
//        ensure(::di.isInitialized) { "Before start using, the `setupInjector` must be called" }
//    }
//
//    private fun <P : Any> Map<Component<*>, Communicator>.getCommunicator(
//        component: ComponentType<P>,
//    ): Either<String, Communicator> =
//        filterKeys { it.type == component }
//            .values
//            .firstOrNone()
//            .toEither { "Communicator not available for component $component" }
//
//    companion object {
//        suspend inline fun <reified C : Any> Component<C>.send(
//            toComponent: ComponentType<C>,
//            message: C,
//        ): Either<String, Unit> = send(toComponent, message, serializer())
//
//        suspend inline fun <reified C : Any> Component<C>.receive(
//            fromComponent: ComponentType<C>,
//        ): Either<String, Flow<C>> = receive(fromComponent, serializer())
//    }
// }
