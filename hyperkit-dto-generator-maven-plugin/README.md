# Hyperkit DTO Generator Maven Plugin

A Maven version of the Hyperkit DTO Generator plugin for Gradle

## Overview

The Hyperkit DTO Generator Maven Plugin automatically generates Data Transfer Objects (DTOs) from annotated classes
during the Maven build process. It processes classes annotated with `@GenerateDto` and creates corresponding DTO classes
with configurable properties and projections.

## Usage

- Add the Hyperkit DTO API dependency to your `pom.xml`
```xml
    <build>
        <plugins>
            <plugin>
                <groupId>solutions.sulfura</groupId>
                <artifactId>hyperkit-dto-generator-maven-plugin</artifactId>
                <version>${hyperkit.version}</version>
                <configuration>
                    <!-- Paths to the input source files (default: project compile source roots) -->
                    <inputPaths>
                        <inputPath>src/main/java/</inputPath>
                    </inputPaths>
                    <!-- Output path for the generated sources (default: "src/main/java/") -->
                    <rootOutputPath>src/main/java/</rootOutputPath>
                    <!-- Default package where the generated DTOs will be placed (default: "solutions.sulfura.hyperkit.dtos") -->
                    <defaultOutputPackage>solutions.sulfura.hyperkit.dtos</defaultOutputPackage>
                    <!-- Path to the template used for DTO generation (default: "templates/dto.vm") -->
                    <templatePath>templates/dto.vm</templatePath>
                </configuration>
                <executions>
                    <execution>
                        <id>generate-dtos</id>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>hyperkit-dto-generator-maven-plugin</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```
- Run `mvn generate-sources` or `mvn compile`


## Sample project:
- [dto-generation-maven-example](../hyperkit-examples/dto-generation-maven-example)