@file:Suppress("UndocumentedPublicFunction", "UnusedPrivateMember")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

fun ShadowJar.genericJarConfig(jarName: String, mainClass: String) {
    archiveClassifier.set("all")
    archiveBaseName.set(jarName)
    archiveVersion.set("")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude("META-INF/*.SF", "META-INF/*.RSA", "META-INF/*.DSA")
    manifest {
        attributes("Main-Class" to mainClass)
    }
    val main by kotlin.jvm().compilations
    from(main.output)
    configurations += main.compileDependencyFiles as Configuration
    configurations += main.runtimeDependencyFiles as Configuration
}

kotlin {
    jvm {
        apply(plugin = "com.github.johnrengelman.shadow")

        tasks {
            register("generateJars") {
                dependsOn("sensorsJar", "behaviourJar", "communicationJar")
            }
            register<ShadowJar>("behaviourJar") {
                genericJarConfig("behaviour", "it.nicolasfarabegoli.pulverization.rabbitmq.MainBehaviourKt")
            }
            register<ShadowJar>("communicationJar") {
                genericJarConfig("communication", "it.nicolasfarabegoli.pulverization.rabbitmq.MainCommunicationKt")
            }
            register<ShadowJar>("sensorsJar") {
                genericJarConfig("sensors", "it.nicolasfarabegoli.pulverization.rabbitmq.MainSensorsKt")
            }
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core"))
                implementation(project(":rabbitmq-platform"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
            }
        }

        jvmMain {
            dependencies {
                implementation(rootProject.libs.kotlinx.coroutines.reactive)
                implementation(rootProject.libs.kotlinx.coroutines.reactor)
            }
        }
    }
    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        enabled = false
    }
}
