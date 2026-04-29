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
            implementation(libs.kotlin.reflect)
        }
        commonTest.dependencies {
            implementation(libs.bluebikebase.core)
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
