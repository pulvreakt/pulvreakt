package it.unibo.pulvreakt.core.unit

import it.unibo.pulvreakt.core.reconfiguration.Reconfigurator
import it.unibo.pulvreakt.core.utils.PulvreaktKoinComponent
import org.koin.core.component.inject

abstract class AbstractUnitManager : UnitManager, PulvreaktKoinComponent() {
    protected val reconfigurator by inject<Reconfigurator>()
}
