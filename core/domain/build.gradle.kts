plugins {
    `maven-publish`
    alias { libs.plugins.kotlin.multiplatform }
}

kotlin {
    jvm {

    }
    linuxArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bluebikebase.core)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        linuxMain.dependencies {
            implementation(libs.kotlincrypto.hash.sha2)
        }
    }
}
