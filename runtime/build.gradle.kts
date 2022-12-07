kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core"))
                implementation("org.jetbrains.kotlin:kotlin-reflect")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
            }
        }
    }
}
