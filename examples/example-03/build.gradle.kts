kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":runtime"))
                implementation(project(":rabbitmq-platform"))
            }
        }
    }
    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        enabled = false
    }
}
