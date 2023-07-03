kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":runtime"))
                implementation(project(":core"))
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
