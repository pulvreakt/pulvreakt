package it.unibo.pulvreakt.core.component

import arrow.core.Either
import arrow.core.firstOrNone
import arrow.core.raise.either
import arrow.core.right
import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.context.Context
import it.unibo.pulvreakt.core.unit.UnitManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import kotlin.reflect.KClass

abstract class AbstractComponent<T : Any> : Component<T>, KoinComponent {
    protected val context by inject<Context>()
    private val unitManager by inject<UnitManager>()
    private lateinit var communicators: Map<Component<*>, Communicator>
    private lateinit var unitManagerJob: Job

    override suspend fun initialize(): Either<String, Unit> = coroutineScope {
        communicators = links.map { this@AbstractComponent to it }
            .toMap()
            .mapValues { (source, destination) -> get<Communicator>().apply { communicatorSetup(source, destination) } }
        unitManagerJob = launch {
            unitManager.configurationUpdates().collect {
                TODO("Implement reconfiguration logic mode")
            }
        }
        Unit.right()
    }

    override suspend fun finalize(): Either<String, Unit> {
        if (::unitManagerJob.isInitialized) { unitManagerJob.cancel() }
        communicators.forEach { (_, communicator) -> communicator.finalize() }
        return Unit.right()
    }

    final override suspend fun <P : Any, C : Component<P>> send(
        componentKClass: KClass<C>,
        message: P,
        serializer: KSerializer<P>,
    ): Either<String, Unit> = either {
        val communicator = communicators.getCommunicator(componentKClass).bind()
        communicator.sendToComponent(Json.encodeToString(serializer, message).encodeToByteArray())
    }

    final override suspend fun <P : Any, C : Component<P>> receive(
        componentKClass: KClass<C>,
        serializer: KSerializer<P>,
    ): Either<String, Flow<P>> = either {
        val communicator = communicators.getCommunicator(componentKClass).bind()
        val flow = communicator.receiveFromComponent().bind()
        return flow.map { Json.decodeFromString(serializer, it.decodeToString()) }.right()
    }

    private fun <C : Component<*>> Map<Component<*>, Communicator>.getCommunicator(
        component: KClass<C>,
    ): Either<String, Communicator> =
        filterKeys { component.isInstance(it) }
            .values
            .firstOrNone()
            .toEither { "Communicator not available for component ${component.simpleName}" }

    companion object {
        suspend inline fun <reified P : Any, reified C : Component<P>> AbstractComponent<*>.send(
            message: P,
        ): Either<String, Unit> = send(C::class, message, serializer())

        suspend inline fun <reified P : Any, reified C : Component<P>> AbstractComponent<*>.receive(): Either<String, Flow<P>> =
            receive(C::class, serializer())
    }
}