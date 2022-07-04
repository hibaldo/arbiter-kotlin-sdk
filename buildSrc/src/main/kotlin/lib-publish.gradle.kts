import java.util.*

plugins {
    id("java-library")
    id("maven-publish")
    id("signing")
}

group = "com.kep.arbiter.sdk"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("sdk") {
            from(components["java"])
            pom {
                name.set("Arbiter kotlin server SDK")
                description.set("Arbiter AB Experiment server-side SDK for JVM (java, kotlin)")
                url.set("https://github.com/kakaoenterprise/arbiter-kotlin-sdk")
                inceptionYear.set("2022")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("kakaoenterprise")
                        name.set("kakaoenterprise")
                        email.set("insight.service@kakaoenterprise.com")
                    }
                }
                scm {
                    url.set("https://github.com/kakaoenterprise/arbiter-kotlin-sdk")
                }
            }
        }
    }
}

fun getExtraString(name: String) = rootProject.extra[name]?.toString()!!
fun base64Decode(value: String) = String(Base64.getDecoder().decode(value)).trim()

signing {
    val signingKey = base64Decode(getExtraString("signingKey"))
    val signingPassword = getExtraString("signingPassword")
    val signingKeyId = getExtraString("signingKeyId")

    setRequired { !project.version.toString().endsWith("-SNAPSHOT") && !project.hasProperty("skipSigning") }
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications["sdk"])
}