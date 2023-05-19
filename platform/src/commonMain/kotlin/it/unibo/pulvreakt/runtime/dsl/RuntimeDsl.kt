package it.unibo.pulvreakt.runtime.dsl

import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.dsl.model.SystemSpecification
import it.unibo.pulvreakt.runtime.dsl.model.DeploymentUnitRuntimeConfiguration
import it.unibo.pulvreakt.runtime.dsl.model.Host
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
