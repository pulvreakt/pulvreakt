kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(rootProject.libs.kotlinx.serialization.json)
            }
        }
    }
}
