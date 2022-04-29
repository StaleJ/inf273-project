import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    application
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
application {
    mainClass.set("RunInstanceKt")
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation(kotlin("test"))
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")
    implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation("org.postgresql:postgresql:42.3.3")
    implementation("com.google.code.gson:gson:2.9.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}