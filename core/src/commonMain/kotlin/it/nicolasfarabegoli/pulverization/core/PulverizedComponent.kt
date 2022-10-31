package it.nicolasfarabegoli.pulverization.core

import it.nicolasfarabegoli.pulverization.component.Context
import org.koin.core.component.KoinComponent

/**
 * High level concept of pulverized component.
 */
interface PulverizedComponent : KoinComponent {
    /**
     * The context in which the component live.
     */
    val context: Context
}
