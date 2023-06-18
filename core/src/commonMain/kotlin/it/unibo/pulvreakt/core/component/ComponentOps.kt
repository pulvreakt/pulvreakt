package it.unibo.pulvreakt.core.component

import arrow.core.Either
import it.unibo.pulvreakt.core.utils.PulvreaktInjected
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer
import org.kodein.di.DI
import kotlin.reflect.KClass

interface ComponentOps<T : Any> : Initializable, PulvreaktInjected {
    fun setupWiring(vararg components: ComponentRef<*>)

    fun getRef(): ComponentRef<T> = ComponentRef.create(this)

    suspend fun Component.send(
        toComponent: ComponentRef<*>,
        message: T,
        serializer: KSerializer<T>,
    ): Either<String, Unit>

    suspend fun <P : Any> Component.receive(
        fromComponent: ComponentRef<P>,
        serializer: KSerializer<P>,
    ): Either<String, Flow<P>>

    fun <C : ComponentOps<*>> get(component: KClass<C>): C

    companion object {
        fun <T : Any> create(): ComponentOps<T> = ComponentOpsImpl()
        inline fun <reified C : ComponentOps<*>> ComponentOps<*>.get(): C = get(C::class)
    }
}

internal class ComponentOpsImpl<T : Any> : ComponentOps<T> {
    override fun setupWiring(vararg components: ComponentRef<*>) {
        TODO("Not yet implemented")
    }

    override suspend fun <P : Any> Component.receive(fromComponent: ComponentRef<P>, serializer: KSerializer<P>): Either<String, Flow<P>> {
        TODO("Not yet implemented")
    }

    override fun <C : ComponentOps<*>> get(component: KClass<C>): C {
        TODO("Not yet implemented")
    }

    override suspend fun initialize(): Either<String, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun finalize(): Either<String, Unit> {
        TODO("Not yet implemented")
    }

    override fun setupInjector(kodein: DI) {
        TODO("Not yet implemented")
    }

    override suspend fun Component.send(toComponent: ComponentRef<*>, message: T, serializer: KSerializer<T>): Either<String, Unit> {
        TODO("Not yet implemented")
    }

    override val di: DI
        get() = TODO("Not yet implemented")
}
