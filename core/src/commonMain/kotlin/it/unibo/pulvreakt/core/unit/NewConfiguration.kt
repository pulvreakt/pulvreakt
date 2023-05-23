package it.unibo.pulvreakt.core.unit

import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.infrastructure.Host

/**
 * Represents the new [configuration] of the logical device, split in its deployment units.
 */
data class NewConfiguration(val configuration: Map<Component<*>, Host>)
