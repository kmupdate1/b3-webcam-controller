import java.net.URI

plugins {
    `maven-publish`
    alias { libs.plugins.kotlin.multiplatform } apply false
    alias { libs.plugins.kotlin.serialization } apply false
}

val isRelease = project.hasProperty("release") && project.property("release") == "true"
val v = rootProject.property("library.version")?.toString() ?: "unspecified"

val repoType = "bluebikebase-" + if (isRelease) "releases" else "snapshots"
val domain = rootProject.property("repository.domain")?.toString()
fun repositoryUri(target: String): URI = uri("https://$domain/repository/$target/")

allprojects {
    group = "org.bluebikebase.iot"
    version = if (isRelease) v else "$v-SNAPSHOT"

    repositories {
        mavenCentral()
        maven {
            url = repositoryUri("bluebikebase-snapshots")

            credentials(PasswordCredentials::class) {
                username = System.getenv("B3_REPO_USER")
                password = System.getenv("B3_REPO_PASS")
            }

            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}

subprojects {
    plugins.withId("maven-publish") {
        configure<PublishingExtension> {
            repositories {
                maven {
                    url = repositoryUri(repoType)

                    credentials(PasswordCredentials::class) {
                        username = System.getenv("B3_REPO_USER")
                        password = System.getenv("B3_REPO_PASS")
                    }

                    authentication {
                        create<BasicAuthentication>("basic")
                    }
                }
            }
        }
    }
}
