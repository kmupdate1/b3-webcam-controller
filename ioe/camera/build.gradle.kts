plugins {
    `maven-publish`
    alias { libs.plugins.kotlin.multiplatform }
    alias { libs.plugins.kotlin.serialization }
}

kotlin {
    jvm {

    }
    linuxArm64()
    macosArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bluebikebase.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.websockets)
            implementation(libs.ktor.serialization)

            implementation(project(":core:domain"))
        }
    }
}
