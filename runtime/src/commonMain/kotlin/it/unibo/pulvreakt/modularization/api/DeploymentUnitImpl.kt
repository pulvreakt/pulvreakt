package it.unibo.pulvreakt.modularization.api

import it.unibo.pulvreakt.modularization.dsl.data.ModularizedSystem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.KSerializer
import org.kodein.di.DI
import org.kodein.di.provider

@PublishedApi
internal class DeploymentUnitImpl<C : Capabilities>(
    override val di: DI,
    override val runningHost: Host<C>,
    private val scheduler: Scheduler,
    private val hostDiscover: HostDiscover,
    private val configuration: ModularizedSystem,
) : DeploymentUnit<C> {
    private val registeredModules: MutableMap<Module<*, *, *>, Pair<KSerializer<*>, KSerializer<*>>> = mutableMapOf()
    private val offloadedModules: MutableMap<Module<*, *, *>, Pair<KSerializer<*>, KSerializer<*>>> = mutableMapOf()
    private val channelProvider by provider<Channel<ByteArray, String>>()
    private val scope = CoroutineScope(Dispatchers.Default)

    override fun <Input : Any, Output : Any> registerModule(
        module: Module<*, Input, Output>,
        inputKSerializer: KSerializer<Input>,
        outputKSerializer: KSerializer<Output>,
    ) {
        module.setupInjector(di)
        when {
            module.capabilities == runningHost.exposedCapabilities -> registeredModules += module to (inputKSerializer to outputKSerializer)
            else -> offloadedModules += module to (inputKSerializer to outputKSerializer)
        }
    }

    override suspend fun setup(): Outcome {
        // Setup the channels
        // Launch host discover
        TODO("Not yet implemented")
    }

    override suspend fun teardown(): Outcome {
        TODO("Not yet implemented")
    }

    override suspend fun start(): Outcome {
        // Start the scheduler reacting on incoming messages
        TODO("Not yet implemented")
    }

    override suspend fun stop(): Outcome {
        TODO("Not yet implemented")
    }
}
