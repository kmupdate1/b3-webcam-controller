plugins {
    alias { libs.plugins.kotlin.multiplatform } apply false
}

val isRelease = project.hasProperty("release") && project.property("release") == "true"
val v = rootProject.property("library.version")?.toString() ?: "unspecified"

val repoType = if (isRelease) "releases" else "snapshots"
val domain = rootProject.property("repository.domain")?.toString()
val repositoryUri = "https://$domain/repository/bluebikebase-$repoType/"

allprojects {
    group = "org.bluebikebase"
    version = if (isRelease) v else "$v-SNAPSHOT"

    repositories {
        mavenCentral()
        maven {
            url = uri(repositoryUri)

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

repositories {
    mavenCentral()
}
