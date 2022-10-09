import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

fun ShadowJar.genericJarConfig(jarName: String, mainClass: String) {
    archiveClassifier.set("all")
    archiveBaseName.set(jarName)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
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
            register<ShadowJar>("sensorsJar") {
                genericJarConfig("sensors", "it.nicolasfarabegoli.pulverization.example01.MySensorsMainKt")
            }
            register<ShadowJar>("behaviourJar") {
                genericJarConfig("behaviour", "it.nicolasfarabegoli.pulverization.example01.MyBehaviourMainKt")
            }
            register<ShadowJar>("communicationJar") {
                genericJarConfig("communication", "it.nicolasfarabegoli.pulverization.example01.MyCommunicationMainKt")
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
