package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.NonEmptyList
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
): Either<NonEmptyList<String>, DeploymentSpecification> =
    DeploymentDslScope(deviceName, availableHosts, systemSpecification).apply(config).generate()
