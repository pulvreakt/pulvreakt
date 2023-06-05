package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.zipOrAccumulate
import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.core.reconfiguration.Reconfigurator
import it.unibo.pulvreakt.dsl.deployment.model.ComponentsContainer
import it.unibo.pulvreakt.dsl.deployment.model.DeploymentSpecification
import it.unibo.pulvreakt.dsl.deployment.model.ReconfigurationRules
import it.unibo.pulvreakt.dsl.system.model.SystemSpecification

class DeploymentDslScope(
    private val deviceName: String,
    private val availableHosts: Set<Host>,
    private val systemSpecification: SystemSpecification,
) {
    private lateinit var communicatorProvider: () -> Communicator
    private lateinit var reconfiguratorProvider: () -> Reconfigurator
    private var componentsContainer: ComponentsContainer = emptyMap()
    private var reconfigurationRules: Either<NonEmptyList<String>, ReconfigurationRules>? = null

    fun withCommunicator(provider: () -> Communicator) {
        communicatorProvider = provider
    }

    fun withReconfigurator(provider: () -> Reconfigurator) {
        reconfiguratorProvider = provider
    }

    fun reconfigurationRules(config: ReconfigurationRulesScope.() -> Unit) {
        val reconfigurationRulesScope = ReconfigurationRulesScope().apply(config)
        reconfigurationRules = reconfigurationRulesScope.generate()
    }

    fun Component<*>.startsOn(host: Host) {
        componentsContainer = componentsContainer + (this to host)
    }

    private fun allComponentsAreDeclared(): Either<NonEmptyList<String>, ComponentsContainer> = TODO()

    internal fun generate(): Either<NonEmptyList<String>, DeploymentSpecification> = either {
        zipOrAccumulate(
            { ensure(::communicatorProvider.isInitialized) { "No communicator registered" } },
            { ensure(::reconfiguratorProvider.isInitialized) { "No reconfigurator registered" } },
            { ensure(::componentsContainer.isInitialized) { "No components container registered" } },
            { allComponentsAreDeclared().bind() },
            { reconfigurationRules?.bind() },
        ) { _, _, _, components, reconfigurationRules ->
            DeploymentSpecification(
                systemSpecification,
                components,
                reconfigurationRules,
                communicatorProvider,
                reconfiguratorProvider,
                availableHosts,
            )
        }
    }
}
