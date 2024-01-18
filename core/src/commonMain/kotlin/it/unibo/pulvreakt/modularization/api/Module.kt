package it.unibo.pulvreakt.modularization.api

import org.kodein.di.DI
import org.kodein.di.DIAware

interface Module<out C : Capabilities, in Input, out Output> : DIAware {
    val capabilities: C
    operator fun invoke(input: Input): Output

    fun setupInjector(di: DI)
}

abstract class AbstractModule<out C : Capabilities, in Input, out Output> : Module<C, Input, Output> {
    override lateinit var di: DI

    override fun setupInjector(di: DI) {
        this.di = di
    }
}
