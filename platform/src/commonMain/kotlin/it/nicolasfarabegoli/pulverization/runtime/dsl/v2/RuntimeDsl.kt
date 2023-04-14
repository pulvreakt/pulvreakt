package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Capability
import it.nicolasfarabegoli.pulverization.dsl.v2.model.SystemSpecification
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.DeploymentUnitRuntimeConfiguration
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host
import kotlinx.coroutines.coroutineScope

/**
 * Runtime DSL which takes a [configuration], a [deviceName] and the [hostCapabilityMapping].
 * Generates the configuration needed by the runtime to work.
 */
suspend fun <S : Any, C : Any, SS : Any, AS : Any, O : Any> pulverizationRuntime(
    configuration: SystemSpecification,
    deviceName: String,
    availableHosts: Set<Host>,
    config: suspend PulverizationRuntimeScope<S, C, SS, AS, O>.() -> Unit,
): DeploymentUnitRuntimeConfiguration<S, C, SS, AS, O> = coroutineScope {
    val runtimeConfig = PulverizationRuntimeScope<S, C, SS, AS, O>().apply { config() }.generate()
    val deviceSpecification = configuration.getConfigurationByDevice(deviceName)
    DeploymentUnitRuntimeConfiguration(
        deviceSpecification,
        runtimeConfig,
        availableHosts.toHostCapabilityMapping(),
        availableHosts,
    )
}

internal fun Set<Host>.toHostCapabilityMapping(): Map<Capability, Set<Host>> {
    return flatMap { it.capabilities }
        .toSet()
        .associateWith { capability ->
            this.filter { it.capabilities.contains(capability) }.toSet()
        }
}
