import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

kotlin {
    jvm {
        apply(plugin = "com.github.johnrengelman.shadow")

        tasks {
            register<ShadowJar>("behaviourJar") {
                archiveClassifier.set("all")
                archiveBaseName.set("behaviour")
                duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                manifest {
                    attributes("Main-Class" to "it.nicolasfarabegoli.pulverization.example01.MyBehaviourMainKt")
                }
                val main by kotlin.jvm().compilations
                from(main.output)
                configurations += main.compileDependencyFiles as Configuration
                configurations += main.runtimeDependencyFiles as Configuration
            }
            register<ShadowJar>("communicationJar") {
                archiveClassifier.set("all")
                archiveBaseName.set("communication")
                duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                manifest {
                    attributes("Main-Class" to "it.nicolasfarabegoli.pulverization.example01.MyCommunicationMainKt")
                }
                val main by kotlin.jvm().compilations
                from(main.output)
                configurations += main.compileDependencyFiles as Configuration
                configurations += main.runtimeDependencyFiles as Configuration
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":core"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("com.rabbitmq:amqp-client:5.9.0")
                implementation("org.slf4j:slf4j-simple:1.7.29")
                implementation("com.uchuhimo:konf:1.1.2")
            }
        }
        val jvmTest by getting { }
    }
}
