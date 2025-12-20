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
include("hyperkit-dto-api")
include("hyperkit-dto-generator-plugin")
include("hyperkit-entity-generator")
include("hyperkit-projections-dsl")
include("hyperkit-query-builder")
include("hyperkit-spring-boot-starter")
include("hyperkit-utils-serialization")
include("hyperkit-utils-spring")
includeBuild("hyperkit-examples/dto-generation-example")
includeBuild("hyperkit-examples/entity-generation-example")
includeBuild("hyperkit-examples/spring-boot-controller-example")