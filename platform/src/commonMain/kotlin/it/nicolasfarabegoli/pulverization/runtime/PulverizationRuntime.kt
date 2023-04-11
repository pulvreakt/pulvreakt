package it.nicolasfarabegoli.pulverization.runtime

import it.nicolasfarabegoli.pulverization.core.Initializable
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.DeploymentUnitRuntimeConfiguration
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host

class PulverizationRuntime<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    private val runtimeConfig: DeploymentUnitRuntimeConfiguration<S, C, SS, AS, O>,
    private val host: Host,
    private val deviceID: String
) : Initializable {

    override suspend fun initialize() { }

    override suspend fun finalize() { }

    suspend fun start() { }

    suspend fun stop() { }
}
