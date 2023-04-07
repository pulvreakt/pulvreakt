package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import it.nicolasfarabegoli.pulverization.dsl.v2.model.SystemSpecification
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.DeploymentUnitRuntimeConfiguration
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.HostCapabilityMapping
import kotlinx.coroutines.coroutineScope

/**
 * Runtime DSL which takes a [configuration], a [deviceName] and the [hostCapabilityMapping].
 * Generates the configuration needed by the runtime to work.
 */
suspend fun <S : Any, C : Any, SS : Any, AS : Any, O : Any> pulverizationRuntime(
    configuration: SystemSpecification,
    deviceName: String,
    hostCapabilityMapping: HostCapabilityMapping,
    config: suspend PulverizationRuntimeScope<S, C, SS, AS, O>.() -> Unit,
): DeploymentUnitRuntimeConfiguration<S, C, SS, AS, O> = coroutineScope {
    val runtimeConfig = PulverizationRuntimeScope<S, C, SS, AS, O>().apply { config() }.generate()
    val deviceSpecification = configuration.getConfigurationByDevice(deviceName)
    DeploymentUnitRuntimeConfiguration(deviceSpecification, runtimeConfig, hostCapabilityMapping)
}
