package it.nicolasfarabegoli.pulverization.utils

import org.koin.core.KoinApplication
import kotlin.native.concurrent.ThreadLocal

/**
 * Module to load all the dependencies in the framework.
 * Relying on Koin as dependency injection framework.
 */
@ThreadLocal
object PulverizationKoinModule {
    /**
     * The koin app.
     */
    var koinApp: KoinApplication? = null
}
