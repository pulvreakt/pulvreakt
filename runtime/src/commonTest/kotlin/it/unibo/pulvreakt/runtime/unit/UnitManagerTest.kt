package it.unibo.pulvreakt.runtime.unit

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.api.communication.Channel
import it.unibo.pulvreakt.api.communication.LocalChannelManager
import it.unibo.pulvreakt.api.communication.protocol.Protocol
import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.api.infrastructure.Host
import it.unibo.pulvreakt.api.reconfiguration.component.ComponentModeReconfigurator
import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.runtime.RuntimeContext
import it.unibo.pulvreakt.runtime.communication.ChannelImpl
import it.unibo.pulvreakt.runtime.component.ComponentManager
import it.unibo.pulvreakt.runtime.component.SimpleComponentManager
import it.unibo.pulvreakt.runtime.utils.TestProtocol
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

class UnitManagerTest : StringSpec(
    {
        val cap by Capability
        val runtimeContext = object : RuntimeContext<Int> {
            override val context: Context<Int> = object : Context<Int> {
                override val deviceId: Int = 1
                override val executingHost: Host = Host("foo", cap)

                override fun getChannel(): Channel = ChannelImpl()

                override val channelManager: LocalChannelManager = LocalChannelManager()

                override fun protocolInstance(): Protocol = TestProtocol()

                override fun <T : Any> get(key: String): T? = null

                override fun <T : Any> set(key: String, value: T) { }
            }
            override val localChannelManager: LocalChannelManager = LocalChannelManager()
            override val componentManager: ComponentManager = SimpleComponentManager()
        }
        "The unit manager should not raise an error during initialization if properly configured" {
            val deviceConfig = configuration.getOrNull()?.get("dev") ?: error("Configuration should be valid")
            val unitManager = UnitManager(deviceConfig).apply { setupContext(runtimeContext) }
            unitManager.initialize().isRight() shouldBe true
            unitManager.start().isRight() shouldBe true
            unitManager.stop().isRight() shouldBe true
        }
    },
)
