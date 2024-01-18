package it.unibo.pulvreakt.modularization.api

import it.unibo.pulvreakt.modularization.dsl.data.ModularizedSystem
import kotlinx.serialization.KSerializer
import org.kodein.di.DI

@PublishedApi
internal class DeploymentUnitImpl<C : Capabilities>(
    override val di: DI,
    override val runningHost: Host<C>,
    private val scheduler: Scheduler,
    private val hostDiscover: HostDiscover,
    private val configuration: ModularizedSystem,
) : DeploymentUnit<C> {
    override fun <Input : Any, Output : Any> registerModule(
        module: Module<*, Input, Output>,
        inputKSerializer: KSerializer<Input>,
        outputKSerializer: KSerializer<Output>,
    ) {
        module.setupInjector(di)
        TODO()
    }

    override suspend fun setup(): Outcome {
        TODO("Not yet implemented")
    }

    override suspend fun teardown(): Outcome {
        TODO("Not yet implemented")
    }

    override suspend fun start(): Outcome {
        TODO("Not yet implemented")
    }

    override suspend fun stop(): Outcome {
        TODO("Not yet implemented")
    }
}
