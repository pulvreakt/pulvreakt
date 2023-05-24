package it.unibo.pulvreakt.core.utils

import org.koin.core.KoinApplication
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
internal object PulvreaktKoinContext {
    var koinApp: KoinApplication? = null
}
