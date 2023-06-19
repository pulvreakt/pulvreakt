package it.unibo.pulvreakt.dsl.deployment

import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.core.reconfiguration.event.ReconfigurationEvent
import it.unibo.pulvreakt.dsl.model.old.ComponentName
import it.unibo.pulvreakt.dsl.model.old.NewConfiguration

/**
 * Scope for the reconfiguration of a device.
 */
class OnDeviceScope {

    inline fun <reified C : Component> component(): ComponentName = C::class.simpleName!!

    /**
     * Specifies the [newConfiguration] associated to the [ReconfigurationEvent].
     */
    infix fun ReconfigurationEvent<*>.reconfigures(newConfiguration: NewConfiguration): Nothing = TODO()

    /**
     * Utility method for create a [NewConfiguration].
     */
    infix fun ComponentName.movesTo(host: Host): NewConfiguration = this to host

    internal fun generate(): Nothing = TODO()
}
