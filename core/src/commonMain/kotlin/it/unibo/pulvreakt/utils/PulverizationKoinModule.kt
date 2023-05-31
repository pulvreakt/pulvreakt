package it.unibo.pulvreakt.utils

import org.koin.core.KoinApplication
import kotlin.native.concurrent.ThreadLocal

/**
 * Represents the Koin module of the Pulverization framework.
 * [koinApp] is the Koin application that is used to inject dependencies.
 */
@ThreadLocal
@Deprecated(message = "Use the internal Kodein module. Do not use this object explicitly")
object PulverizationKoinModule {
    var koinApp: KoinApplication? = null
}
