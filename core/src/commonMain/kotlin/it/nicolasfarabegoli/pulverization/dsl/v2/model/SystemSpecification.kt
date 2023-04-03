package it.nicolasfarabegoli.pulverization.dsl.v2.model

/**
 * Specification of the system where all the device are configured with [devicesConfiguration].
 */
data class SystemSpecification(val devicesConfiguration: Set<LogicalDeviceSpecification>)
