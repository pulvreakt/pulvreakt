package it.unibo.pulvreakt.runtime.unit

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.api.context.Context
import it.unibo.pulvreakt.api.context.IntId.Companion.toId
import it.unibo.pulvreakt.api.infrastructure.Host
import it.unibo.pulvreakt.api.communication.protocol.Protocol
import it.unibo.pulvreakt.api.reconfiguration.component.ComponentModeReconfigurator
import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.runtime.component.ComponentManager
import it.unibo.pulvreakt.runtime.component.SimpleComponentManager
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

class UnitManagerTest : StringSpec(
    {
        val cap by Capability
        val diModule = DI {
            bind<ComponentManager> { singleton { SimpleComponentManager() } }
            bind<Protocol> { singleton { TestProtocol() } }
            bind<Context> { singleton { Context(1.toId(), Host("foo", cap)) } }
            bind<ComponentModeReconfigurator> { singleton { TestComponentModeReconfigurator() } }
        }
        "The unit manager should not raise an error during initialization if properly configured" {
            val deviceConfig = configuration.getOrNull()?.get("dev") ?: error("Configuration should be valid")
            val unitManager = UnitManager(deviceConfig).apply { setupInjector(diModule) }
            unitManager.initialize().isRight() shouldBe true
            unitManager.start().isRight() shouldBe true
            unitManager.stop().isRight() shouldBe true
        }
    },
)
