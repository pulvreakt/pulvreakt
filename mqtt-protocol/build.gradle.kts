kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":runtime"))
                implementation(project(":core"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(rootProject.libs.paho.mqtt)
            }
        }
    }
}
