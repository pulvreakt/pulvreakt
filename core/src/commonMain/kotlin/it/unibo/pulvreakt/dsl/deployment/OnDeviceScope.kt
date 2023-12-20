package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptySet
import arrow.core.flatten
import arrow.core.mapOrAccumulate
import arrow.core.nonEmptyListOf
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.zipOrAccumulate
import it.unibo.pulvreakt.api.component.Component
import it.unibo.pulvreakt.api.component.ComponentKind
import it.unibo.pulvreakt.api.component.ComponentRef
import it.unibo.pulvreakt.api.component.pulverization.Actuators
import it.unibo.pulvreakt.api.component.pulverization.Behaviour
import it.unibo.pulvreakt.api.component.pulverization.Communication
import it.unibo.pulvreakt.api.component.pulverization.Sensors
import it.unibo.pulvreakt.api.component.pulverization.State
import it.unibo.pulvreakt.api.infrastructure.Host
import it.unibo.pulvreakt.api.reconfiguration.event.ReconfigurationEvent
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError
import it.unibo.pulvreakt.dsl.errors.InvalidReconfiguration
import it.unibo.pulvreakt.dsl.errors.InvalidReconfigurationHost
import it.unibo.pulvreakt.dsl.errors.UnknownComponent
import it.unibo.pulvreakt.dsl.model.DeviceReconfigurationRule
import it.unibo.pulvreakt.dsl.model.DeviceStructure
import it.unibo.pulvreakt.dsl.model.NewConfiguration
import it.unibo.pulvreakt.dsl.model.OnDeviceRules

/**
 * Scope for the reconfiguration of a device.
 */
class OnDeviceScope(private val deviceStructure: DeviceStructure, private val infrastructure: NonEmptySet<Host>) {
    private val rules = mutableListOf<Either<Nel<DeploymentConfigurationError>, DeviceReconfigurationRule>>()

    /**
     * Specifies which Behaviour component should be reconfigured.
     */
    inline fun <reified B : Behaviour<*, *, *, *>> theBehaviour(): ComponentRef =
        ComponentRef.create<B>(ComponentKind.Behavior)

    /**
     * Specifies which State component should be reconfigured.
     */
    inline fun <reified S : State<*>> theState(): ComponentRef =
        ComponentRef.create<S>(ComponentKind.State)

    /**
     * Specifies which Communication component should be reconfigured.
     */
    inline fun <reified C : Communication<*>> theCommunication(): ComponentRef =
        ComponentRef.create<C>(ComponentKind.Communication)

    /**
     * Specifies which Sensors component should be reconfigured.
     */
    inline fun <reified SS : Sensors<*>> theSensors(): ComponentRef =
        ComponentRef.create<SS>(ComponentKind.Sensor)

    /**
     * Specifies which Actuators component should be reconfigured.
     */
    inline fun <reified AS : Actuators<*>> theActuators(): ComponentRef =
        ComponentRef.create<AS>(ComponentKind.Actuator)

    /**
     * Specifies which generic component should be reconfigured.
     */
    inline fun <reified Comp : Component> theComponent(): ComponentRef =
        ComponentRef.create<Comp>()

    /**
     * Specifies the [newConfiguration] associated to the [ReconfigurationEvent].
     */
    infix fun ReconfigurationEvent<*>.reconfigures(newConfiguration: NewConfiguration) {
        val (componentType, host) = newConfiguration
        val components = deviceStructure.componentsGraph.keys + deviceStructure.componentsGraph.values.flatten()

        val result = either<Nel<DeploymentConfigurationError>, DeviceReconfigurationRule> {
            zipOrAccumulate(
                { ensure(componentType in components) { UnknownComponent(componentType) } },
                { ensure(host in infrastructure) { InvalidReconfigurationHost(host) } },
                { ensure(componentType in deviceStructure.requiredCapabilities.keys) { UnknownComponent(componentType) } },
                {
                    val componentCapabilities = deviceStructure.requiredCapabilities[componentType]
                        ?: raise(nonEmptyListOf(UnknownComponent(componentType)))
                    val hostCapability = host.capabilities
                    val isCompatible = componentCapabilities.intersect(hostCapability).isNotEmpty()
                    ensure(isCompatible) { InvalidReconfiguration(componentType, host) }
                },
            ) { _, _, _, _ -> DeviceReconfigurationRule(this@reconfigures, newConfiguration) }
        }
        rules += result
    }

    /**
     * Utility method for create a [NewConfiguration].
     */
    infix fun ComponentRef.movesTo(host: Host): NewConfiguration = NewConfiguration(this, host)

    internal fun generate(): Either<Nel<DeploymentConfigurationError>, OnDeviceRules> = rules.mapOrAccumulate {
        when (it) {
            is Either.Right -> it.bind()
            is Either.Left -> raise(it.value)
        }
    }.mapLeft { it.flatten() }.map { it.toSet() }
}
