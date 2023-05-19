kotlin {
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":core"))
                implementation(project(":runtime"))
                implementation(project(":mqtt-protocol"))
                implementation("org.slf4j:slf4j-simple:2.0.3")
            }
        }
    }
}
