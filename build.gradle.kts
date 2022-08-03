import org.commonmark.renderer.html.HtmlRenderer
import org.commonmark.parser.Parser as MarkdownParser
import java.io.Reader

plugins {
    id("org.jetbrains.intellij") version "1.7.0"
    java
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("idea")
}

group = "com.wilinz.globalization"
version = "1.0.1"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    maven { url = uri("https://jitpack.io") }
}

configurations.all { exclude("xml-apis", "xml-apis") }

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.commonmark:commonmark:0.18.1")
    }
}

dependencies {
//    compileOnly(compose.desktop.currentOs) runtime dependency is provided by org.jetbrains.compose.intellij.platform
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.github.wilinz:java-properties:1.0.6")
    implementation("com.github.dom4j:dom4j:version-2.1.1")
    testImplementation(kotlin("test"))
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

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "9"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
    patchPluginXml {
        sinceBuild.set("203")
        untilBuild.set("")
        pluginDescription.set(getDescription("description.md"))
    }
}

fun getDescription(relative: String): String {
    return projectDir.resolve(relative).reader().use {
        markdownToHtml(it)
    }
}

fun markdownToHtml(reader: Reader): String {
    val parser = MarkdownParser.builder().build()
    val document = parser.parseReader(reader)
    val renderer = HtmlRenderer.builder().build()
    return renderer.render(document)
}