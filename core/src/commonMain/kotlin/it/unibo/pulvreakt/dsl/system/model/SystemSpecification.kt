package it.unibo.pulvreakt.dsl.system.model

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure

/**
 * Represents which [logicalDevices] are involved in the system.
 */
data class SystemSpecification private constructor(val logicalDevices: Collection<LogicalDeviceSpecification>) {
    companion object {
        private fun allNameDistinct(logicalDevices: Collection<LogicalDeviceSpecification>): Boolean =
            logicalDevices.distinctBy { it.deviceName }.size == logicalDevices.size

        /**
         * Smart constructor for [SystemSpecification].
         */
        operator fun invoke(logicalDevices: Collection<LogicalDeviceSpecification>): Either<String, SystemSpecification> = either {
            ensure(allNameDistinct(logicalDevices)) { "Multiple device with the same name are not allowed" }
            SystemSpecification(logicalDevices)
        }
    }
}
