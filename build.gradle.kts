plugins {
    kotlin("jvm") version "1.7.10"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
}
