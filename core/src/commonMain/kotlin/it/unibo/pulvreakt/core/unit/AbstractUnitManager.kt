package it.unibo.pulvreakt.core.unit

import it.unibo.pulvreakt.core.reconfiguration.Reconfigurator
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class AbstractUnitManager : UnitManager, KoinComponent {
    protected val reconfigurator by inject<Reconfigurator>()
}
