package it.unibo.pulvreakt.runtime.unit

import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.api.reconfiguration.Reconfigurator
import it.unibo.pulvreakt.api.reconfiguration.component.ComponentModeReconfigurator
import it.unibo.pulvreakt.runtime.component.ComponentManager
import org.kodein.di.DI
import org.kodein.di.instance

internal abstract class AbstractUnitManager : UnitManager {
    final override lateinit var di: DI
    protected val componentManager by instance<ComponentManager>()
    protected val reconfigurator by instance<Reconfigurator>()
    protected val componentModeReconfigurator by instance<ComponentModeReconfigurator>()
    protected val context by instance<Context<*>>()

    final override fun setupInjector(kodein: DI) {
        di = kodein
    }
}
