kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core"))
                implementation(rootProject.libs.kotlinx.serialization.json)
            }
        }
    }
}
