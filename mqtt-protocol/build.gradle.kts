import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":runtime"))
                implementation(project(":core"))
            }
        }
        val multithreadMain by creating{
            dependencies{
                dependsOn(commonMain)
                implementation(libs.bundles.kmqtt)
            }
        }

        val jvmMain by getting {
            dependencies {
                dependsOn(multithreadMain)
                api(libs.slf4j.simple)
            }
        }

        val nativeMain by getting {
            dependencies {
                dependsOn(multithreadMain)
            }
        }

        val jsMain by getting{
            dependencies {
                implementation(rootProject.libs.kotlin.stdlib.js)
                implementation(npm("mqtt", "5.5.3")) //todo use toml
            }
        }

        val linuxX64Main by getting {
            dependencies {
                dependsOn(nativeMain)
                implementation(files("native-libs/openssl-linux-x64.klib"))
            }
        }

        val linuxArm64Main by getting {
            dependencies {
                dependsOn(nativeMain)
                implementation(files("native-libs/openssl-linux-arm64.klib"))
            }
        }

        val tvosSimulatorArm64Main by getting {
            dependencies {
                dependsOn(nativeMain)
                implementation(files("native-libs/openssl-tvos-simulator-arm64.klib"))
            }
        }

        val tvosArm64Main by getting {
            dependencies {
                dependsOn(nativeMain)
                implementation(files("native-libs/openssl-tvos-arm64.klib"))
            }
        }

        val watchosSimulatorArm64Main by getting {
            dependencies {
                dependsOn(nativeMain)
                implementation(files("native-libs/openssl-watchos-simulator-arm64.klib"))
            }
        }

        val watchosArm64Main by getting {
            dependencies {
                dependsOn(nativeMain)
                implementation(files("native-libs/openssl-watchos-arm64.klib"))
            }
        }

        val iosX64Main by getting {
            dependencies {
                dependsOn(nativeMain)
                implementation(files("native-libs/openssl-ios-x64.klib"))
            }
        }

        val iosSimulatorArm64Main by getting {
            dependencies {
                dependsOn(nativeMain)
                implementation(files("native-libs/openssl-ios-simulator-arm64.klib"))
            }
        }

        val iosArm64Main by getting {
            dependencies {
                dependsOn(nativeMain)
                implementation(files("native-libs/openssl-ios-arm64.klib"))
            }
        }

        val macosArm64Main by getting {
            dependencies {
                dependsOn(nativeMain)
                implementation(files("native-libs/openssl-macos-arm64.klib"))
            }
        }

        val macosX64Main by getting {
            dependencies {
                dependsOn(nativeMain)
                implementation(files("native-libs/openssl-macos-x64.klib"))
            }
        }

        val mingwX64Main by getting {
            dependencies {
                dependsOn(nativeMain)
                implementation(files("native-libs/openssl-mingw-x64.klib"))
            }
        }
    }

    val nativeSetup: KotlinNativeTarget.(targetName: String) -> Unit = { targetName ->
        compilations["main"].defaultSourceSet.dependsOn(sourceSets[targetName+"Main"])
    }

    linuxX64 {
        nativeSetup("linuxX64")
    }

    linuxArm64 {
        nativeSetup("linuxArm64")
    }

    mingwX64 {
        nativeSetup("mingwX64")
    }

    macosX64 {
        nativeSetup("macosX64")
    }

    macosArm64 {
        nativeSetup("macosArm64")
    }

    iosArm64 {
        nativeSetup("iosArm64")
    }

    iosSimulatorArm64 {
        nativeSetup("iosSimulatorArm64")
    }

    iosX64 {
        nativeSetup("iosX64")
    }

    watchosArm64 {
        nativeSetup("watchosArm64")
    }

    watchosSimulatorArm64 {
        nativeSetup("watchosSimulatorArm64")
    }

    tvosArm64 {
        nativeSetup("tvosArm64")
    }

    tvosSimulatorArm64 {
        nativeSetup("tvosSimulatorArm64")
    }
}
