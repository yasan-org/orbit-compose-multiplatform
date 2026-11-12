package glass.yasan.orbit.buildlogic

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.IncrementalSyncTask

class LibraryPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        val libs = the<LibrariesForLibs>()

        pluginManager.apply("base")
        pluginManager.apply("org.jetbrains.kotlin.multiplatform")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        pluginManager.apply("com.android.kotlin.multiplatform.library")

        extensions.configure<BasePluginExtension> {
            archivesName.set("orbit-${project.name}")
        }

        configurations.all {
            resolutionStrategy {
                force(libs.compose.material3)
            }
        }

        extensions.getByType<KotlinBaseExtension>().apply {
            jvmToolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
                vendor.set(JvmVendorSpec.AZUL)
            }
        }

        extensions.configure<KotlinMultiplatformExtension> {
            explicitApi()

            // Android
            android {
                namespace = "glass.yasan.orbit.${project.name}"
                compileSdk = libs.versions.compileSdk.get().toInt()
                minSdk = libs.versions.minSdk.get().toInt()

                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                    allWarningsAsErrors.set(true)
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }

                androidResources {
                    enable = true
                }

                withHostTest {
                    isIncludeAndroidResources = true
                }

                withDeviceTest {
                    instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                lint {
                    abortOnError = true
                    warningsAsErrors = true
                }

                packaging {
                    resources {
                        excludes.add("META-INF/AL2.0")
                        excludes.add("META-INF/LGPL2.1")
                    }
                }
            }

            // iOS
            iosArm64()
            iosSimulatorArm64()

            // macOS
            macosArm64()

            // Desktop
            jvm("desktop") {
                compilations.all {
                    compileTaskProvider.configure {
                        compilerOptions {
                            jvmTarget.set(JvmTarget.JVM_11)
                            allWarningsAsErrors.set(true)
                            freeCompilerArgs.add("-Xexpect-actual-classes")
                        }
                    }
                }
            }

            // Web
            js {
                browser()
            }
            @OptIn(ExperimentalWasmDsl::class)
            wasmJs {
                browser()
            }

            applyDefaultHierarchyTemplate()

            // Custom source set for non-Android platforms
            sourceSets {
                val commonMain by getting
                val nonAndroidMain by creating {
                    dependsOn(commonMain)
                }
                val iosMain by getting { dependsOn(nonAndroidMain) }
                val macosMain by getting { dependsOn(nonAndroidMain) }
                val desktopMain by getting { dependsOn(nonAndroidMain) }
                val jsMain by getting { dependsOn(nonAndroidMain) }
                val wasmJsMain by getting { dependsOn(nonAndroidMain) }
            }
        }

        tasks.withType<IncrementalSyncTask>().configureEach {
            if (isTestExecutableCompileSync()) {
                duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            }
        }
    }

    private fun IncrementalSyncTask.isTestExecutableCompileSync(): Boolean =
        name.endsWith("TestDevelopmentExecutableCompileSync") ||
            name.endsWith("TestProductionExecutableCompileSync")
}
