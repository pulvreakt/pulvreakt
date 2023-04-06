package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import it.nicolasfarabegoli.pulverization.dsl.v2.model.SystemSpecification
import kotlinx.coroutines.coroutineScope

/**
 * TODO.
 */
suspend fun <S : Any, C : Any, SS : Any, AS : Any, O : Any> pulverizationRuntime(
    configuration: SystemSpecification,
    deviceName: String,
    config: suspend PulverizationRuntimeScope<S, C, SS, AS, O>.() -> Unit,
) = coroutineScope { }
