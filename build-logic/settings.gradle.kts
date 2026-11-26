rootProject.name = "orbit-compose-build-logic"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }

    repositories {
        gradlePluginPortal()
        maven("https://maven-central.storage-download.googleapis.com/maven2/") { // Google mirror of Maven Central, avoids rate limits
            allowInsecureContinueWhenDisabled.set(true)
        }
        mavenCentral()
        google()
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven-central.storage-download.googleapis.com/maven2/") { // Google mirror of Maven Central, avoids rate limits
            allowInsecureContinueWhenDisabled.set(true)
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include("conventions")
