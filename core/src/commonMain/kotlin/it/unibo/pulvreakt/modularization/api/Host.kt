package it.unibo.pulvreakt.modularization.api

import it.unibo.pulvreakt.modularization.api.module.Module

/**
 * Define a host capable of executing one or more [Module]s.
 * A host [exposedCapabilities] for executing the compatible [Module]s.
 */
interface Host<C : Capabilities> {
    val exposedCapabilities: C
}
