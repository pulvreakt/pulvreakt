package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.config

import io.kotest.core.spec.style.FunSpec

class RabbitmqPulverizationConfigTest : FunSpec(
    {
        context("Test the rabbitmq configuration DSL") {
//            val device1 = LogicalDevice("1".toID())
//            val device2 = LogicalDevice("2".toID())
//            test("If no rabbitmq specific configuration are given, the default one are used") {
//                val config = pulverizationConfiguration {
//                    fullyConnectedTopology(device1, device2)
//                }
//                with(config.brokerConfiguration) {
//                    hostname shouldBe "rabbitmq"
//                    port shouldBe 5672
//                }
//            }
//            test("When the rabbitmq configuration is specified the config should reflect the last configuration") {
//                val config = pulverizationConfiguration {
//                    rabbitmq {
//                        hostname = "rabbitmq.example.com"
//                        port = 15672
//                    }
//                    fullyConnectedTopology(device1, device2)
//                }
//                with(config.brokerConfiguration) {
//                    hostname shouldBe "rabbitmq.example.com"
//                    port shouldBe 15672
//                }
//            }
        }
    },
)
