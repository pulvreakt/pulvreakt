pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    id("com.gradle.enterprise") version "3.14.1"
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "1.1.9"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.6.0"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishOnFailure()
    }
}

gitHooks {
    commitMsg { conventionalCommits() }
    preCommit {
        tasks("detekt")
    }
    createHooks()
}

rootProject.name = "pulvreakt"

include(":core")
include(":platform")
include(":rabbitmq-platform")
include(":mqtt-platform")
