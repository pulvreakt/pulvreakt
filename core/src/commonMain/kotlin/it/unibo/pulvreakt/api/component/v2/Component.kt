package it.unibo.pulvreakt.api.component.v2

import arrow.core.Either
import it.unibo.pulvreakt.api.capabilities.CapabilitiesMatcher
import it.unibo.pulvreakt.api.initializable.ManagedResource
import it.unibo.pulvreakt.dsl.model.Capability

interface Component<in Input, out Output> : ManagedResource<Any> {
    val requiresToBeExecuted: CapabilitiesMatcher<Capability>
    suspend fun round(input: Input): Either<Any, Output>
}
