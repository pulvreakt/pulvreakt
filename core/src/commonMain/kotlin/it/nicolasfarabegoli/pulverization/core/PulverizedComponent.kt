package it.nicolasfarabegoli.pulverization.core

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.dsl.model.ComponentType
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
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
