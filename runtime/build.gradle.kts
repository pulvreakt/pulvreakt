kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core"))
                implementation("org.jetbrains.kotlin:kotlin-reflect")
            }
        }
    }
}
