package it.unibo.pulvreakt.runtime.unit.component

import arrow.core.Either
import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.utils.Initializable

internal interface ComponentManager : Initializable {
    fun register(component: Component<*>)
    suspend fun start(component: Component<*>): Either<String, Unit>
    fun stop(component: Component<*>): Either<String, Unit>
    fun alive(): Set<Component<*>>
}
