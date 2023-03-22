import org.danilopianini.gradle.mavencentral.JavadocJar
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.util.*

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotest.multiplatform)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kover)
    alias(libs.plugins.taskTree)
    alias(libs.plugins.publishOnCentral)
    alias(libs.plugins.gitSemVer)
}

val Provider<PluginDependency>.id get() = get().pluginId

allprojects {
    group = "it.nicolasfarabegoli.${rootProject.name}"

    with(rootProject.libs.plugins) {
        apply(plugin = kotlin.multiplatform.id)
        apply(plugin = kotest.multiplatform.id)
        apply(plugin = kotlin.qa.id)
        apply(plugin = dokka.id)
        apply(plugin = kover.id)
        apply(plugin = publishOnCentral.id)
        apply(plugin = kotlinx.serialization.id)
        apply(plugin = publishOnCentral.id)
        apply(plugin = gitSemVer.id)
    }

    repositories {
        google()
        mavenCentral()
    }

    val os = OperatingSystem.current()

    kotlin {
        jvm {
            compilations.all {
                kotlinOptions.jvmTarget = "1.8"
            }
            testRuns["test"].executionTask.configure {
                useJUnitPlatform()
                filter {
                    isFailOnNoMatchingTests = false
                }
                testLogging {
                    showExceptions = true
                    events = setOf(
                        org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                        org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                    )
                    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                }
            }
        }

        sourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(rootProject.libs.koin.core)
                    implementation(rootProject.libs.kotlinx.coroutines.core)
                }
            }
            val commonTest by getting {
                dependencies {
                    implementation(rootProject.libs.bundles.kotest.common)
                    implementation(rootProject.libs.koin.test)
                }
            }
            val jvmTest by getting {
                dependencies {
                    implementation(rootProject.libs.kotest.runner.junit5)
                }
            }
            val nativeMain by creating {
                dependsOn(commonMain)
            }
            val nativeTest by creating {
                dependsOn(commonTest)
            }
        }

        js(IR) {
            browser()
            nodejs()
            binaries.library()
        }

        val nativeSetup: KotlinNativeTarget.() -> Unit = {
            compilations["main"].defaultSourceSet.dependsOn(kotlin.sourceSets["nativeMain"])
            compilations["test"].defaultSourceSet.dependsOn(kotlin.sourceSets["nativeTest"])
            binaries {
                sharedLib()
                staticLib()
            }
        }

        linuxX64(nativeSetup)
        // linuxArm64(nativeSetup)

        mingwX64(nativeSetup)

        macosX64(nativeSetup)
        macosArm64(nativeSetup)
        ios(nativeSetup)
        watchos(nativeSetup)
        tvos(nativeSetup)

        targets.all {
            compilations.all {
                kotlinOptions {
                    allWarningsAsErrors = true
                }
            }
        }

        signing {
            if (System.getenv("CI") == "true") {
                val signingKey: String? by project
                val signingPassword: String? by project
                useInMemoryPgpKeys(signingKey, signingPassword)
            }
        }

        publishOnCentral {
            projectUrl.set("https://github.com/nicolasfara/${rootProject.name}")
            projectLongName.set("Framework enabling pulverization")
            projectDescription.set("A framework to create a pulverized system")
            licenseName.set("MIT License")
            licenseUrl.set("https://opensource.org/license/mit/")
            repository("https://maven.pkg.github.com/nicolasfara/${rootProject.name}".lowercase(Locale.getDefault())) {
                user.set("nicolasfara")
                password.set(System.getenv("GITHUB_TOKEN"))
            }
            publishing {
                publications {
                    withType<MavenPublication>().configureEach {
                        if ("OSSRH" !in name) {
                            artifact(tasks.javadocJar)
                        }
                        pom {
                            scm {
                                connection.set("git:git@github.com:nicolasfara/${rootProject.name}")
                                developerConnection.set("git:git@github.com:nicolasfara/${rootProject.name}")
                                url.set("https://github.com/nicolasfara/${rootProject.name}")
                            }
                            developers {
                                developer {
                                    name.set("Nicolas Farabegoli")
                                    email.set("nicolas.farabegoli@gmail.com")
                                    url.set("https://www.nicolasfarabegoli.it/")
                                }
                            }
                        }
                    }
                }
            }
        }

        // Disable cross compilation
        val excludeTargets = when {
            os.isLinux -> kotlin.targets.filterNot { "linux" in it.name }
            os.isWindows -> kotlin.targets.filterNot { "mingw" in it.name }
            os.isMacOsX -> kotlin.targets.filter { "linux" in it.name || "mingw" in it.name }
            else -> emptyList()
        }.mapNotNull { it as? KotlinNativeTarget }

        configure(excludeTargets) {
            compilations.configureEach {
                cinterops.configureEach { tasks[interopProcessingTaskName].enabled = false }
                compileTaskProvider.get().enabled = false
                tasks[processResourcesTaskName].enabled = false
            }
            binaries.configureEach { linkTask.enabled = false }

            mavenPublication {
                tasks.withType<AbstractPublishToMaven>()
                    .configureEach { onlyIf { publication != this@mavenPublication } }
                tasks.withType<GenerateModuleMetadata>()
                    .configureEach { onlyIf { publication.get() != this@mavenPublication } }
            }
        }
    }
}

tasks {
    dokkaJavadoc {
        enabled = false
    }

    withType<JavadocJar>().configureEach {
        val dokka = dokkaHtml.get()
        dependsOn(dokka)
        from(dokka.outputDirectory)
    }

    // Prevent publishing the root project (since is empty)
    withType<AbstractPublishToMaven>().configureEach {
        enabled = false
    }
    withType<GenerateModuleMetadata>().configureEach {
        enabled = false
    }
}

koverMerged {
    enable()
    htmlReport { onCheck.set(true) }
    xmlReport { onCheck.set(true) }
    filters {
        projects {
            excludes += listOf(":")
        }
    }
}
