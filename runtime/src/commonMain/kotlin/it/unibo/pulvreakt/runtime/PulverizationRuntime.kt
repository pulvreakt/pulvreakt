package it.unibo.pulvreakt.runtime

import it.unibo.pulvreakt.component.Context
import it.unibo.pulvreakt.core.Initializable
import it.unibo.pulvreakt.runtime.communication.CommManager
import it.unibo.pulvreakt.runtime.communication.Communicator
import it.unibo.pulvreakt.runtime.context.ExecutionContext
import it.unibo.pulvreakt.runtime.dsl.model.DeploymentUnitRuntimeConfiguration
import it.unibo.pulvreakt.runtime.dsl.model.reconfigurationRules
import it.unibo.pulvreakt.runtime.reconfiguration.UnitReconfigurator
import it.unibo.pulvreakt.runtime.spawner.SpawnerManager
import it.unibo.pulvreakt.runtime.utils.createComponentsRefs
import it.unibo.pulvreakt.runtime.utils.setupOperationMode
import it.unibo.pulvreakt.runtime.utils.setupRefs
import it.unibo.pulvreakt.utils.PulverizationKoinModule
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import org.koin.dsl.koinApplication
import org.koin.dsl.module

/**
 * Pulverization runtime class.
 */
@Suppress("LongParameterList")
class PulverizationRuntime<S : Any, C : Any, SS : Any, AS : Any, O : Any> (
    deviceId: String,
    host: String,
    private val runtimeConfig: DeploymentUnitRuntimeConfiguration<S, C, SS, AS, O>,
    stateSer: KSerializer<S>,
    commSer: KSerializer<C>,
    sensorsSer: KSerializer<SS>,
    actuatorsSer: KSerializer<AS>,
) : Initializable {

    private val executionContext =
        ExecutionContext.create(deviceId, runtimeConfig.getHost(host) ?: error("No host `$host` found"))
    private val componentsRef =
        runtimeConfig.createComponentsRefs(stateSer, commSer, sensorsSer, actuatorsSer)
    private val spawner =
        SpawnerManager(runtimeConfig.runtimeConfiguration.componentsRuntimeConfiguration, componentsRef)
    private val unitReconfigurator = UnitReconfigurator(
        runtimeConfig.runtimeConfiguration.reconfiguratorProvider(),
        runtimeConfig.reconfigurationRules(),
        componentsRef,
        spawner,
        runtimeConfig.startupComponent(executionContext.host),
    )

    override suspend fun initialize() {
        val koinModule = module {
            single<ExecutionContext> { executionContext }
            single<Context> { executionContext }
            single { CommManager() }
            single { runtimeConfig.runtimeConfiguration.remotePlaceProvider }
            factory<Communicator> { runtimeConfig.runtimeConfiguration.communicatorProvider() }
        }
        PulverizationKoinModule.koinApp = koinApplication {
            // logger(KermitKoinLogger(Logger.withTag("koin")))
            modules(koinModule)
        }
        // Initialize operation mode based on initial deployment
        componentsRef.setupRefs()
        componentsRef.setupOperationMode(runtimeConfig.hostComponentsStartupMap(), executionContext.host)
    }

    override suspend fun finalize() {
        unitReconfigurator.finalize()
    }

    /**
     * Start the platform.
     */
    suspend fun start() {
        initialize()
        runtimeConfig.startupComponent(executionContext.host).forEach { spawner.spawn(it) }
        unitReconfigurator.initialize()
    }

    /**
     * Stop the platform.
     */
    suspend fun stop() {
        finalize()
        spawner.killAll()
    }

    companion object {
        /**
         * Static method for creating [PulverizationRuntime] without specifying all serializer.
         */
        inline operator fun <reified S : Any, reified C : Any, reified SS : Any, reified AS : Any, O : Any> invoke(
            deviceId: String,
            host: String,
            runtimeConfiguration: DeploymentUnitRuntimeConfiguration<S, C, SS, AS, O>,
        ): it.unibo.pulvreakt.runtime.PulverizationRuntime<S, C, SS, AS, O> =
            it.unibo.pulvreakt.runtime.PulverizationRuntime(
                deviceId,
                host,
                runtimeConfiguration,
                serializer(),
                serializer(),
                serializer(),
                serializer(),
            )
    }
}
