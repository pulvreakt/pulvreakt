import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

kotlin {
    jvm {
        apply(plugin = "com.github.johnrengelman.shadow")

        tasks {
            val shadowCreate by creating(ShadowJar::class) {
                manifest {
                    attributes("Main-Class" to "it.nicolasfarabegoli.MainKt")
                }
                archiveClassifier.set("all")
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
                implementation("io.ktor:ktor-network:2.1.1")
            }
        }
    }
}
