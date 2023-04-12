package it.nicolasfarabegoli.pulverization.runtime

import it.nicolasfarabegoli.pulverization.core.Initializable
import it.nicolasfarabegoli.pulverization.runtime.context.ExecutionContext
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.DeploymentUnitRuntimeConfiguration
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.reconfigurationRules
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.UnitReconfigurator
import it.nicolasfarabegoli.pulverization.runtime.utils.Spawner
import it.nicolasfarabegoli.pulverization.runtime.utils.createComponentsRefs
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Pulverization runtime class.
 */
class PulverizationRuntime<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    private val runtimeConfig: DeploymentUnitRuntimeConfiguration<S, C, SS, AS, O>,
    stateSer: KSerializer<S>,
    commSer: KSerializer<C>,
    sensorsSer: KSerializer<SS>,
    actuatorsSer: KSerializer<AS>,
) : Initializable, KoinComponent {

    private val executionContext: ExecutionContext by inject()
    private val componentsRef =
        runtimeConfig.createComponentsRefs(emptyMap(), stateSer, commSer, sensorsSer, actuatorsSer)
    private val spawner = Spawner(runtimeConfig.runtimeConfiguration.componentsRuntimeConfiguration, componentsRef)
    private val unitReconfigurator = UnitReconfigurator(
        runtimeConfig.runtimeConfiguration.reconfiguratorProvider(),
        runtimeConfig.reconfigurationRules(),
        componentsRef,
        spawner,
        runtimeConfig.getStartupComponents(executionContext.host),
    )

    override fun getKoin(): Koin = PulverizationKoinModule.koinApp?.koin ?: error("Koin module not initialized")

    override suspend fun initialize() {
        runtimeConfig.getStartupComponents(executionContext.host).forEach { spawner.spawn(it) }
        unitReconfigurator.initialize()
    }

    override suspend fun finalize() {
        unitReconfigurator.finalize()
    }

    suspend fun start() {
        initialize()
        TODO("Spawn local fiber with components logics")
    }

    suspend fun stop() {
        finalize()
    }

    companion object {
        /**
         * Static method for creating [PulverizationRuntime] without specifying all serializer.
         */
        inline operator fun <reified S : Any, reified C : Any, reified SS : Any, reified AS : Any, O : Any> invoke(
            runtimeConfiguration: DeploymentUnitRuntimeConfiguration<S, C, SS, AS, O>,
        ): PulverizationRuntime<S, C, SS, AS, O> = PulverizationRuntime(
            runtimeConfiguration,
            serializer(),
            serializer(),
            serializer(),
            serializer(),
        )
    }
}
