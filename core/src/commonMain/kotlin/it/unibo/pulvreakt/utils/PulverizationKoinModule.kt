package it.unibo.pulvreakt.utils

import org.koin.core.KoinApplication
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
@Deprecated(message = "Use the internal Kodein module. Do not use this object explicitly")
object PulverizationKoinModule {
    var koinApp: KoinApplication? = null
}
