kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
                implementation("org.jetbrains.kotlin:kotlin-reflect")
            }
        }
        jvmMain {
            dependencies {
                implementation(rootProject.libs.kotlinx.coroutines.reactive)
                implementation(rootProject.libs.kotlinx.coroutines.reactor)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                api("io.projectreactor.rabbitmq:reactor-rabbitmq:1.5.5")
            }
        }
        jvmTest {
            dependencies {
                implementation(rootProject.libs.kotlinx.coroutines.reactive)
                implementation(rootProject.libs.kotlinx.coroutines.reactor)
                implementation(rootProject.libs.kotest.extensions.koin)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
                implementation("com.github.fridujo:rabbitmq-mock:1.1.1")
            }
        }
    }
}
