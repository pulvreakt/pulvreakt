kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":core"))
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}
