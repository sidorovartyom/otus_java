rootProject.name = "otusJava"
include("hw01-gradle")
include("hw02-generics")
include("hw04-gc")
include("hw03-reflection_annotations")
include("hw04-gc")
include("hw05-aop")
include("hw07-structuralPatterns")
include("hw06-solid")
include("hw08-io")
include("L19-jdbc")
include("L06-annotations")
include("L08-gc")
include("L10-byteCodes")
include("L13-creationalPatterns")
include("L14-behavioralPatterns")
include("L15-structuralPatterns")
include("L16-io:demo")
include("L17-nio-logging")
include("L18-rdbms")
include("L19-jdbc:demo")
include("L19-jdbc:homework")
include("L20-hibernate")

pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val jib: String by settings
    val protobufVer: String by settings

    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("com.google.cloud.tools.jib") version jib
        id("com.google.protobuf") version protobufVer
    }
}