import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":runtime"))
                implementation(project(":core"))
            }
        }
        val jvmNativeMain by creating{
            dependencies{
                dependsOn(commonMain)
                implementation(libs.bundles.kmqtt)
            }
        }

        val jvmMain by getting {
            dependencies {
                dependsOn(jvmNativeMain)
                api(libs.slf4j.simple)
            }
        }

        val nativeMain by getting {
            dependencies {
                dependsOn(jvmNativeMain)
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
}
