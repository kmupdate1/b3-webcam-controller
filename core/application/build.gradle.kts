import org.gradle.kotlin.dsl.`maven-publish`

plugins {
    `maven-publish`
    alias { libs.plugins.kotlin.multiplatform }
}

kotlin {
    jvm {

    }
    linuxArm64()
}
