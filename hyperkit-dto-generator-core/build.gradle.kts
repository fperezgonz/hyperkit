plugins {
    `kotlin-dsl`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    api("fr.inria.gforge.spoon:spoon-core:11.3.0")
    api("org.apache.velocity:velocity-engine-core:2.4.1")
    implementation(project(":hyperkit-dto-api"))
    implementation("org.slf4j:slf4j-api:2.0.12")
}

kotlin {
    jvmToolchain(17)
}
