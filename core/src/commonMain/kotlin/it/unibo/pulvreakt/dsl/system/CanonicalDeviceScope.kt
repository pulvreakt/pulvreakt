package it.unibo.pulvreakt.dsl.system

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptySet
import arrow.core.nonEmptySetOf
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.zipOrAccumulate
import arrow.core.toNonEmptySetOrNull
import it.unibo.pulvreakt.core.component.ComponentRef
import it.unibo.pulvreakt.core.component.pulverisation.Actuators
import it.unibo.pulvreakt.core.component.pulverisation.Behaviour
import it.unibo.pulvreakt.core.component.pulverisation.Communication
import it.unibo.pulvreakt.core.component.pulverisation.Sensors
import it.unibo.pulvreakt.core.component.pulverisation.State
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError.UnspecifiedCapabilities
import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.dsl.model.DeviceStructure
import it.unibo.pulvreakt.core.component.ComponentType.Actuator as ActuatorsType
import it.unibo.pulvreakt.core.component.ComponentType.Behaviour as BehaviourType
import it.unibo.pulvreakt.core.component.ComponentType.Communication as CommunicationType
import it.unibo.pulvreakt.core.component.ComponentType.Sensor as SensorsType
import it.unibo.pulvreakt.core.component.ComponentType.State as StateType

/**
 * Scope for the system configuration using a canonical pulverization specification.
 */
class CanonicalDeviceScope(private val deviceName: String) {
    private var behaviourCapability: Pair<ComponentRef, Set<Capability>>? = null
    private var stateCapability: Pair<ComponentRef, Set<Capability>>? = null
    private var commCapability: Pair<ComponentRef, Set<Capability>>? = null
    private var sensorsCapability: Pair<ComponentRef, Set<Capability>>? = null
    private var actuatorsCapability: Pair<ComponentRef, Set<Capability>>? = null

    /**
     * Register a [State] component in the device.
     */
    inline fun <reified S : State<*>> withState(): ComponentRef = ComponentRef.create<S>(StateType).also {
        addComponent("state", it)
    }

    /**
     * Register a [Behaviour] component in the device.
     */
    inline fun <reified B : Behaviour<*, *, *, *>> withBehaviour(): ComponentRef = ComponentRef.create<B>(BehaviourType).also {
        addComponent("behaviour", it)
    }

    /**
     * Register a [Sensors] component in the device.
     */
    inline fun <reified SS : Sensors<*>> withSensors(): ComponentRef = ComponentRef.create<SS>(SensorsType).also {
        addComponent("sensors", it)
    }

    /**
     * Register a [Actuators] component in the device.
     */
    inline fun <reified AS : Actuators<*>> withActuators(): ComponentRef = ComponentRef.create<AS>(ActuatorsType).also {
        addComponent("actuators", it)
    }

    /**
     * Register a [Comm] component in the device.
     */
    inline fun <reified Comm : Communication<*>> withCommunication(): ComponentRef = ComponentRef.create<Comm>(
        CommunicationType,
    ).also { addComponent("comm", it) }

    /**
     * Register a new [component] with the given [tag].
     * This method, even if it is public, should not be used directly.
     */
    fun addComponent(tag: String, component: ComponentRef) {
        when (tag) {
            "behaviour" -> behaviourCapability = component to emptySet()
            "state" -> stateCapability = component to emptySet()
            "comm" -> commCapability = component to emptySet()
            "sensors" -> sensorsCapability = component to emptySet()
            "actuators" -> actuatorsCapability = component to emptySet()
            else -> Unit
        }
    }

    /**
     * Specifies that a component needs a [capability] to be executed.
     */
    infix fun ComponentRef.requires(capability: Capability) = requires(nonEmptySetOf(capability))

    /**
     * Specifies that a component needs a set of [capabilities] to be executed.
     */
    infix fun ComponentRef.requires(capabilities: NonEmptySet<Capability>) {
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
                sensorsCapability?.first,
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
        val actuatorsWiring = actuatorsCapability?.let {
            it.first to setOf(behaviourCapability?.first).filterNotNull().toSet()
        }
        val graph = listOfNotNull(behaviourWiring, stateWiring, commWiring, sensorsWiring, actuatorsWiring).toMap()
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
