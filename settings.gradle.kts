plugins {
    id("com.gradle.enterprise") version "3.12.3"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishOnFailure()
    }
}

rootProject.name = "pulverization-framework"

include(":core")
include(":platform")
include(":rabbitmq-platform")
// include(":examples:example-01")
// include(":examples:example-02")
include(":examples:example-03")
