plugins {
    kotlin("jvm") version "1.9.20"
    application
}

group = "net.turtton"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("net.turtton.ktcakesample.MainKt")
}
