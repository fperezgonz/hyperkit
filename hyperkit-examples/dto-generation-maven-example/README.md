# Dto Generation Example

This project shows how to generate dto classes with the Maven version of the `hyperkit-dto-generator-plugin`

## Overview

This project uses the following components:
- **HyperKit Dto Generator Maven Plugin**: Generates dtos based on annotated classes.

## How to Run this project

To generate the dtos, follow these steps:
1. Run `mvnw generate-sources` or `mvnw compile` to generate the dtos for the annotated classes in the `solutions.sulfura.hyperkit.entities` package.
2. The generated dtos will be available in `src/main/java/solutions/sulfura/hyperkit/dtos`.

