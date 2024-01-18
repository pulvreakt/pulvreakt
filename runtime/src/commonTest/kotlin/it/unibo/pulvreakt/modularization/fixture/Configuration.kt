package it.unibo.pulvreakt.modularization.fixture

import it.unibo.pulvreakt.modularization.dsl.modularization

val configuration = modularization {
    TemperatureConditioner() connectedTo SwitchModule()
}
