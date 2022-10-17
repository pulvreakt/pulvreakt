kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core"))
            }
        }
        jvmMain {
            dependencies {
                implementation(rootProject.libs.kotlinx.coroutines.reactive)
                implementation(rootProject.libs.kotlinx.coroutines.reactor)
                implementation("io.projectreactor.rabbitmq:reactor-rabbitmq:1.5.5")
            }
        }
    }
}
