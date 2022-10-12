import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toLowerCaseAsciiOnly

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.ktlint.gradle)
    alias(libs.plugins.detekt.gradle)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.dokka)
    alias(libs.plugins.shadow)
    alias(libs.plugins.docker)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kover)
    alias(libs.plugins.taskTree)
}

val Provider<PluginDependency>.id get() = get().pluginId

allprojects {
    with(rootProject.libs.plugins) {
        apply(plugin = kotlin.multiplatform.id)
        apply(plugin = detekt.gradle.id)
        apply(plugin = ktlint.gradle.id)
        apply(plugin = dokka.id)
        apply(plugin = kover.id)
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

        js(IR) {
            browser()
            nodejs()
            binaries.library()
        }

        val hostOs = System.getProperty("os.name").trim().toLowerCaseAsciiOnly()
        val hostArch = System.getProperty("os.arch").trim().toLowerCaseAsciiOnly()
        val nativeTarget: (String, org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.() -> Unit) -> KotlinTarget =
            when (hostOs to hostArch) {
                "linux" to "aarch64" -> ::linuxArm64
                "linux" to "amd64" -> ::linuxX64
                "linux" to "arm", "linux" to "arm32" -> ::linuxArm32Hfp
                "linux" to "mips", "linux" to "mips32" -> ::linuxMips32
                "linux" to "mipsel", "linux" to "mips32el" -> ::linuxMipsel32
                "mac os x" to "aarch64" -> ::macosArm64
                "mac os x" to "amd64", "mac os x" to "x86_64" -> ::macosX64
                "windows" to "amd64", "windows server 2022" to "amd64" -> ::mingwX64
                "windows" to "x86" -> ::mingwX86
                else -> throw GradleException("Host OS '$hostOs' with arch '$hostArch' is not supported in Kotlin/Native.")
            }

        nativeTarget("native") {
            binaries {
                sharedLib()
                staticLib()
                // Remove if it is not executable
//                "main".let { executable ->
//                    executable {
//                        entryPoint = executable
//                    }
//                    // Enable wasm32
//                wasm32 {
//                    binaries {
//                        executable {
//                            entryPoint = executable
//                        }
//                    }
//                }
//                }
            }
        }

        sourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(rootProject.libs.kotlin.stdlib)
                    implementation(rootProject.libs.koin.core)
                }
            }
            val commonTest by getting {
                dependencies {
                    implementation(rootProject.libs.bundles.kotlin.testing.common)
                    implementation(rootProject.libs.koin.test)
                }
            }
            val jvmTest by getting {
                dependencies {
                    implementation(rootProject.libs.bundles.kotlin.testing.jvm)
                }
            }
            val nativeMain by getting {
                dependsOn(commonMain)
            }
            val nativeTest by getting {
                dependsOn(commonTest)
            }
        }
        targets.all {
            compilations.all {
                kotlinOptions {
                    allWarningsAsErrors = true
                }
            }
        }
    }

    detekt {
        config = files("detekt.yml")
    }

    koverMerged {
        enable()

        htmlReport {
            onCheck.set(true)
        }

        xmlReport {
            onCheck.set(true)
        }
    }

//    kover {
//        isDisabled.set(false)
//        xmlReport {
//            onCheck.set(true)
//        }
//        htmlReport {
//            onCheck.set(true)
//        }
//    }
}
