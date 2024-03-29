package it.unibo.pulvreakt.runtime.unit

import arrow.core.Either
import arrow.core.raise.either
import arrow.fx.coroutines.parMap
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.pulvreakt.api.communication.Mode
import it.unibo.pulvreakt.dsl.model.DeviceSpecification
import it.unibo.pulvreakt.runtime.unit.errors.UnitManagerError
import it.unibo.pulvreakt.runtime.unit.errors.WrapComponentError
import it.unibo.pulvreakt.runtime.unit.errors.WrapComponentManagerError

internal class UnitManagerImpl(
    private val deviceSpecification: DeviceSpecification,
) : AbstractUnitManager() {

    private val host by lazy { context.host }
    private val logger = KotlinLogging.logger("UnitManagerImpl")

    override suspend fun start(): Either<UnitManagerError, Unit> = either {
        val localComponents = deviceSpecification.runtimeConfiguration.componentStartupHost.filter { (_, h) -> h == host }.keys
        // Start the components
        // TODO: Use the defer value returned by the start method and check errors
        localComponents.parMap {
            componentManager.start(it.getRef()).mapLeft { err -> WrapComponentManagerError(err) }.bind()
        }
    }

    override suspend fun stop(): Either<UnitManagerError, Unit> = either {
        componentManager.stopAll().mapLeft { err -> WrapComponentManagerError(err) }.bind()
    }

    override suspend fun initialize(): Either<UnitManagerError, Unit> = either {
        // Create binding between components according to the device specification
        componentManager.initialize().bind()
        val localComponents = deviceSpecification.runtimeConfiguration.componentStartupHost.filter { (_, h) -> h == host }.keys
        val remoteComponents = deviceSpecification.runtimeConfiguration.componentStartupHost.filter { (_, h) -> h != host }.keys
        localComponents.forEach { comp ->
            val componentRef = comp.getRef()
            // Retrieve the component references from the device specification
            logger.debug { "ComponentRef: $componentRef" }
            logger.debug { "Components: ${deviceSpecification.componentsConfiguration}" }
            deviceSpecification.componentsConfiguration[componentRef]?.let { compRefs ->
                comp.setupWiring(*compRefs.toTypedArray())
                comp.setupInjector(di)
                comp.initialize().mapLeft { err -> WrapComponentError(err) }.bind()
                // Add the component to the component manager
                componentManager.register(comp)
                logger.debug { "Setup completed for component [$comp]" }
            } ?: error("Unable to wire $comp. This can be a bug, please fill and issue.")
        }

        // Set up the communicators in the component according to the initial startup
        remoteComponents.forEach { componentModeReconfigurator.setMode(it.getRef(), Mode.Remote) }
        logger.debug { "Unit manager setup concluded" }
    }

    override suspend fun finalize(): Either<UnitManagerError, Unit> = either {
        componentManager.finalize().bind()
    }
}
