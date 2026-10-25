@file:Suppress("UnstableApiUsage")

import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.plugin.compose)
}

val catalogVersionName = providers.gradleProperty("VERSION_NAME").get()
val catalogVersionCode = catalogVersionName.split('.').map { it.toInt() }.let { bits ->
    check(bits.size == 3)
    bits[0] * 1_00_00 + bits[1] * 1_00 + bits[2]
}
val ciVersionName = providers.environmentVariable("GITHUB_REF_NAME")
    .orNull
    ?.takeIf { it.isNotBlank() }
    ?.let { refName -> "$catalogVersionName-$refName" }
    ?: catalogVersionName
val ciVersionCode = providers.environmentVariable("GITHUB_RUN_NUMBER")
    .orNull
    ?.toIntOrNull()
    ?: 1

val releaseSigningProperties = rootProject.layout.projectDirectory
    .file("release/signing.properties")
    .asFile
    .takeIf { it.isFile }
    ?.let { file -> Properties().apply { file.inputStream().use(::load) } }

android {
    namespace = "glass.yasan.orbit.catalog"

    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "glass.yasan.orbit.catalog"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionName = catalogVersionName
        versionCode = catalogVersionCode
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        releaseSigningProperties?.let { properties ->
            create("release") {
                storeFile = properties.getProperty("store.path")
                    ?.let { rootProject.layout.projectDirectory.asFile.resolve(it) }
                storePassword = properties.getProperty("store.password")
                keyAlias = properties.getProperty("key.alias")
                keyPassword = properties.getProperty("key.password")
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isPseudoLocalesEnabled = true
        }
        create("ci") {
            matchingFallbacks.add("release")
            applicationIdSuffix = ".ci"
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfigs.findByName("release")?.let { signingConfig = it }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        release {
            isPseudoLocalesEnabled = true
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfigs.findByName("release")?.let { signingConfig = it }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        buildConfig = false
        aidl = false
        resValues = false
        shaders = false
    }

    packaging {
        jniLibs.keepDebugSymbols.add("**/libandroidx.graphics.path.so")
    }

    lint {
        abortOnError = true
        warningsAsErrors = true
    }
}

androidComponents {
    onVariants(selector().withBuildType("ci")) { variant ->
        variant.outputs.forEach { output ->
            output.versionName.set(ciVersionName)
            output.versionCode.set(ciVersionCode)
        }
    }
}

dependencies {
    implementation(project(":catalog"))
    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.core)
    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.activityCompose)
    implementation(libs.compose.runtime)

    debugImplementation(libs.compose.tooling)
}
