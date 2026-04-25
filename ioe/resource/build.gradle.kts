plugins {
    `maven-publish`
    alias { libs.plugins.kotlin.multiplatform }
}

kotlin {
    jvm()
    linuxArm64()
    macosArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bluebikebase.core)
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
