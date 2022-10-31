kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core"))
                implementation(project(":platforms:rabbitmq"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
            }
        }

        jvmMain {
            dependencies {
                implementation(rootProject.libs.kotlinx.coroutines.reactive)
                implementation(rootProject.libs.kotlinx.coroutines.reactor)
            }
        }
    }
}
