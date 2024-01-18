package it.unibo.pulvreakt.modularization.fixture

import it.unibo.pulvreakt.modularization.api.AbstractModule
import org.kodein.di.instance

class TemperatureConditioner : AbstractModule<TemperatureSensor, Unit, Double>() {
    override val capabilities by instance<TemperatureSensor>()
    override fun invoke(input: Unit): Double = 0.0
}

class SwitchModule : AbstractModule<UnionCapabilities, Double, Double>() {
    override val capabilities: UnionCapabilities by instance()
    override fun invoke(input: Double): Double = when (capabilities) {
        is UnionCapabilities.A -> input * 2
        is UnionCapabilities.B -> input / 2
    }
}
