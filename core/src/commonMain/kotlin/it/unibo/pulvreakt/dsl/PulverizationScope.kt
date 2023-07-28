package it.unibo.pulvreakt.dsl

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptySet
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import arrow.core.raise.zipOrAccumulate
import arrow.core.toNonEmptySetOrNull
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.core.protocol.Protocol
import it.unibo.pulvreakt.dsl.deployment.DeploymentSpecificationScope
import it.unibo.pulvreakt.dsl.errors.ConfigurationError
import it.unibo.pulvreakt.dsl.errors.ConfigurationValidator
import it.unibo.pulvreakt.dsl.errors.EmptyDeploymentConfiguration
import it.unibo.pulvreakt.dsl.errors.EmptySystemConfiguration
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError
import it.unibo.pulvreakt.dsl.model.ConfiguredDevicesRuntimeConfiguration
import it.unibo.pulvreakt.dsl.model.DeviceSpecification
import it.unibo.pulvreakt.dsl.model.DeviceStructure
import it.unibo.pulvreakt.dsl.model.PulvreaktConfiguration
import it.unibo.pulvreakt.dsl.system.CanonicalDeviceScope
import it.unibo.pulvreakt.dsl.system.ExtendedDeviceScope
import kotlin.jvm.JvmInline
import kotlin.properties.ReadOnlyProperty

/**
 * Represents the type specification of a logic device.
 * It does not contain any information about the device itself, but is only used to identify the device configuration.
 * [name] is the name of the device retrieved from the property name.
 * Do not use this class directly.
 */
@JvmInline
value class LogicDeviceType(val name: String)

/**
 * Configuration DSL scope for configuring the pulverization.
 */
class PulverizationScope {
    private val systemConfigSpec = mutableSetOf<Either<Nel<SystemConfigurationError>, DeviceStructure>>()
    private var deploymentConfigSpec: Either<Nel<ConfigurationError>, ConfiguredDevicesRuntimeConfiguration>? = null
    private lateinit var protocol: Protocol
    private var infrastructure: NonEmptySet<Host>? = null

    /**
     * Configure a logic device.
     */
    fun <St : Any, Co : Any, Se : Any, Ac : Any> logicDevice(
        config: CanonicalDeviceScope<St, Co, Se, Ac>.() -> Unit,
    ): ReadOnlyProperty<Any?, LogicDeviceType> = ReadOnlyProperty { _, property ->
        val deviceScope = CanonicalDeviceScope<St, Co, Se, Ac>(property.name).apply(config)
        systemConfigSpec.add(deviceScope.generate())
        LogicDeviceType(property.name)
    }

    /**
     * Configure an extended logic device.
     */
    fun extendedLogicDevice(config: ExtendedDeviceScope.() -> Unit): ReadOnlyProperty<Any?, LogicDeviceType> = ReadOnlyProperty { _, property ->
        val deviceScope = ExtendedDeviceScope(property.name).apply(config)
        systemConfigSpec.add(deviceScope.generate())
        LogicDeviceType(property.name)
    }

    /**
     * DSL scope for configuring the pulverization deployment.
     */
    fun deployment(
        infrastructure: NonEmptySet<Host>,
        protocol: Protocol,
        deploymentConfig: DeploymentSpecificationScope.() -> Unit,
    ) {
        this.protocol = protocol
        this.infrastructure = infrastructure
        val result = either {
            val deploymentScope = DeploymentSpecificationScope().apply(deploymentConfig)
            deploymentScope.generate().bind()
        }
        deploymentConfigSpec = result
    }

    internal fun generate(): Either<Nel<ConfigurationError>, PulvreaktConfiguration> = either {
        zipOrAccumulate(
            { ensure(systemConfigSpec.isNotEmpty()) { EmptySystemConfiguration } },
            { ensureNotNull(deploymentConfigSpec) { EmptyDeploymentConfiguration } },
        ) { _, _ -> }
        val systemDevicesSpec = either { systemConfigSpec.map { it.bind() } }
            .map { it.toNonEmptySetOrNull()!! } // Safe since first check

        val (systemSpec, deploymentSpec) = zipOrAccumulate(
            { systemDevicesSpec.bind() },
            // `!!` is necessary since the property is a var and the smart cast can't be performed
            { deploymentConfigSpec!!.bind() },
        ) { systemSpec, deploymentConfig -> systemSpec to deploymentConfig }

        ConfigurationValidator(systemSpec, deploymentSpec, infrastructure!!).validate().bind()

        val devicesConfig = systemSpec
            .map { systemConfig ->
                // The use of `first` is safe since the check about the presence of the config is performed by the validator
                systemConfig to deploymentSpec.first { deploymentConfig -> deploymentConfig.deviceName == systemConfig.deviceName }
            }
            .map { (systemConfig, deploymentConfig) ->
                systemConfig.deviceName to DeviceSpecification(
                    systemConfig.deviceName,
                    systemConfig.componentsGraph,
                    systemConfig.requiredCapabilities,
                    deploymentConfig,
                )
            }.toMap()

        PulvreaktConfiguration(devicesConfig, protocol)
    }
}
