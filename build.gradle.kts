
plugins {
    kotlin("jvm") version "2.0.0"
}

group = "com"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Основные зависимости
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    implementation("ch.qos.logback:logback-classic:1.2.7")
    implementation("org.slf4j:jul-to-slf4j:1.7.32")
    implementation("org.postgresql:postgresql:42.3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.5.2")

    // Тестовые зависимости
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")
}


tasks.test {
    useJUnitPlatform()
}
