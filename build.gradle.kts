@file:Suppress("UndocumentedPublicFunction", "UnusedPrivateMember")

import io.gitlab.arturbosch.detekt.Detekt
import org.danilopianini.gradle.mavencentral.JavadocJar
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toLowerCaseAsciiOnly

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
    alias(libs.plugins.gitSemVer)
    alias(libs.plugins.sonarqube)
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

fun KotlinNativeTarget.configureNativeTarget() {
    compilations["main"].defaultSourceSet.dependsOn(kotlin.sourceSets["nativeMain"])
    compilations["test"].defaultSourceSet.dependsOn(kotlin.sourceSets["nativeTest"])
    binaries {
        sharedLib()
        staticLib()
        "main".let {
            executable { entryPoint = it }
        }
    }
}

allprojects {
    with(rootProject.libs.plugins) {
        apply(plugin = kotlin.multiplatform.id)
        apply(plugin = kotest.multiplatform.id)
        apply(plugin = detekt.id)
        apply(plugin = ktlint.id)
        apply(plugin = dokka.id)
        apply(plugin = kover.id)
        apply(plugin = publishOnCentral.id)
        apply(plugin = gitSemVer.id)
        apply(plugin = kotlinx.serialization.id)
        apply(plugin = sonarqube.id)
    }

    repositories {
        google()
        mavenCentral()
    }

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

        targets.all {
            compilations.all {
                kotlinOptions {
                    allWarningsAsErrors = true
                }
            }
        }

        when (val hostOs = System.getProperty("os.name").trim().toLowerCaseAsciiOnly()) {
            "linux" -> {
                linuxX64().configureNativeTarget()
                // linuxArm64().configureNativeTarget()
                // linuxArm32Hfp().configureNativeTarget()
                // linuxMips32().configureNativeTarget()
                // linuxMipsel32().configureNativeTarget()
            }

            "mac os x" -> {
                macosX64().configureNativeTarget()
                macosArm64().configureNativeTarget()
                iosArm64().configureNativeTarget()
                iosArm32().configureNativeTarget()
                iosSimulatorArm64().configureNativeTarget()
                watchosArm64().configureNativeTarget()
                watchosArm32().configureNativeTarget()
                watchosSimulatorArm64().configureNativeTarget()
                tvosArm64().configureNativeTarget()
                tvosSimulatorArm64().configureNativeTarget()
            }

            "windows" -> {
                mingwX64().configureNativeTarget()
                // mingwX86().configureNativeTarget()
            }

            else -> throw GradleException(
                "Host OS '$hostOs' is not supported in Kotlin/Native.",
            )
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

    detekt {
        parallel = true
        buildUponDefaultConfig = true
        config = files("${rootDir.path}/detekt.yml")
        source = files(kotlin.sourceSets.map { it.kotlin.sourceDirectories })
    }
    group = "it.nicolasfarabegoli.${rootProject.name}"
}

subprojects {
    with(rootProject.libs.plugins) {
        apply(plugin = publishOnCentral.id)
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
            publications.withType<MavenPublication>().configureEach {
                if ("OSSRH" !in name) {
                    artifact(tasks.javadocJar)
                }
            }
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
            excludes += listOf(":", ":examples:example-03")
        }
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "nicolasfara_pulverization-framework")
        property("sonar.organization", "nicolasfara")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
