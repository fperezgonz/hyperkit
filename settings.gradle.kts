pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "hyperkit"
include("hyperkit-dto-api")
include("hyperkit-dto-generator-core")
include("hyperkit-dto-generator-gradle-plugin")
include("hyperkit-dto-generator-maven-plugin")
include("hyperkit-entity-generator")
include("hyperkit-projections-dsl")
include("hyperkit-projection-spec-validator")
include("hyperkit-query-builder")
include("hyperkit-utils-standard-test-model")
include("hyperkit-utils:serialization:jackson2")
include("hyperkit-utils:serialization:jackson3")
include("hyperkit-utils:serialization:specification")
include("hyperkit-spring-support:spring-boot-starter")
include("hyperkit-spring-support:spring-jackson2")
include("hyperkit-spring-support:spring-jackson3")
include("hyperkit-spring-support:spring-openapi")
include("hyperkit-spring-support:spring-persistence")
include("hyperkit-spring-support:spring-projection-field-alias-specification")
include("hyperkit-spring-support:spring-web")
includeBuild("hyperkit-examples/dto-generation-gradle-example")
includeBuild("hyperkit-examples/entity-generation-example")
includeBuild("hyperkit-examples/spring-boot-controller-example")