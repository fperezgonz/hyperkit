plugins {
    `java-library`
    `maven-publish`
    id("io.spring.dependency-management") version "1.1.7"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "hyperkit-utils-spring"
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "Gitlab"
            url = uri("https://gitlab.com/api/v4/projects/67836497/packages/maven")
            credentials(HttpHeaderCredentials::class) {
                name = "Job-Token"
                value = System.getenv("CI_JOB_TOKEN")
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
    }
}

// Configuration used to set up mockito instrumentation to support running the tests using Gradle with JDK 21+ (https://javadoc.io/static/org.mockito/mockito-core/5.14.2/org/mockito/Mockito.html#0.3)
val mockitoAgent: Configuration by configurations.creating {
    isTransitive = false
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.6")
    }
}

dependencies {
    implementation(project(":hyperkit-dto-api"))
    implementation(project(":hyperkit-projections-dsl"))
    implementation(project(":hyperkit-utils-serialization"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.github.perplexhub:rsql-jpa-spring-boot-starter:6.0.26")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.8.9")
    compileOnly("org.jspecify:jspecify:1.0.0")
    testImplementation("org.hsqldb:hsqldb:2.7.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    mockitoAgent("org.mockito:mockito-core")
}

tasks.test {
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
    useJUnitPlatform()
}
