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
import it.unibo.pulvreakt.api.component.Component
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.api.component.pulverization.Actuators
import it.unibo.pulvreakt.api.component.pulverization.Behavior
import it.unibo.pulvreakt.api.component.pulverization.BehaviourOutput
import it.unibo.pulvreakt.api.component.pulverization.Communication
import it.unibo.pulvreakt.api.component.pulverization.Sensors
import it.unibo.pulvreakt.api.component.pulverization.State
import it.unibo.pulvreakt.dsl.errors.SystemConfigurationError
import it.unibo.pulvreakt.dsl.errors.UnspecifiedCapabilities
import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.dsl.model.DeviceStructure
import it.unibo.pulvreakt.api.component.ComponentKind.Actuator as ActuatorsKind
import it.unibo.pulvreakt.api.component.ComponentKind.Behavior as BehaviourKind
import it.unibo.pulvreakt.api.component.ComponentKind.Communication as CommunicationKind
import it.unibo.pulvreakt.api.component.ComponentKind.Sensor as SensorsKind
import it.unibo.pulvreakt.api.component.ComponentKind.State as StateKind

data class ConfiguredComponent(val component: Component)

/**
 * Scope for the system configuration using a canonical pulverization specification.
 */
class CanonicalDeviceScope<St : Any, Co : Any, Sens : Any, Act : Any>(private val deviceName: String) {
    private var behaviourCapability: Pair<ComponentRef, Set<Capability>>? = null
    private var stateCapability: Pair<ComponentRef, Set<Capability>>? = null
    private var commCapability: Pair<ComponentRef, Set<Capability>>? = null
    private var sensorsCapability: Pair<ComponentRef, Set<Capability>>? = null
    private var actuatorsCapability: Pair<ComponentRef, Set<Capability>>? = null

//    fun withBehavior(behaviorLogic: (St?, List<Co>, Sens?) -> BehaviourOutput<St, Co, Act>): ConfiguredComponent {
////        val behavior = object : Behavior<St, Co, Sens, Act>(TODO(), TODO(), TODO(), TODO(), TODO()) {
////            override fun invoke(state: St?, comm: List<Co>, sensors: Sens?): BehaviourOutput<St, Co, Act> {
////                return behaviorLogic(state, comm, sensors)
////            }
////        }
////        return ConfiguredComponent(behavior)
//        TODO()
//    }

    /**
     * Register a [State] component in the device.
     */
    inline fun <reified S : State<St>> withState(): ComponentRef = ComponentRef.create<S>(StateKind).also {
        addComponent("state", it)
    }

    /**
     * Register a [Behavior] component in the device.
     */
    inline fun <reified B : Behavior<St, Co, Sens, Act>> withBehaviour(): ComponentRef =
        ComponentRef.create<B>(BehaviourKind).also {
            addComponent("behaviour", it)
        }

    /**
     * Register a [Sensors] component in the device.
     */
    inline fun <reified SS : Sensors<Sens>> withSensors(): ComponentRef = ComponentRef.create<SS>(SensorsKind).also {
        addComponent("sensors", it)
    }

    /**
     * Register a [Actuators] component in the device.
     */
    inline fun <reified AS : Actuators<Act>> withActuators(): ComponentRef = ComponentRef.create<AS>(ActuatorsKind).also {
        addComponent("actuators", it)
    }

    /**
     * Register a [Comm] component in the device.
     */
    inline fun <reified Comm : Communication<Co>> withCommunication(): ComponentRef = ComponentRef.create<Comm>(
        CommunicationKind,
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
