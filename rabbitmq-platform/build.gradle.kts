kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":platform"))
                implementation(rootProject.libs.kotlinx.serialization.json)
            }
        }
        jvmMain {
            dependencies {
                implementation(rootProject.libs.kotlinx.coroutines.reactor)
                api(rootProject.libs.rabbitmq.reactor)
            }
        }
        jvmTest {
            dependencies {
                implementation(rootProject.libs.kotlinx.coroutines.reactor)
            }
        }
    }
}
