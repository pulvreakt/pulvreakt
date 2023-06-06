package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.mapOrAccumulate
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import arrow.core.raise.zipOrAccumulate
import it.unibo.pulvreakt.core.communicator.Communicator
import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.core.reconfiguration.Reconfigurator
import it.unibo.pulvreakt.dsl.deployment.model.ComponentsContainer
import it.unibo.pulvreakt.dsl.deployment.model.DeploymentSpecification
import it.unibo.pulvreakt.dsl.deployment.model.ReconfigurationRules
import it.unibo.pulvreakt.dsl.system.model.LogicalDeviceSpecification

/**
 * Scope for the deployment DSL configuration.
 */
class DeploymentDslScope(
    private val availableHosts: Set<Host>,
    private val logicalDeviceSpecification: LogicalDeviceSpecification,
) {
    private var communicatorProvider: (() -> Communicator)? = null
    private var reconfiguratorProvider: (() -> Reconfigurator)? = null
    private var componentsContainer: ComponentsContainer = emptyMap()
    private var reconfigurationRules: Either<NonEmptyList<String>, ReconfigurationRules>? = null

    fun withCommunicator(provider: () -> Communicator) {
        communicatorProvider = provider
    }

    fun withReconfigurator(provider: () -> Reconfigurator) {
        reconfiguratorProvider = provider
    }

    fun reconfigurationRules(config: ReconfigurationRulesScope.() -> Unit) {
        val reconfigurationRulesScope = ReconfigurationRulesScope(logicalDeviceSpecification).apply(config)
        reconfigurationRules = reconfigurationRulesScope.generate()
    }

    /**
     * Specifies the startup [host] for the given component.
     */
    infix fun Component<*>.startsOn(host: Host) {
        componentsContainer = componentsContainer + (this to host)
    }

    private fun allComponentsAreDeclared(): Either<NonEmptyList<String>, Unit> {
        val systemComponents = logicalDeviceSpecification.componentsRequiredCapabilities.keys
        val registeredComponents = componentsContainer.keys.map { it.type }.toSet()

        fun Raise<String>.isIn(component: ComponentType<*>) {
            ensure(component in registeredComponents) { "Component $component is not registered" }
        }

        return systemComponents.mapOrAccumulate { isIn(it) }.map { }
    }

    internal fun generate(): Either<NonEmptyList<String>, DeploymentSpecification> = either {
        zipOrAccumulate(
            { ensureNotNull(communicatorProvider) { "No communicator registered" } },
            { ensureNotNull(reconfiguratorProvider) { "No reconfigurator registered" } },
            { ensure(componentsContainer.isNotEmpty()) { "No components are registered" } },
            { reconfigurationRules?.bindNel() },
            { allComponentsAreDeclared().bindNel() },
        ) { cprovider, rprovider, _, rules, _ ->
            DeploymentSpecification(logicalDeviceSpecification, componentsContainer, rules, cprovider, rprovider, availableHosts)
        }
    }
}
