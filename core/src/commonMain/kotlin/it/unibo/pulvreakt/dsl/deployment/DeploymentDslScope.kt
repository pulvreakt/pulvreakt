package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.reconfiguration.Reconfigurator
import it.unibo.pulvreakt.dsl.deployment.model.DeploymentSpecification

class DeploymentDslScope {
    private lateinit var communicatorProvider: () -> Communicator
    private lateinit var reconfiguratorProvider: () -> Reconfigurator

    fun withCommunicator(provider: () -> Communicator) {
        communicatorProvider = provider
    }

    fun withReconfigurator(provider: () -> Reconfigurator) {
        reconfiguratorProvider = provider
    }

    fun reconfigurationRules(config: ReconfigurationRulesScope.() -> Unit) {
        TODO()
    }

    internal fun generate(): Either<String, DeploymentSpecification> = either {
        ensure(::communicatorProvider.isInitialized) { "No communicator registered" }
        ensure(::reconfiguratorProvider.isInitialized) { "No reconfigurator registered" }
        TODO()
    }
}
