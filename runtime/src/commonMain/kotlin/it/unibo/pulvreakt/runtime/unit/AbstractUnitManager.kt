package it.unibo.pulvreakt.core.unit

import it.unibo.pulvreakt.core.reconfiguration.Reconfigurator
import it.unibo.pulvreakt.core.unit.manager.ComponentManager
import it.unibo.pulvreakt.core.utils.PulvreaktInjected
import org.kodein.di.DI
import org.kodein.di.instance

internal abstract class AbstractUnitManager : UnitManager, PulvreaktInjected {
    final override lateinit var di: DI
    protected val reconfigurator by instance<Reconfigurator>()
    protected val componentManager by instance<ComponentManager>()

    final override fun setupInjector(kodein: DI) { di = kodein }
}
