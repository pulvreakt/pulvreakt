@file:Suppress("TooManyFunctions")

package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlaceProvider
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ActuatorsRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.BehaviourRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.CommunicationRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ComponentsRuntimeConfiguration
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ComponentsRuntimeContainer
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.PartialActuatorsRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.PartialBehaviourRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.PartialCommunicationRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.PartialSensorsRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.PartialStateRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ReconfigurationRules
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.SensorsRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.StateRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.Reconfigurator
import it.nicolasfarabegoli.pulverization.runtime.utils.ActuatorsLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.BehaviourLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.CommunicationLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.SensorsLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.StateLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.defaultActuatorsLogic
import it.nicolasfarabegoli.pulverization.runtime.utils.defaultBehaviourLogic
import it.nicolasfarabegoli.pulverization.runtime.utils.defaultCommunicationLogic
import it.nicolasfarabegoli.pulverization.runtime.utils.defaultSensorsLogic
import it.nicolasfarabegoli.pulverization.runtime.utils.defaultStateLogic

/**
 * Scope for configuring the device with its components.
 */
class PulverizationRuntimeScope<S : Any, C : Any, SS : Any, AS : Any, O : Any> {
    private var componentsRuntime = ComponentsRuntimeContainer<S, C, SS, AS, O>(null, null, null, null, null)
    private var allReconfigurationRules: ReconfigurationRules? = null
    private lateinit var communicator: () -> Communicator
    private lateinit var reconfigurator: () -> Reconfigurator
    private lateinit var remotePlaceProvider: RemotePlaceProvider

    /**
     * Specify which [Communicator] should be used.
     */
    fun withCommunicator(commProvider: () -> Communicator) {
        communicator = commProvider
    }

    /**
     * Specify which [Reconfigurator] should be used.
     */
    fun withReconfigurator(reconfigProvider: () -> Reconfigurator) {
        reconfigurator = reconfigProvider
    }

    /**
     * Specify which [RemotePlaceProvider] should be used.
     */
    fun withRemotePlaceProvider(rpProvider: () -> RemotePlaceProvider) {
        remotePlaceProvider = rpProvider()
    }

    /**
     * Configure all the reconfiguration rules.
     */
    fun reconfigurationRules(config: ReconfigurationRulesScope.() -> Unit) {
        val reconfigurationRulesScope = ReconfigurationRulesScope().apply(config)
        allReconfigurationRules = reconfigurationRulesScope.generate()
    }

    /**
     * Associate the [Behaviour] with its corresponding [logic].
     */
    infix fun Behaviour<S, C, SS, AS, O>.withLogic(
        logic: BehaviourLogicType<S, C, SS, AS, O>,
    ): PartialBehaviourRuntimeConfig<S, C, SS, AS, O> = PartialBehaviourRuntimeConfig(this, logic)

    /**
     * Associate the [State] with its corresponding [logic].
     */
    infix fun State<S>.withLogic(
        logic: StateLogicType<S>,
    ): PartialStateRuntimeConfig<S> = PartialStateRuntimeConfig(this, logic)

    /**
     * Associate the [Communication] with its corresponding [logic].
     */
    infix fun Communication<C>.withLogic(
        logic: CommunicationLogicType<C>,
    ): PartialCommunicationRuntimeConfig<C> = PartialCommunicationRuntimeConfig(this, logic)

    /**
     * Associate the [SensorsContainer] with its corresponding [logic].
     */
    infix fun SensorsContainer.withLogic(
        logic: SensorsLogicType<SS>,
    ): PartialSensorsRuntimeConfig<SS> = PartialSensorsRuntimeConfig(this, logic)

    /**
     * Associate the [ActuatorsContainer] with its corresponding [logic].
     */
    infix fun ActuatorsContainer.withLogic(
        logic: ActuatorsLogicType<AS>,
    ): PartialActuatorsRuntimeConfig<AS> = PartialActuatorsRuntimeConfig(this, logic)

    /**
     * Configure the [Behaviour] to start on a specific [host].
     */
    infix fun Behaviour<S, C, SS, AS, O>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            behaviourRuntime = BehaviourRuntimeConfig(this, ::defaultBehaviourLogic, host),
        )
    }

    /**
     * Configure the [State] to start on a specific [host].
     */
    infix fun State<S>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            stateRuntime = StateRuntimeConfig(this, ::defaultStateLogic, host),
        )
    }

    /**
     * Configure the [Communication] to start on a specific [host].
     */
    infix fun Communication<C>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            communicationRuntime = CommunicationRuntimeConfig(this, ::defaultCommunicationLogic, host),
        )
    }

    /**
     * Configure the [SensorsContainer] to start on a specific [host].
     */
    infix fun SensorsContainer.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            sensorsRuntime = SensorsRuntimeConfig(this, ::defaultSensorsLogic, host),
        )
    }

    /**
     * Configure the [ActuatorsContainer] to start on a specific [host].
     */
    infix fun ActuatorsContainer.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            actuatorsRuntime = ActuatorsRuntimeConfig(this, ::defaultActuatorsLogic, host),
        )
    }

    /**
     * Configure the [Behaviour] to start on a specific [host].
     */
    infix fun PartialBehaviourRuntimeConfig<S, C, SS, AS, O>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            behaviourRuntime = BehaviourRuntimeConfig(behaviourComponent, behaviourLogic, host),
        )
    }

    /**
     * Configure the [State] to start on a specific [host].
     */
    infix fun PartialStateRuntimeConfig<S>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            stateRuntime = StateRuntimeConfig(stateComponent, stateLogic, host),
        )
    }

    /**
     * Configure the [Communication] to start on a specific [host].
     */
    infix fun PartialCommunicationRuntimeConfig<C>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            communicationRuntime = CommunicationRuntimeConfig(communicationComponent, communicationLogic, host),
        )
    }

    /**
     * Configure the [SensorsContainer] to start on a specific [host].
     */
    infix fun PartialSensorsRuntimeConfig<SS>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            sensorsRuntime = SensorsRuntimeConfig(sensorsComponent, sensorsLogic, host),
        )
    }

    /**
     * Configure the [ActuatorsContainer] to start on a specific [host].
     */
    infix fun PartialActuatorsRuntimeConfig<AS>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            actuatorsRuntime = ActuatorsRuntimeConfig(actuatorsComponent, actuatorsLogic, host),
        )
    }

    internal fun generate(): ComponentsRuntimeConfiguration<S, C, SS, AS, O> {
        require(::communicator.isInitialized) { "Communicator not initialized" }
        require(::reconfigurator.isInitialized) { "Reconfigurator not initialized" }
        require(::remotePlaceProvider.isInitialized) { "Remote place provider not initialized" }
        return ComponentsRuntimeConfiguration(
            componentsRuntime,
            allReconfigurationRules,
            communicator,
            reconfigurator,
            remotePlaceProvider,
        )
    }
}
