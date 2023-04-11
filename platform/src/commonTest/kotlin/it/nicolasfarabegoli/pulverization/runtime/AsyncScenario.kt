package it.nicolasfarabegoli.pulverization.runtime

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import it.nicolasfarabegoli.pulverization.dsl.Device
import it.nicolasfarabegoli.pulverization.dsl.getDeviceConfiguration
import it.nicolasfarabegoli.pulverization.dsl.pulverizationConfig
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State
import it.nicolasfarabegoli.pulverization.runtime.dsl.BehaviourFixture
import it.nicolasfarabegoli.pulverization.runtime.dsl.CommPayload
import it.nicolasfarabegoli.pulverization.runtime.dsl.CommunicationFixture
import it.nicolasfarabegoli.pulverization.runtime.dsl.PulverizationPlatformScope.Companion.behaviourLogic
import it.nicolasfarabegoli.pulverization.runtime.dsl.PulverizationPlatformScope.Companion.communicationLogic
import it.nicolasfarabegoli.pulverization.runtime.dsl.PulverizationPlatformScope.Companion.stateLogic
import it.nicolasfarabegoli.pulverization.runtime.dsl.StateFixture
import it.nicolasfarabegoli.pulverization.runtime.dsl.StatePayload
import it.nicolasfarabegoli.pulverization.runtime.dsl.pulverizationPlatform
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first

class AsyncScenario : FreeSpec(
    {
        "Instantiating the platform" - {
            "should work as expected" {
                val config = pulverizationConfig {
                    logicalDevice("device-1") {
                        Behaviour and State and Communication deployableOn Device
                    }
                }
                val platform = pulverizationPlatform(config.getDeviceConfiguration("device-1")!!) {
                    withContext {
                        deviceID("1")
                    }
                    behaviourLogic(BehaviourFixture()) { b, sr, cr, _, _ ->
                        val stateDefer = async { sr.receiveFromComponent().first() }
                        val commDefer = async { cr.receiveFromComponent().first() }
                        val st: StatePayload = stateDefer.await()
                        val comm: CommPayload = commDefer.await()
                        st shouldBe StatePayload(0)
                        comm shouldBe CommPayload(3)
                        val r = b(st, listOf(comm), 1)
                        sr.sendToComponent(r.newState)
                        cr.sendToComponent(r.newExport)
                    }
                    stateLogic(StateFixture()) { s, br ->
                        br.sendToComponent(s.get())
                        val newState: StatePayload = br.receiveFromComponent().first()
                        newState shouldBe StatePayload(0)
                        s.update(newState)
                        return@stateLogic
                    }
                    communicationLogic(CommunicationFixture()) { c, br ->
                        br.sendToComponent(CommPayload(3))
                        c.send(br.receiveFromComponent().first())
                        return@communicationLogic
                    }
                }
                val jobs = platform.start()
                jobs.size shouldBe 3
                jobs.forEach { it.join() }
                platform.stop()
            }
        }
    },
)
