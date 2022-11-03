package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.config

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class RabbitmqPulverizationConfigTest : FunSpec(
    {
        context("Test the rabbitmq configuration DSL") {
            test("The DSL should configure the RabbitMQ parameters") {
                val config = pulverizationConfig {
                    logicalDevice("device-1") { }
                    rabbitmq {
                        setHostname("rmq")
                        setPort(1111)
                    }
                }
                config.hostname shouldBe "rmq"
                config.port shouldBe 1111
            }
        }
    },
)
