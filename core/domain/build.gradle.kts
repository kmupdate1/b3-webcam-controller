plugins {
    `maven-publish`
    alias { libs.plugins.kotlin.multiplatform }
}

kotlin {
    jvm {

    }
    linuxArm64 {
        /*
        compilations["main"].cinterops {
            val openssl by creating {
                defFile(project.file("src/nativeInterop/cinterop/openssl.def"))
            }
        }
        */
    }
    macosArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bluebikebase.core)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        nativeMain.dependencies {
            implementation(libs.kotlincrypto.hash.sha2)
        }
    }
}
