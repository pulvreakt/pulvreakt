package it.unibo.pulvreakt.runtime.unit

import it.unibo.pulvreakt.runtime.RuntimeContext

internal abstract class AbstractUnitManager : UnitManager {
    protected lateinit var context: RuntimeContext<*>
    protected val componentManager by lazy { context.componentManager }
//    protected val reconfigurator by instance<Reconfigurator>()
//    protected val componentModeReconfigurator by instance<ComponentModeReconfigurator>()

    final override fun setupContext(context: RuntimeContext<*>) {
        this.context = context
    }
}
