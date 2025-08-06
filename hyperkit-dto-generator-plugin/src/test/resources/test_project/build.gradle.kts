plugins{
    java
    id("solutions.sulfura.hyperkit-dto-generator-plugin") version "5.0.0-SNAPSHOT"
}

hyperKitDtoGenerator {
    inputPaths = setOf("src/test_input_sources/")
    rootOutputPath = "src/out/java"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies{
    implementation("org.jspecify:jspecify:1.0.0")
}