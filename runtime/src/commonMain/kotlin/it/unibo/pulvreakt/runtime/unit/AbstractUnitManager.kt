package it.unibo.pulvreakt.runtime.unit

import it.unibo.pulvreakt.core.reconfiguration.Reconfigurator
import it.unibo.pulvreakt.core.reconfiguration.component.ComponentModeReconfigurator
import it.unibo.pulvreakt.runtime.unit.component.ComponentManager
import org.kodein.di.DI
import org.kodein.di.instance

internal abstract class AbstractUnitManager : UnitManager {
    final override lateinit var di: DI
    protected val componentManager by instance<ComponentManager>()
    protected val reconfigurator by instance<Reconfigurator>()
    protected val componentModeReconfigurator by instance<ComponentModeReconfigurator>()

    final override fun setupInjector(kodein: DI) {
        di = kodein
    }
}
