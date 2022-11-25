kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core"))
                implementation("org.jetbrains.kotlin:kotlin-reflect")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
            }
        }
    }
}
