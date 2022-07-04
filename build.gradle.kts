import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("kapt") version "1.5.31"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

val secretPropsFile = project.rootProject.file("local.properties")
secretPropsFile.reader().use { Properties().apply { load(it) } }
    .onEach { (k,v) -> ext[k.toString()] = v }

fun getExtraString(name: String) = ext[name]?.toString()!!

nexusPublishing {
    val ossrhUsername = getExtraString("ossrhUsername")
    val ossrhPassword = getExtraString("ossrhPassword")
    val ossrhStagingProfileId = getExtraString("ossrhStagingProfileId")

    repositories {
        sonatype {
            stagingProfileId.set(ossrhStagingProfileId)
            username.set(ossrhUsername)
            password.set(ossrhPassword)
        }
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("kotlin-kapt")
    }

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.5.2")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.5.2")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib")

        implementation("org.slf4j:slf4j-api:1.7.36")
        implementation("io.github.microutils:kotlin-logging:1.12.5")

        testImplementation("io.mockk:mockk:1.12.0")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
        testImplementation("org.assertj:assertj-core:3.11.1")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}