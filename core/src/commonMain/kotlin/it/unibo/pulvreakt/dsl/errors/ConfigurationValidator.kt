package it.unibo.pulvreakt.dsl.errors

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptySet
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.toNonEmptyListOrNull
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.model.ConfiguredDeviceStructure
import it.unibo.pulvreakt.dsl.model.ConfiguredDevicesRuntimeConfiguration

internal class ConfigurationValidator(
    private val systemSpec: ConfiguredDeviceStructure,
    private val deploymentSpec: ConfiguredDevicesRuntimeConfiguration,
    private val infrastructure: NonEmptySet<Host>,
) {
    fun validate(): Either<Nel<ConfigurationError>, Unit> = either {
        uniqueDevicesNameOrRaise(systemSpec.map { it.deviceName }).bind()
        deploymentConfigurationOrRaise(systemSpec.map { it.deviceName }).bind()
        componentsRegisteredOrRaise().bind()
        validHostOrRaise().bind()
        validStartupHostOrRaise().bind()
    }

    private fun uniqueDevicesNameOrRaise(names: List<String>): Either<Nel<DuplicateDeviceName>, Unit> = either {
        val duplicateDeviceNames = names.groupBy { it }.filter { it.value.size > 1 }.keys
        ensure(duplicateDeviceNames.isEmpty()) { duplicateDeviceNames.map { DuplicateDeviceName(it) }.toNonEmptyListOrNull()!! }
    }

    private fun deploymentConfigurationOrRaise(names: List<String>): Either<Nel<NoDeviceFound>, Unit> = either {
        val missingDevices = names.filter { name -> name !in deploymentSpec.map { it.deviceName } }
        ensure(missingDevices.isEmpty()) { missingDevices.map { NoDeviceFound(it) }.toNonEmptyListOrNull()!! }
    }

    private fun componentsRegisteredOrRaise(): Either<Nel<ComponentNotRegistered>, Unit> = either {
        val missingComponents = systemSpec.map { (deviceName, graph, _) ->
            val allSystemSpecComponents = graph.keys + graph.values.flatten()
            val allDeploymentSpecComponents =
                deploymentSpec.first { it.deviceName == deviceName }.componentStartupHost.keys.map { it.getRef() }.toSet()
            deviceName to (allSystemSpecComponents - allDeploymentSpecComponents)
        }
            .filter { (_, missingComps) -> missingComps.isNotEmpty() }
            .flatMap { (deviceName, missingComps) -> missingComps.map { ComponentNotRegistered(deviceName, it) } }
        ensure(missingComponents.isEmpty()) { missingComponents.toNonEmptyListOrNull()!! }
    }

    private fun validHostOrRaise(): Either<Nel<UnknownHost>, Unit> = either {
        val unknownHosts = deploymentSpec
            .map { it.componentStartupHost.values }
            .flatten()
            .filter { it !in infrastructure }
        ensure(unknownHosts.isEmpty()) { unknownHosts.map { UnknownHost(it) }.toNonEmptyListOrNull()!! }
    }

    private fun validStartupHostOrRaise(): Either<Nel<InvalidStartupHost>, Unit> = either {
        val invalidStartupHosts = systemSpec.map { (deviceName, _, componentsCapabilities) ->
            val deviceStartupHosts = deploymentSpec
                .first { it.deviceName == deviceName }
                .componentStartupHost
                .mapKeys { it.key.getRef() }
            componentsCapabilities.map { (component, componentCapabilities) ->
                val host = deviceStartupHosts[component]
                val hostCapabilities = host?.capabilities
                hostCapabilities?.intersect(componentCapabilities)
                    ?.let { if (it.isEmpty()) InvalidStartupHost(component, host) else null }
            }.filterNotNull()
        }.flatten()
        ensure(invalidStartupHosts.isEmpty()) { invalidStartupHosts.toNonEmptyListOrNull()!! }
    }
}
