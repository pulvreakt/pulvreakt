package it.unibo.pulvreakt.core.component

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldNotBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ComponentTypeTest : StringSpec(
    {
        "The ComponentType should have a delegate for its creation" {
            val type1 by ComponentTypeDelegate<Int>()
            val type2 by ComponentTypeDelegate<String>()
            val type3 by ComponentTypeDelegate<String>()

            type1 shouldNotBeEqual type2
            type1 shouldNotBeEqual type3
            type2 shouldNotBeEqual type3
        }
        "The ComponentType should be serializable and de-serializable" {
            val component1 by ComponentTypeDelegate<Int>()
            val component2 by ComponentTypeDelegate<Int>()
            val serialized = Json.encodeToString(component1)
            val deserialized = Json.decodeFromString<ComponentType<Int>>(serialized)

            component1 shouldNotBe component2
            component1 shouldBe deserialized
            component2 shouldNotBe deserialized
        }
        "Two ComponentType must be equals if they have the same name and type" {
            fun stringComponent(): ComponentType<String> {
                val component2 by ComponentTypeDelegate<String>()
                return component2
            }

            val component1 by ComponentTypeDelegate<Int>()
            val component2 by ComponentTypeDelegate<String>()

            component1 shouldNotBe component2
            component2 shouldBe stringComponent()
        }
        "Two ComponentType must be different if the have the same name but different type" {
            fun stringComponent(): ComponentType<String> {
                val component by ComponentTypeDelegate<String>()
                return component
            }

            val component by ComponentTypeDelegate<Int>()
            component shouldNotBe stringComponent()
        }
    },
)
