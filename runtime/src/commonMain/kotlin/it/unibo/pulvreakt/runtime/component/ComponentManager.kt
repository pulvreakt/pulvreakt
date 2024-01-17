package it.unibo.pulvreakt.runtime.component

import arrow.core.Either
import it.unibo.pulvreakt.api.component.Component
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.api.initializable.ManagedResource
import it.unibo.pulvreakt.errors.component.ComponentError
import it.unibo.pulvreakt.runtime.component.errors.ComponentManagerError
import kotlinx.coroutines.Deferred

internal interface ComponentManager : ManagedResource<Nothing> {
    fun register(component: Component<*>)

    suspend fun start(component: ComponentRef): Either<ComponentManagerError, Deferred<Either<ComponentError, Unit>>>

    suspend fun stop(component: ComponentRef): Either<ComponentManagerError, Unit>

    suspend fun stopAll(): Either<ComponentManagerError, Unit>

    fun alive(): Set<ComponentRef>
}
