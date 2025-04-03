# Hyperkit Project

## Overview
Hyperkit is a Java library for working with DTOs (Data Transfer Objects), providing features for value wrapping, list operations, and applying projections.

## Project Structure
The project is organized into several modules:
- `hyperkit-dto-api`: Core API for DTOs
- `hyperkit-dto-generator-plugin`: Gradle plugin for generating DTOs from annotated classes
- `hyperkit-projections-dsl`: DSL for defining projections
- `hyperkit-spring-boot-starter`: Spring Boot starter for Hyperkit
- `hyperkit-utils-serialization`: Utilities for serialization
- `hyperkit-utils-spring`: Spring-specific utilities

## Getting Started
To use Hyperkit in your Spring Boot project, add the hyperkit-spring-boot-starter dependency to your build file.

### Gradle
```gradle
implementation 'solutions.sulfura:hyperkit-spring-boot-starter:latest.version'
```

### Maven
```xml
<dependency>
    <groupId>solutions.sulfura</groupId>
    <artifactId>hyperkit-spring-boot-starter</artifactId>
    <version>latest.version</version>
</dependency>
```

## License
[License information]