package it.unibo.pulvreakt.runtime

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import io.github.oshai.kotlinlogging.KotlinLogging
import it.unibo.pulvreakt.api.communication.Channel
import it.unibo.pulvreakt.api.communication.ChannelImpl
import it.unibo.pulvreakt.api.communication.LocalChannelManager
import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.api.context.IntId.Companion.toId
import it.unibo.pulvreakt.api.infrastructure.Host
import it.unibo.pulvreakt.api.communication.protocol.Protocol
import it.unibo.pulvreakt.api.reconfiguration.Reconfigurator
import it.unibo.pulvreakt.api.reconfiguration.component.ComponentModeReconfigurator
import it.unibo.pulvreakt.dsl.model.PulvreaktConfiguration
import it.unibo.pulvreakt.runtime.component.ComponentManager
import it.unibo.pulvreakt.runtime.component.SimpleComponentManager
import it.unibo.pulvreakt.runtime.errors.DeviceConfigurationNotFound
import it.unibo.pulvreakt.runtime.errors.RuntimeError
import it.unibo.pulvreakt.runtime.errors.UnitManagerNotInitialized
import it.unibo.pulvreakt.runtime.errors.UnitReconfiguratorNotInitialized
import it.unibo.pulvreakt.runtime.errors.WrapProtocolError
import it.unibo.pulvreakt.runtime.errors.WrapUnitManagerError
import it.unibo.pulvreakt.runtime.errors.WrapUnitReconfiguratorError
import it.unibo.pulvreakt.runtime.reconfigurator.UnitReconfigurator
import it.unibo.pulvreakt.runtime.unit.UnitManager
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider
import org.kodein.di.singleton

internal class PulvreaktRuntimeImpl(
    private val config: PulvreaktConfiguration,
    private val device: String,
    private val id: Int,
    private val host: Host,
) : PulvreaktRuntime {

    private lateinit var unitManager: UnitManager
    private lateinit var unitReconfigurator: UnitReconfigurator
    private val logger = KotlinLogging.logger("PulvreaktRuntime")

    override suspend fun start(): Either<RuntimeError, Unit> = either {
        logger.debug { "Start Pulvreakt runtime" }
        ensure(::unitManager.isInitialized) { UnitManagerNotInitialized }
        ensure(::unitReconfigurator.isInitialized) { UnitReconfiguratorNotInitialized }
        unitManager.start().mapLeft { WrapUnitManagerError(it) }.bind()
        // unitReconfigurator.start()
    }

    override suspend fun stop(): Either<RuntimeError, Unit> = either {
        logger.debug { "Stop Pulvreakt runtime" }
        ensure(::unitManager.isInitialized) { UnitManagerNotInitialized }
        ensure(::unitReconfigurator.isInitialized) { UnitReconfiguratorNotInitialized }
        unitManager.stop().mapLeft { WrapUnitManagerError(it) }.bind()
        // unitReconfigurator.stop()
    }

    override suspend fun initialize(): Either<RuntimeError, Unit> = either {
        logger.info { "Initializing PulvreaktRuntime" }
        logger.debug { "Configuration for device $device - id $id - host $host" }

        val deviceConfiguration = config[device]
        ensureNotNull(deviceConfiguration) { DeviceConfigurationNotFound(device) }

        logger.debug { "Setup dependency injection module" }

        // Setup DI
        val diModule = DI {
            bind<Protocol> { provider { config.protocol } }
            bind<Channel> { provider { ChannelImpl() } }
            bind<LocalChannelManager> { singleton { LocalChannelManager() } }
            bind<Reconfigurator> { singleton { Reconfigurator() } }
            bind<ComponentModeReconfigurator> { singleton { ComponentModeReconfigurator() } }
            bind<ComponentManager> { singleton { SimpleComponentManager() } }
            bind<Context> { provider { Context(id.toId(), host) } }
        }

        with(config.protocol) {
            setupInjector(diModule)
            initialize().mapLeft { WrapProtocolError(it) }.bind()
        }

        logger.debug { "Create and setup UnitManager" }

        unitManager = UnitManager(deviceConfiguration)
        with(unitManager) {
            setupInjector(diModule)
            initialize().mapLeft { WrapUnitManagerError(it) }.bind()
        }

        unitReconfigurator = UnitReconfigurator()
        with(unitReconfigurator) {
            setupInjector(diModule)
            initialize().mapLeft { err -> WrapUnitReconfiguratorError(err) }.bind()
        }

        logger.debug { "PulvreaktRuntime initialized" }
    }

    override suspend fun finalize(): Either<RuntimeError, Unit> = either {
        logger.info { "Finalizing PulvreaktRuntime" }
        ensure(::unitManager.isInitialized) { UnitManagerNotInitialized }
        unitManager.finalize().mapLeft { WrapUnitManagerError(it) }.bind()
    }
}
