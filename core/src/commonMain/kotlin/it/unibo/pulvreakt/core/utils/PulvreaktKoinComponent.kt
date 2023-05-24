package it.unibo.pulvreakt.core.utils

import org.koin.core.Koin
import org.koin.core.component.KoinComponent

abstract class PulvreaktKoinComponent : KoinComponent {
    override fun getKoin(): Koin = PulvreaktKoinContext.koinApp?.koin ?: error("Pulvreakt koin application not defined")
}
