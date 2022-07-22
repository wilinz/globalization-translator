import org.jetbrains.compose.compose

plugins {
    id("org.jetbrains.intellij") version "1.3.0"
    java
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("idea")
}

group = "org.jetbrains.compose.intellij.platform"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

configurations.all { exclude ("xml-apis","xml-apis")}

dependencies {
//    compileOnly(compose.desktop.currentOs) runtime dependency is provided by org.jetbrains.compose.intellij.platform
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.google.code.gson:gson:2.9.0")
    testImplementation(kotlin("test"))
    implementation( fileTree("libs"))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2021.3")
    plugins.set(
        listOf(
            "org.jetbrains.compose.intellij.platform:0.1.0",
            "org.jetbrains.kotlin"
        )
    )
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
