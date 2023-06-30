package it.unibo.pulvreakt.dsl.system

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptySet
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.zipOrAccumulate
import arrow.core.toNonEmptySetOrNull
import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.UnspecifiedCapabilities
import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.dsl.model.ComponentType
import it.unibo.pulvreakt.dsl.model.ComponentType.Companion.ctypeOf
import it.unibo.pulvreakt.dsl.model.DeviceStructure

class CanonicalDeviceScope(private val deviceName: String) {
    private var behaviourCapability: Pair<ComponentType, Set<Capability>>? = null
    private var stateCapability: Pair<ComponentType, Set<Capability>>? = null
    private var commCapability: Pair<ComponentType, Set<Capability>>? = null
    private var sensorsCapability: Pair<ComponentType, Set<Capability>>? = null
    private var actuatorsCapability: Pair<ComponentType, Set<Capability>>? = null

    inline fun <reified State : Component<*>> withState(): ComponentType = ctypeOf<State>().also {
        addComponent(
            "state",
            it,
        )
    }
    inline fun <reified Behaviour : Component<*>> withBehaviour(): ComponentType = ctypeOf<Behaviour>().also {
        addComponent(
            "behaviour",
            it,
        )
    }
    inline fun <reified Sensors : Component<*>> withSensors(): ComponentType = ctypeOf<Sensors>().also {
        addComponent(
            "sensors",
            it,
        )
    }
    inline fun <reified Actuators : Component<*>> withActuators(): ComponentType = ctypeOf<Actuators>().also {
        addComponent(
            "actuators",
            it,
        )
    }
    inline fun <reified Comm : Component<*>> withCommunication(): ComponentType = ctypeOf<Comm>().also {
        addComponent(
            "comm",
            it,
        )
    }

    fun addComponent(tag: String, component: ComponentType) {
        when (tag) {
            "behaviour" -> behaviourCapability = component to emptySet()
            "state" -> stateCapability = component to emptySet()
            "comm" -> commCapability = component to emptySet()
            "sensors" -> sensorsCapability = component to emptySet()
            "actuators" -> actuatorsCapability = component to emptySet()
            else -> Unit
        }
    }

    infix fun ComponentType.requires(capability: Capability) {
        when (this) {
            behaviourCapability?.first -> behaviourCapability = behaviourCapability!!.first to setOf(capability)
            stateCapability?.first -> stateCapability = stateCapability!!.first to setOf(capability)
            commCapability?.first -> commCapability = commCapability!!.first to setOf(capability)
            sensorsCapability?.first -> sensorsCapability = sensorsCapability!!.first to setOf(capability)
            actuatorsCapability?.first -> actuatorsCapability = actuatorsCapability!!.first to setOf(capability)
            else -> Unit
        }
    }

    infix fun ComponentType.requires(capabilities: NonEmptySet<Capability>) {
        when (this) {
            behaviourCapability?.first -> behaviourCapability = behaviourCapability!!.first to capabilities.toSet()
            stateCapability?.first -> stateCapability = stateCapability!!.first to capabilities.toSet()
            commCapability?.first -> commCapability = commCapability!!.first to capabilities.toSet()
            sensorsCapability?.first -> sensorsCapability = sensorsCapability!!.first to capabilities.toSet()
            actuatorsCapability?.first -> actuatorsCapability = actuatorsCapability!!.first to capabilities.toSet()
            else -> Unit
        }
    }

    private fun Raise<Nel<UnspecifiedCapabilities>>.ensureNonEmptyCapability() {
        zipOrAccumulate(
            { behaviourCapability?.let { ensure(it.second.isNotEmpty()) { UnspecifiedCapabilities(it.first) } } },
            { stateCapability?.let { ensure(it.second.isNotEmpty()) { UnspecifiedCapabilities(it.first) } } },
            { commCapability?.let { ensure(it.second.isNotEmpty()) { UnspecifiedCapabilities(it.first) } } },
            { sensorsCapability?.let { ensure(it.second.isNotEmpty()) { UnspecifiedCapabilities(it.first) } } },
            { actuatorsCapability?.let { ensure(it.second.isNotEmpty()) { UnspecifiedCapabilities(it.first) } } },
        ) { _, _, _, _, _ -> }
    }

    internal fun generate(): Either<Nel<SystemConfigurationError>, DeviceStructure> = either {
        ensureNonEmptyCapability()
        val behaviourWiring = behaviourCapability?.let {
            it.first to setOf(
                stateCapability?.first,
                commCapability?.first,
                actuatorsCapability?.first,
            ).filterNotNull().toSet()
        }
        val stateWiring = stateCapability?.let {
            it.first to setOf(behaviourCapability?.first).filterNotNull().toSet()
        }
        val commWiring = commCapability?.let {
            it.first to setOf(behaviourCapability?.first).filterNotNull().toSet()
        }
        val sensorsWiring = sensorsCapability?.let {
            it.first to setOf(behaviourCapability?.first).filterNotNull().toSet()
        }
        val graph = listOfNotNull(behaviourWiring, stateWiring, commWiring, sensorsWiring).toMap()
        val requiredCapabilities = setOfNotNull(
            behaviourCapability,
            stateCapability,
            commCapability,
            sensorsCapability,
            actuatorsCapability,
        ).associate { it.first to it.second.toNonEmptySetOrNull()!! }
        DeviceStructure(deviceName, graph, requiredCapabilities)
    }
}
