plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "dev.aceituno"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:multik-core:0.2.3")
    implementation("org.jetbrains.kotlinx:multik-default:0.2.3")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    if (gradle.startParameter.taskNames.contains("run")) {
        if (hasProperty("day")) {
            val day = property("day")!!.toString().padStart(2, '0')
            mainClass = "dev.aceituno.Day${day}Kt"
        } else {
            error("Please provide the property `day`, e.g. `-Pday=1`")
        }
    }
}

tasks.getByName<JavaExec>(tasks.run.name) {
    standardInput = System.`in`
}
