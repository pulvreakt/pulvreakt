package it.unibo.pulvreakt.runtime.unit.component

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import arrow.core.right
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.errors.ComponentError
import it.unibo.pulvreakt.runtime.unit.component.errors.ComponentManagerError
import it.unibo.pulvreakt.runtime.unit.component.errors.ComponentNotRegistered
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope

internal class SimpleComponentManager : ComponentManager {
    private var componentContainer = emptyMap<Component, Deferred<Either<ComponentError, Unit>>?>()
    private val logger = KotlinLogging.logger("SimpleComponentManager")

    override fun register(component: Component) {
        componentContainer += component to null
    }

    override suspend fun start(component: ComponentRef): Either<ComponentManagerError, Deferred<Either<ComponentError, Unit>>> = coroutineScope {
        either {
            val candidateComponent = componentContainer.keys.firstOrNull { it.getRef() == component }
            ensureNotNull(candidateComponent) { ComponentNotRegistered(component) }
            logger.debug { "Starting the execution of ${component::class.simpleName}" }
            val jobRef = async { candidateComponent.execute() }
            componentContainer += candidateComponent to jobRef
            jobRef
        }
    }

    override fun stop(component: ComponentRef): Either<ComponentManagerError, Unit> = either {
        val candidateComponent = componentContainer.keys.firstOrNull { it.getRef() == component }
        ensureNotNull(candidateComponent) { ComponentNotRegistered(component) }
        logger.debug { "Stop the execution of ${component::class.simpleName}" }
        componentContainer[candidateComponent]
            ?.cancel("Deployment unit reconfigured. No more need for ${component::class.simpleName}")
            ?: run {
                logger.warn { "Tried to cancel a non-running component" }
                logger.debug { "Tried to cancel the ${component::class.simpleName} but it was not running" }
            }
        componentContainer += candidateComponent to null
    }

    override fun alive(): Set<ComponentRef> = componentContainer
        .filterValues { it != null }
        .keys.map { it.getRef() }
        .toSet()
    override suspend fun initialize(): Either<Nothing, Unit> = Unit.right()
    override suspend fun finalize(): Either<Nothing, Unit> = Unit.right()
}
