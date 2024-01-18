package it.unibo.pulvreakt.modularization.fixtures

import it.unibo.pulvreakt.modularization.api.AbstractModule
import it.unibo.pulvreakt.modularization.api.Capabilities
import org.kodein.di.instance
import kotlin.math.sqrt

class SqrtModule : AbstractModule<Nothing, Double, Double>() {
    override val capabilities = error("Trying to access capabilities of a module that does not have any")
    override fun invoke(input: Double): Double = sqrt(input)
}

class SumModule : AbstractModule<Nothing, Double, Double>() {
    override val capabilities = error("Trying to access capabilities of a module that does not have any")
    override fun invoke(input: Double): Double = input + input
}

class EmbeddedSensor : Capabilities
class SensorModule : AbstractModule<EmbeddedSensor, Unit, Double>() {
    override val capabilities: EmbeddedSensor by instance()
    override fun invoke(input: Unit): Double = 42.0
}

class ProducerModule : AbstractModule<Nothing, Unit, Double>() {
    override val capabilities = error("Trying to access capabilities of a module that does not have any")
    override fun invoke(input: Unit): Double = 42.0
}

sealed interface UnionCapabilities : Capabilities {
    data object A : UnionCapabilities
    data object B : UnionCapabilities
}

class SelectionModule : AbstractModule<UnionCapabilities, Unit, Double>() {
    override val capabilities: UnionCapabilities by instance()
    override fun invoke(input: Unit): Double = when (capabilities) {
        is UnionCapabilities.A -> 42.0
        is UnionCapabilities.B -> 24.0
    }
}

interface CapOne : Capabilities
interface CapTwo : Capabilities
class IntersectionCapabilities : CapOne, CapTwo
class MultiCapabilityModule : AbstractModule<IntersectionCapabilities, Unit, Double>() {
    override val capabilities: IntersectionCapabilities by instance()
    override fun invoke(input: Unit): Double = 42.0
}
