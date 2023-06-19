kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":platform"))
                implementation(rootProject.libs.kotlinx.serialization.json)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
            }
        }
    }
}
