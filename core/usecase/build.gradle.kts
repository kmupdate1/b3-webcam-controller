import org.gradle.kotlin.dsl.`maven-publish`

plugins {
    `maven-publish`
    alias { libs.plugins.kotlin.multiplatform }
}

kotlin {
    jvm {

    }
    linuxArm64()
    macosArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
