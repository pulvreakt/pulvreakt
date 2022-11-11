package it.nicolasfarabegoli.pulverization.dsl

import it.nicolasfarabegoli.pulverization.core.LogicalDevice
import it.nicolasfarabegoli.pulverization.core.PulverizedComponent
import kotlin.reflect.KClass

/**
 * Annotation class used for scoping correctly the DSL.
 */
@DslMarker
annotation class PulverizationMarker

/**
 * Scope for building DSL.
 */
interface Scope<S> {
    /**
     * Build the object configured by the DSL.
     */
    fun build(): S
}

/**
 * Scope for configuring a specific [LogicalDevice].
 */
@PulverizationMarker
class LogicalDeviceScope : Scope<Set<DeploymentUnit>> {
    private val deploymentUnits: MutableSet<DeploymentUnit> = mutableSetOf()

    /**
     * Retrive the component type.
     */
    inline fun <reified C : PulverizedComponent> component(): KClass<out C> = C::class

    /**
     * Define where the deployment unit should be deployed.
     */
    infix fun <C : PulverizedComponent> Set<KClass<out C>>.deployableOn(tier: Tier) {
        deploymentUnits += DeploymentUnit(this, tier)
    }

    /**
     * Define where the deployment unit should be deployed.
     */
    infix fun <C : PulverizedComponent> KClass<out C>.deployableOn(tier: Tier) {
        deploymentUnits += DeploymentUnit(setOf(this), tier)
    }

    /**
     * Set the current component in the same deployment unit with [other] component.
     */
    infix fun <C : PulverizedComponent> KClass<out C>.and(other: KClass<out C>): Set<KClass<out C>> {
        if (this == other) error("The same component should not appear in the same deployment unit")
        return setOf(this, other)
    }

    /**
     * Set the current components in the same deployment unit with [other] component.
     */
    infix fun <C : PulverizedComponent> Set<KClass<out C>>.and(other: KClass<out C>): Set<KClass<out C>> {
        if (this.contains(other)) error("The same component should not appear in the same deployment unit")
        return this + other
    }

    /**
     * Creates a new [LogicalDevice].
     */
    override fun build(): Set<DeploymentUnit> {
        val allComponents = deploymentUnits.flatMap { it.deployableComponents }
        if (allComponents.distinct().size != allComponents.size) {
            error("A component appear in more than one deployment unit. Check your configuration!")
        }
        return deploymentUnits
    }
}

/**
 * Scope for creating a link between devices.
 */
@PulverizationMarker
class DevicesLinksScope : Scope<Set<DeviceLink>> {
    private val devicesConnections: MutableSet<DeviceLink> = mutableSetOf()

    /**
     * Link a device with another.
     */
    infix fun String.linkedWith(that: String) {
        if (this == that) error("The same device could not be linked with itself")
        devicesConnections += DeviceLink(this, that)
    }

    override fun build(): Set<DeviceLink> = devicesConnections
}

/**
 * Scope for creating [LogicalDevice]s.
 */
@PulverizationMarker
open class PulverizationConfigScope : Scope<PulverizationConfiguration> {
    private val logicalDevices: MutableSet<LogicalDeviceConfiguration> = mutableSetOf()
    private val devicesConnections: MutableSet<DeviceLink> = mutableSetOf()

    /**
     * Creates a new [LogicalDevice] assigning to it a [name].
     */
    fun logicalDevice(name: String, init: LogicalDeviceScope.() -> Unit) {
        if (logicalDevices.map { it.deviceName }.contains(name)) error("$name already registered")
        val deploymentUnits = LogicalDeviceScope().apply(init).build()
        val allComponents = deploymentUnits.flatMap { it.deployableComponents }.toSet()
        logicalDevices += LogicalDeviceConfiguration(name, allComponents, deploymentUnits)
    }

    /**
     * Defines a logical link between devices.
     */
    fun devicesLinks(init: DevicesLinksScope.() -> Unit) {
        devicesConnections += DevicesLinksScope().apply(init).build()
    }

    /**
     * Creates a new [PulverizationConfiguration].
     */
    override fun build(): PulverizationConfiguration =
        PulverizationConfiguration(logicalDevices, DeviceRelationsConfiguration(devicesConnections))
}

/**
 * Entrypoint for the DSL for building the configuration.
 */
fun pulverizationConfig(init: PulverizationConfigScope.() -> Unit): PulverizationConfiguration =
    PulverizationConfigScope().apply(init).build()
