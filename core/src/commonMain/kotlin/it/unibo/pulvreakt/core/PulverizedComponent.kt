package it.unibo.pulvreakt.core

import it.unibo.pulvreakt.component.Context
import it.unibo.pulvreakt.dsl.model.ComponentType
import it.unibo.pulvreakt.utils.PulverizationKoinModule
import org.koin.core.Koin
import org.koin.core.component.KoinComponent

/**
 * High level concept of pulverized component.
 */
interface PulverizedComponent<S, C, SS, AS, O> : Initializable, KoinComponent {
    override fun getKoin(): Koin = PulverizationKoinModule.koinApp?.koin ?: error("No Koin app defined")

    /**
     * The context in which the component live.
     */
    val context: Context

    /**
     * The type of the component.
     */
    val componentType: ComponentType
}
