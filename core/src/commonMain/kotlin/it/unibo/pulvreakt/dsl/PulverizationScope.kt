package it.unibo.pulvreakt.dsl

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptyList
import arrow.core.NonEmptySet
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.zipOrAccumulate
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.deployment.DeploymentSpecificationScope
import it.unibo.pulvreakt.dsl.errors.ConfigurationError
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.EmptyDeploymentConfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.EmptySystemConfiguration
import it.unibo.pulvreakt.dsl.model.CommunicatorProvider
import it.unibo.pulvreakt.dsl.model.ConfiguredDeviceStructure
import it.unibo.pulvreakt.dsl.model.ConfiguredDevicesRuntimeConfiguration
import it.unibo.pulvreakt.dsl.model.DeviceSpecification
import it.unibo.pulvreakt.dsl.model.PulvreaktConfiguration
import it.unibo.pulvreakt.dsl.model.ReconfiguratorProvider
import it.unibo.pulvreakt.dsl.system.SystemSpecificationScope

class PulverizationScope {
    private lateinit var systemConfigurations: Either<Nel<SystemConfigurationError>, ConfiguredDeviceStructure>
    private lateinit var deploymentConfigurations:
        Either<Nel<ConfigurationError>, ConfiguredDevicesRuntimeConfiguration>
    private lateinit var commProvider: CommunicatorProvider
    private lateinit var reconfigProvider: ReconfiguratorProvider

    fun system(systemConfig: SystemSpecificationScope.() -> Unit) {
        systemConfigurations = SystemSpecificationScope().apply(systemConfig).generate()
    }

    fun deployment(
        infrastructure: NonEmptySet<Host>,
        communicatorProvider: CommunicatorProvider,
        reconfiguratorProvider: ReconfiguratorProvider,
        deploymentConfig: DeploymentSpecificationScope.() -> Unit,
    ) {
        commProvider = communicatorProvider
        reconfigProvider = reconfiguratorProvider
        val result = either {
            val systemConf = systemConfigurations.bind()
            val deploymentScope = DeploymentSpecificationScope(systemConf, infrastructure).apply(deploymentConfig)
            deploymentScope.generate().bind()
        }
        deploymentConfigurations = result
    }

    private fun getConfiguration(): Either<Nel<ConfigurationError>, Pair<ConfiguredDeviceStructure, ConfiguredDevicesRuntimeConfiguration>> = either {
        zipOrAccumulate(
            { systemConfigurations.bind() },
            { deploymentConfigurations.bind() },
        ) { sConf, dConf -> sConf to dConf }
    }

    internal fun generate(): Either<NonEmptyList<ConfigurationError>, PulvreaktConfiguration> = either {
        val (sConf, dConf) = zipOrAccumulate(
            { ensure(::systemConfigurations.isInitialized) { EmptySystemConfiguration } },
            { ensure(::deploymentConfigurations.isInitialized) { EmptyDeploymentConfiguration } },
            { getConfiguration().bindNel() },
        ) { _, _, conf -> conf }
        zipOrAccumulate(
            { ensure(sConf.size == dConf.size) { TODO() } },
            { ensure(sConf.map { it.deviceName } == dConf.map { it.deviceName }) { TODO() } },
        ) { _, _ ->
            val deviceConfigs = sConf.map { (deviceName, deviceSpec, capabilities) ->
                val deviceRuntimeConfiguration = dConf.find { it.deviceName == deviceName }!!
                deviceName to DeviceSpecification(deviceName, deviceSpec, capabilities, deviceRuntimeConfiguration)
            }.toMap()
            PulvreaktConfiguration(deviceConfigs, commProvider, reconfigProvider)
        }
    }
}
