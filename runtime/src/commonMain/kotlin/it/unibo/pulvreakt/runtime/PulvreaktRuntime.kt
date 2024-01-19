package it.unibo.pulvreakt.runtime

import arrow.core.Either
import arrow.core.raise.either
import it.unibo.pulvreakt.api.infrastructure.Host
import it.unibo.pulvreakt.api.initializable.ManagedResource
import it.unibo.pulvreakt.dsl.model.PulvreaktConfiguration
import it.unibo.pulvreakt.runtime.errors.RuntimeError

/**
 * The [PulvreaktRuntime] is the main entry point of the runtime.
 */
interface PulvreaktRuntime<ID : Any> : ManagedResource<RuntimeError> {
    /**
     * Starts the runtime.
     */
    suspend fun start(): Either<RuntimeError, Unit>

    /**
     * Stops the runtime.
     */
    suspend fun stop(): Either<RuntimeError, Unit>

    companion object {
        /**
         * Smart constructor for [PulvreaktRuntime].
         * @return an [Either] with [RuntimeError] on the left side and [PulvreaktRuntime] on the right side.
         */
        suspend operator fun <ID : Any> invoke(
            config: PulvreaktConfiguration,
            device: String,
            id: ID,
            host: Host,
        ): Either<RuntimeError, PulvreaktRuntime<ID>> = either {
            val runtime = PulvreaktRuntimeImpl(config, device, id, host)
            runtime.initialize().bind()
            runtime
        }
    }
}
