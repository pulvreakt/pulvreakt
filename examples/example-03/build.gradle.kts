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
                dependsOn("sensorsJar", "behaviourJar", "communicationJar", "stateJar")
            }
            register<ShadowJar>("stateJar") {
                genericJarConfig("state", "example.unit.StateUnitKt")
            }
            register<ShadowJar>("behaviourJar") {
                genericJarConfig("behaviour", "example.unit.BehaviourUnitKt")
            }
            register<ShadowJar>("communicationJar") {
                genericJarConfig("communication", "example.unit.CommunicationUnitKt")
            }
            register<ShadowJar>("sensorsJar") {
                genericJarConfig("sensors", "example.unit.SensorsUnitKt")
            }
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":runtime"))
                implementation(project(":rabbitmq-platform"))
            }
        }
    }
    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        enabled = false
    }
}
