import org.danilopianini.gradle.mavencentral.DocStyle
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

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
    alias(libs.plugins.hugo)
}

val Provider<PluginDependency>.id: String get() = get().pluginId

allprojects {
    group = "it.unibo.${rootProject.name}"

    with(rootProject.libs.plugins) {
        apply(plugin = kotlin.multiplatform.id)
        apply(plugin = kotest.multiplatform.id)
        apply(plugin = kotlin.qa.id)
        apply(plugin = dokka.id)
        apply(plugin = kover.id)
        apply(plugin = publishOnCentral.id)
        apply(plugin = kotlinx.serialization.id)
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
                    implementation(rootProject.libs.kotlinx.coroutines.core)
                    implementation(rootProject.libs.kotlinx.serialization.json)
                    api(rootProject.libs.koin.core)
                    api(rootProject.libs.bundles.kermit)
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
            projectUrl.set("https://github.com/pulvreakt/${rootProject.name}")
            projectLongName.set("PulvReAKt")
            projectDescription.set("A framework to create a pulverized system")
            licenseName.set("MIT License")
            licenseUrl.set("https://opensource.org/license/mit/")
            docStyle.set(DocStyle.HTML)
            publishing {
                publications {
                    withType<MavenPublication>().configureEach {
                        if ("OSSRH" !in name) {
                            artifact(tasks.javadocJar)
                        }
                        pom {
                            scm {
                                connection.set("git:git@github.com:pulvreakt/${rootProject.name}")
                                developerConnection.set("git:git@github.com:pulvreakt/${rootProject.name}")
                                url.set("https://github.com/pulvreakt/${rootProject.name}")
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

val websiteDir = File(buildDir, "website")

hugo {
    version = Regex("gohugoio/hugo@v([\\.\\-\\+\\w]+)")
        .find(file("deps-utils/action.yml").readText())!!.groups[1]!!.value
}

tasks {
    // Prevent publishing the root project (since is empty)
    withType<AbstractPublishToMaven>().configureEach {
        enabled = false
    }
    withType<GenerateModuleMetadata>().configureEach {
        enabled = false
    }

    hugoBuild {
        outputDirectory = websiteDir
    }

    mapOf("kdoc" to dokkaHtmlMultiModule)
        .mapValues { it.value.get() }
        .forEach { (folder, task) ->
            hugoBuild.configure { dependsOn(task) }
            val copyTask = register<Copy>("copy${folder.capitalized()}IntoWebsite") {
                from(task.outputDirectory)
                into(File(websiteDir, "reference/$folder"))
            }
            hugoBuild.configure { finalizedBy(copyTask) }
        }
}
