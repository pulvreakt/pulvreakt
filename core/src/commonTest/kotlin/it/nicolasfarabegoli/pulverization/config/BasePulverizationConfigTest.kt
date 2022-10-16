package it.nicolasfarabegoli.pulverization.config

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly

class BasePulverizationConfigTest : FunSpec({
    context("Test the configuration DSL") {
        val d1 = LogicalDevice("1")
        val d2 = LogicalDevice("2")
        val d3 = LogicalDevice("3")
        test("The DSL should give the possibility to define a custom topology") {
            val configuration = pulverizationConfiguration {
                topology {
                    createLinks(d1, setOf(d2, d3))
                }
            }
            testNeighbours(configuration, d1, setOf(d2, d3))
            testNeighbours(configuration, d2, setOf(d1))
            testNeighbours(configuration, d3, setOf(d1))
        }
        test("The DSL should give the possibility to define a fully-connected network") {
            val configuration = pulverizationConfiguration {
                fullyConnectedTopology(d1, d2, d3)
            }
            testNeighbours(configuration, d1, setOf(d2, d3))
            testNeighbours(configuration, d2, setOf(d1, d3))
            testNeighbours(configuration, d3, setOf(d1, d2))
        }
    }
},)

internal fun <I> testNeighbours(
    config: PulverizationConfig<I>,
    device: LogicalDevice<I>,
    expectedNeighbours: Set<LogicalDevice<I>>,
) {
    config.topology[device]?.let {
        it shouldContainExactly expectedNeighbours
    } ?: error("The device $device should be in the configuration")
}
