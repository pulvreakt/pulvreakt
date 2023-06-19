package it.unibo.pulvreakt.dsl.deployment

import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.model.DeviceReconfigurationRule

class DeviceDeploymentSpecificationScope {
    private val componentsStartupHosts = mutableMapOf<Component, Host>()
    private val deviceReconfigurationRules = mutableSetOf<DeviceReconfigurationRule>()

    infix fun Component.startsOn(host: Host) {
        componentsStartupHosts[this] = host
    }

    fun reconfigurationRules(config: ReconfigurationRulesScope.() -> Unit) {
        val scope = ReconfigurationRulesScope().apply(config)
        deviceReconfigurationRules += scope.generate()
    }
}
