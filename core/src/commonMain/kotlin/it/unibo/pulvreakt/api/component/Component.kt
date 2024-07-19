package it.unibo.pulvreakt.api.component

import arrow.core.Either
import it.unibo.pulvreakt.api.capabilities.CapabilitiesMatcher

/**
 * TODO.
 */
interface Component<out Output, Capability> {
    /**
     * The capabilities required to execute this component.
     */
    val requiresToBeExecuted: CapabilitiesMatcher<Capability>

    /**
     * Executes a round of the component with the given input.
     */
    suspend fun execute(): Either<Any, Output>
}
