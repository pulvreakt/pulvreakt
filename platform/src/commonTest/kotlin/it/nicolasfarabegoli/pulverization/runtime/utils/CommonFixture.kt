package it.nicolasfarabegoli.pulverization.runtime.utils

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.BehaviourOutput
import it.nicolasfarabegoli.pulverization.core.Sensor
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.runtime.componentsref.ActuatorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.CommunicationRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.SensorsRef
import it.nicolasfarabegoli.pulverization.runtime.componentsref.StateRef
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.koin.core.component.inject
import kotlin.random.Random

class BehaviourTest : Behaviour<Unit, Unit, Int, Unit, Unit> {
    override val context: Context by inject()

    override fun invoke(state: Unit, export: List<Unit>, sensedValues: Int): BehaviourOutput<Unit, Unit, Unit, Unit> {
        return BehaviourOutput(Unit, Unit, Unit, Unit)
    }
}

@Suppress("UnusedPrivateMember", "UnusedParameter")
suspend fun behaviourTestLogic(
    behaviour: Behaviour<Unit, Unit, Int, Unit, Unit>,
    stateRef: StateRef<Unit>,
    commRef: CommunicationRef<Unit>,
    sensRef: SensorsRef<Int>,
    actRef: ActuatorsRef<Unit>,
) = coroutineScope {
    stateRef.receiveFromComponent()
    commRef.receiveFromComponent()
    actRef.receiveFromComponent()
    sensRef.receiveFromComponent().collect {
        behaviour(Unit, emptyList(), it)
    }
}

class SensorTest : Sensor<Int> {
    override suspend fun sense(): Int = Random.nextInt(0, 100)
}

class SensorsContainerTest : SensorsContainer() {
    override val context: Context by inject()
    override suspend fun initialize() {
        this += SensorTest()
    }
}

suspend fun sensorsLogicTest(
    sensors: SensorsContainer,
    behaviourRef: BehaviourRef<Int>,
) = coroutineScope {
    println("Spawned")
    sensors.get<SensorTest> {
        while (true) {
            println("send")
            behaviourRef.sendToComponent(sense())
            delay(200)
        }
    }
}
