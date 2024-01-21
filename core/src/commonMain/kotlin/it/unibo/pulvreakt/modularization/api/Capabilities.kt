package it.unibo.pulvreakt.modularization.api

import it.unibo.pulvreakt.modularization.api.module.Module

/**
 * Base interface for modelling the capabilities.
 *
 * ## Example of "Union Type" capability
 *
 * ```kotlin
 * sealed interface UnionCapabilities : Capabilities
 * data object CapabilityOne : UnionCapabilities
 * data object CapabilityTwo : UnionCapabilities
 * ```
 * The example above tries to emulate a union type specifying that one of the two kind of the capabilities must be available to execute a [Module].
 *
 * ## Example of "Intersection Type" capability
 *
 * ```kotlin
 * interface BaseCapability : Capabilities
 * class CapabilityOne() : BaseCapability
 * class CapabilityTwo() : CapabilityOne
 * ```
 *
 * The example above tries to emulate an intersection type specifying that the both the capabilities must be available to execute a [Module].
 */
interface Capabilities
