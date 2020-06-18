import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
}

kotlin {
    val uniqueName = "${project.rootProject.name}${project.name.capitalize()}"

    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
            if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
                ::iosArm64
            else
                ::iosX64

    iOSTarget("ios") {
        binaries {
            framework(uniqueName) {
                export(project(":common:core"))
                export(project(":common:model"))
                export(project(":feature:home"))
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":common:core"))
                api(project(":common:model"))
                api(project(":feature:home"))

                implementation(Kotlin.stdlib)
                implementation(Kodein.core)
            }
        }
    }
}

val buildXCFramework = tasks.register<XCFrameworkTask>("buildXCFramework") { singleTarget = true }
tasks.build { finalizedBy(buildXCFramework) }
