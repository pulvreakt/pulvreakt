@file:Suppress("UndocumentedPublicFunction", "UnusedPrivateMember")

import io.gitlab.arturbosch.detekt.Detekt
import org.danilopianini.gradle.mavencentral.JavadocJar
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotest.multiplatform)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.dokka)
    alias(libs.plugins.shadow)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kover)
    alias(libs.plugins.taskTree)
    alias(libs.plugins.conventionalCommits)
    alias(libs.plugins.publishOnCentral)
    alias(libs.plugins.git.sensitive.semver)
}

val Provider<PluginDependency>.id get() = get().pluginId

tasks {
    create("uploadAll") {
        description = "Upload all artifacts"
        group = "publishing"
        dependsOn(
            "core:uploadAllPublicationsToMavenCentralNexus",
            "platform:uploadAllPublicationsToMavenCentralNexus",
            "rabbitmq-platform:uploadAllPublicationsToMavenCentralNexus",
        )
    }
    create("uploadAllGithub") {
        description = "Upload all artifacts to github"
        group = "publishing"
        dependsOn(
            "core:publishKotlinMultiplatformPublicationToGithubRepository",
            "platform:publishKotlinMultiplatformPublicationToGithubRepository",
            "rabbitmq-platform:publishKotlinMultiplatformPublicationToGithubRepository",
        )
    }
}

allprojects {
    group = "it.nicolasfarabegoli.${rootProject.name}"

    with(rootProject.libs.plugins) {
        apply(plugin = kotlin.multiplatform.id)
        apply(plugin = kotest.multiplatform.id)
        apply(plugin = detekt.id)
        apply(plugin = ktlint.id)
        apply(plugin = dokka.id)
        apply(plugin = kover.id)
        apply(plugin = publishOnCentral.id)
        apply(plugin = kotlinx.serialization.id)
        apply(plugin = publishOnCentral.id)
        apply(plugin = git.sensitive.semver.id)
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
        // linuxArm32Hfp(nativeSetup)
        // linuxMips32(nativeSetup)
        // linuxMipsel32(nativeSetup)

        macosX64(nativeSetup)
        macosArm64(nativeSetup)
        ios(nativeSetup)
        // iosArm32(nativeSetup)
        // iosSimulatorArm64(nativeSetup)
        watchos(nativeSetup)
        // watchosArm64(nativeSetup)
        // watchosArm32(nativeSetup)
        // watchosSimulatorArm64(nativeSetup)
        tvos(nativeSetup)
        // tvosArm64(nativeSetup)
        // tvosSimulatorArm64(nativeSetup)

        mingwX64(nativeSetup)
        // mingwX86()

        targets.all {
            compilations.all {
                kotlinOptions {
                    allWarningsAsErrors = true
                }
            }
        }

        tasks.dokkaJavadoc {
            enabled = false
        }
        tasks.withType<Detekt>().configureEach {
            exclude("**/*Test.kt", "**/*Fixtures.kt")
        }
        tasks.withType<JavadocJar>().configureEach {
            val dokka = tasks.dokkaHtml.get()
            dependsOn(dokka)
            from(dokka.outputDirectory)
        }

        signing {
            if (System.getenv("CI") == "true") {
                val signingKey: String? by project
                val signingPassword: String? by project
                useInMemoryPgpKeys(signingKey, signingPassword)
            }
        }
        publishOnCentral {
            projectUrl.set("https://github.com/nicolasfara/rabbitmq-platform")
            projectLongName.set("Framework enabling pulverization")
            projectDescription.set("A framework to create a pulverized system")
            repository("https://maven.pkg.github.com/nicolasfara/${rootProject.name}".toLowerCase()) {
                user.set("nicolasfara")
                password.set(System.getenv("GITHUB_TOKEN"))
            }
        }
        publishing.publications.withType<MavenPublication>().configureEach {
            pom {
                scm {
                    connection.set("git:git@github.com:nicolasfara/rabbitmq-platform")
                    developerConnection.set("git:git@github.com:nicolasfara/rabbitmq-platform")
                    url.set("https://github.com/nicolasfara/rabbitmq-platform")
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
        publishing {
            publications {
                val publicationsFromMainHost = listOf(jvm(), js()).map { it.name } + "kotlinMultiplatform"
                matching { it.name in publicationsFromMainHost }.all {
                    val targetPublication = this@all
                    tasks.withType<AbstractPublishToMaven>()
                        .matching { it.publication == targetPublication }
                        .configureEach { onlyIf { findProperty("isMainHost") == "true" } }
                }
                publications.withType<MavenPublication>().configureEach {
                    if ("OSSRH" !in name) {
                        artifact(tasks.javadocJar)
                    }
                }
            }
        }

        // Disable cross compilation
        plugins.withId("org.jetbrains.kotlin.multiplatform") {
            afterEvaluate {
                val excludeTargets = when {
                    os.isLinux -> kotlin.targets.filter { "linux" !in it.name }
                    os.isWindows -> kotlin.targets.filter { "mingw" !in it.name }
                    os.isMacOsX -> kotlin.targets.filter { "linux" in it.name || "mingw" in it.name }
                    else -> emptyList()
                }.mapNotNull { it as? KotlinNativeTarget }

                configure(excludeTargets) {
                    compilations.all {
                        cinterops.all { tasks[interopProcessingTaskName].enabled = false }
                        compileKotlinTask.enabled = false
                        tasks[processResourcesTaskName].enabled = false
                    }
                    binaries.all { linkTask.enabled = false }

                    mavenPublication {
                        val publicationToDisable = this
                        tasks.withType<AbstractPublishToMaven>().all { onlyIf { publication != publicationToDisable } }
                        tasks.withType<GenerateModuleMetadata>()
                            .all { onlyIf { publication.get() != publicationToDisable } }
                    }
                }
            }
        }

        detekt {
            parallel = true
            buildUponDefaultConfig = true
            config = files("${rootDir.path}/detekt.yml")
            source = files(kotlin.sourceSets.map { it.kotlin.sourceDirectories })
        }
    }
}

koverMerged {
    enable()
    htmlReport {
        onCheck.set(true)
    }
    xmlReport {
        onCheck.set(true)
    }
    filters {
        projects {
            excludes += listOf(":" /*, ":examples:example-03"*/)
        }
    }
}
