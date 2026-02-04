# Dto Generation Example

This project demonstrates how to use the `hyperkit-dto-generator-plugin` to generate dto classes for annotated classes

## Overview

This project uses the following components:
- **HyperKit Dto Generator Gradle Plugin**: Generates dtos based on annotated classes.

## How to Run this project

To generate the dtos, follow these steps:
1. Run `gradlew :generateDtos` to generate the dtos from the annotated classes in the `solutions.sulfura.hyperkit.entities` package.
2. The generated dtos will be available in `src/main/java/solutions/sulfura/hyperkit/dtos`.

## Configuration

No configuration is required to run this project; however, some plugin properties can be configured in the `build.gradle.kts` file, on the block `hyperKitDtoGenerator`
