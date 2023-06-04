package it.unibo.pulvreakt.runtime.unit.component

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right
import it.unibo.pulvreakt.core.component.Component
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import mu.KotlinLogging

internal class SimpleComponentManager : ComponentManager {
    private var componentContainer = emptyMap<Component<*>, Job?>()
    private val logger = KotlinLogging.logger("SimpleComponentManager")

    override fun register(component: Component<*>) { componentContainer += component to null }

    override suspend fun start(component: Component<*>): Either<String, Unit> = coroutineScope {
        either {
            val isComponentRegistered = componentContainer.containsKey(component)
            ensure(isComponentRegistered) { "The component ${component.name} is not registered. Unable to execute it." }
            logger.debug { "Starting the execution of ${component.name}" }
            val jobRef = launch { component.execute() }
            componentContainer += component to jobRef
        }
    }

    override fun stop(component: Component<*>): Either<String, Unit> = either {
        ensure(componentContainer.containsKey(component)) {
            "The component ${component.name} is not registered. Unable to stop it."
        }
        logger.debug { "Stop the execution of ${component.name}" }
        componentContainer[component]?.cancel("Deployment unit reconfigured. No more need for ${component.name}")
            ?: run {
                logger.warn { "Tried to cancel a non-running component" }
                logger.debug { "Tried to cancel the ${component.name} but it was not running" }
            }
        componentContainer += component to null
    }

    override fun alive(): Set<Component<*>> = componentContainer.filterValues { it != null }.keys
    override suspend fun initialize(): Either<String, Unit> = Unit.right()
    override suspend fun finalize(): Either<String, Unit> = Unit.right()
}