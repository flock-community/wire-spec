import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

plugins {
    kotlin("multiplatform")
    kotlin("jvm") apply false
    id("com.github.johnrengelman.shadow")
}

group = "${Settings.groupId}.compiler"
version = Settings.version

repositories {
    mavenCentral()
}

kotlin {
    val projectName = name
    val buildAll = "WIRE_SPEC_BUILD_ALL".fromEnv()
    val buildMacX86 = buildAll || "WIRE_SPEC_BUILD_MAC_X86".fromEnv()
    val buildMacArm = buildAll || "WIRE_SPEC_BUILD_MAC_ARM".fromEnv()
    val buildWindows = buildAll || "WIRE_SPEC_BUILD_WINDOWS".fromEnv()
    val buildNothing = !buildMacX86 && !buildMacArm && !buildWindows
    val buildLinux = buildAll || "WIRE_SPEC_BUILD_LINUX".fromEnv() || buildNothing

    if (buildMacX86) macosX64 { build() }
    if (buildMacArm) macosArm64 { build() }
    if (buildLinux) linuxX64 { build() }
    if (buildWindows) mingwX64 { build() }
    js(IR) { build() }
    jvm {
        withJava()
        tasks {
            getByName<ShadowJar>("shadowJar") {
                archiveBaseName.set(projectName)
                mergeServiceFiles()
                manifest { attributes(mapOf("Main-Class" to "community.flock.wirespec.compiler.cli.MainKt")) }
            }
            build {
                dependsOn("shadowJar")
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":compiler:core"))
            }
        }
        val desktopMain by creating {
            dependsOn(commonMain)
        }
        if (buildMacX86) {
            val macosX64Main by getting {
                dependsOn(desktopMain)
            }
        }
        if (buildMacArm) {
            val macosArm64Main by getting {
                dependsOn(desktopMain)
            }
        }
        if (buildLinux) {
            val linuxX64Main by getting {
                dependsOn(desktopMain)
            }
        }
        if (buildWindows) {
            val mingwX64Main by getting {
                dependsOn(desktopMain)
            }
        }
        val jsMain by getting {
            dependsOn(commonMain)
        }
        val jvmMain by getting {
            dependsOn(commonMain)
        }
    }
}

fun String.fromEnv() = let(System::getenv).toBoolean()

fun KotlinNativeTargetWithHostTests.build() = binaries {
    executable {
        entryPoint = "community.flock.wirespec.compiler.cli.main"
    }
}

fun KotlinJsTargetDsl.build() {
    nodejs()
    binaries.executable()
}
