package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import arrow.core.raise.zipOrAccumulate
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.deployment.model.DeploymentSpecification
import it.unibo.pulvreakt.dsl.system.model.SystemSpecification

/**
 * Configures the deployment of a given [deviceName].
 */
fun pulverizationRuntime(
    deviceName: String,
    availableHosts: Set<Host>,
    systemSpecification: SystemSpecification,
    config: DeploymentDslScope.() -> Unit,
): Either<NonEmptyList<String>, DeploymentSpecification> = either {
    val deviceConfiguration = systemSpecification.logicalDevices.firstOrNull { it.deviceName == deviceName }
    zipOrAccumulate(
        { ensureNotNull(deviceConfiguration) { "No device with name '$deviceName' found in the system configuration" } },
        { DeploymentDslScope(availableHosts, deviceConfiguration!!).apply(config).generate().bind() },
    ) { _, deploymentSpecification -> deploymentSpecification }
}
