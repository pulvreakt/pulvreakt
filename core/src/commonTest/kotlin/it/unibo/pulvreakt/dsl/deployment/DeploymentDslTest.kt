package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.right
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.core.component.AbstractComponent
import it.unibo.pulvreakt.core.component.ComponentTypeDelegate
import it.unibo.pulvreakt.core.infrastructure.HostDelegate
import it.unibo.pulvreakt.core.reconfiguration.event.AbstractReconfigurationEvent
import it.unibo.pulvreakt.dsl.system.model.CapabilityDelegate
import it.unibo.pulvreakt.dsl.system.pulverizedSystem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

val highCPU by CapabilityDelegate()
val embeddedDevice by CapabilityDelegate()
val highMemory by CapabilityDelegate()
val componentType1 by ComponentTypeDelegate<Int>()
val componentType2 by ComponentTypeDelegate<String>()
val componentType3 by ComponentTypeDelegate<Float>()
val host1 by HostDelegate(highCPU)
val host2 by HostDelegate(highCPU, highMemory)
val host3 by HostDelegate(embeddedDevice)

val event = object : AbstractReconfigurationEvent<Int>() {
    override fun eventsFlow(): Flow<Int> = emptyFlow()
    override fun condition(event: Int): Boolean = event > 10
}

class ComponentTest1 : AbstractComponent<Int>() {
    override val name: String = "componentTest"
    override val type = componentType1
    override suspend fun execute(): Either<String, Unit> = Unit.right()
}
class ComponentTest2 : AbstractComponent<String>() {
    override val name: String = "componentTest"
    override val type = componentType2
    override suspend fun execute(): Either<String, Unit> = Unit.right()
}
class ComponentTest3 : AbstractComponent<Float>() {
    override val name: String = "componentTest"
    override val type = componentType3
    override suspend fun execute(): Either<String, Unit> = Unit.right()
}

val systemSpec = pulverizedSystem {
    device("device 1") {
        componentType1 deployableOn highCPU
        componentType2 deployableOn setOf(highCPU, highMemory)
        componentType3 deployableOn embeddedDevice
    }
}

class DeploymentDslTest : StringSpec(
    {
        "The deployment DSL should raise an error if one or more components are not configured" {
            val spec = systemSpec.getOrNull() ?: error("No error must be raised")
            val deploymentSpec = pulverizationRuntime("device 1", setOf(host1, host2, host3), spec) {
                ComponentTest1() startsOn host1
                // ComponentTest2() startsOn host2
                // ComponentTest3() startsOn host1
                reconfigurationRules {
                    onDevice {
                        event reconfigures (componentType1 to host3) // host3 is not compatible with componentType1
                    }
                }
            }
            deploymentSpec.isLeft() shouldBe true
            // TODO: other assertions
        }
    },
)
