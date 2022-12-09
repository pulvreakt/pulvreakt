kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":runtime"))
                implementation(project(":rabbitmq-platform"))
            }
        }
    }
}
