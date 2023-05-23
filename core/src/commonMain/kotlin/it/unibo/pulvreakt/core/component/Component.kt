package it.unibo.pulvreakt.core.component

import arrow.core.Either
import arrow.core.NonEmptySet
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass

interface Component<T : Any> : Initializable {
    val name: String
    val links: NonEmptySet<Component<*>>

    suspend fun <P : Any, C : Component<P>> send(
        componentKClass: KClass<C>,
        message: P,
        serializer: KSerializer<P>,
    ): Either<String, Unit>

    suspend fun <P : Any, C : Component<P>> receive(
        componentKClass: KClass<C>,
        serializer: KSerializer<P>,
    ): Either<String, Flow<P>>

    suspend fun execute(): Either<String, Unit>
}
