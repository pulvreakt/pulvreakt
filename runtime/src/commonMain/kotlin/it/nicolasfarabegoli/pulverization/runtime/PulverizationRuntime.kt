package it.nicolasfarabegoli.pulverization.runtime

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.ActuatorsComponent
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.CommunicationComponent
import it.nicolasfarabegoli.pulverization.core.PulverizedComponent
import it.nicolasfarabegoli.pulverization.core.PulverizedComponentType
import it.nicolasfarabegoli.pulverization.core.SensorsComponent
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.core.StateComponent
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import it.nicolasfarabegoli.pulverization.dsl.LogicalDeviceConfiguration
import it.nicolasfarabegoli.pulverization.runtime.dsl.RuntimeDSL
import kotlinx.coroutines.coroutineScope

class PulverizationRuntime {

    fun withDeploymentUnit(
        deviceConfig: LogicalDeviceConfiguration,
        vararg deployableComponents: PulverizedComponentType,
        init: suspend RuntimeDSL.() -> Unit,
    ) {
        if (deviceConfig.deploymentUnits.any { it.deployableComponents.containsAll(deployableComponents.toSet()) }) {
            error("Unable to find a deployable unit with the provided components: $deployableComponents")
        }
        // The following components are remote and for each one of this, a component reference should be produced.
        val remoteComponents = deviceConfig.components - deployableComponents.toSet()

        deployableComponents.toSet().forEach {
            when (it) {
                ActuatorsComponent -> TODO()
                BehaviourComponent -> TODO()
                CommunicationComponent -> TODO()
                SensorsComponent -> TODO()
                StateComponent -> TODO()
            }
        }
    }

    private fun linkWithBehaviour(
        self: PulverizedComponent,
        remotes: Set<PulverizedComponentType>,
        locals: Set<PulverizedComponentType>,
    ): BehaviourRef = TODO()

    private fun linkWithOtherComponents(
        remotes: Set<PulverizedComponentType>,
        locals: Set<PulverizedComponentType>,
    ): Quadruple<StateRef?, CommunicationRef?, SensorsRef?, ActuatorRef?> = TODO()
}

internal data class Quadruple<out A, out B, out C, out D>(val first: A, val second: B, val third: C, val fourth: D)

// This sample of code shows the high level API to create a pulverized system

data class SR(val i: Int) : StateRepresentation
class MyState(override val context: Context) : State<SR> {
    override fun get(): SR = TODO("Not yet implemented")

    override fun update(newState: SR): SR = TODO("Not yet implemented")
}

suspend fun senseLogic(state: MyState, bhRef: BehaviourRef): Unit = coroutineScope {
    TODO()
}

suspend fun test(): Unit = coroutineScope {
    PulverizationRuntime().withDeploymentUnit(TODO(), StateComponent) {
        withPlatform()
        withStateLogic(::senseLogic)
    }
}
