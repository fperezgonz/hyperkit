pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "hyperkit"
include("hyperkit-dto-generator-plugin")
include("hyperkit-dto-api")
include("hyperkit-projections-dsl")
include("hyperkit-utils-spring")
include("hyperkit-utils-serialization")
include("hyperkit-spring-boot-starter")
include("hyperkit-entity-generator")