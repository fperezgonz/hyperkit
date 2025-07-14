# Hyperkit Spring Boot Starter

## Overview
The Hyperkit Spring Boot Starter provides auto-configuration for Hyperkit components in Spring Boot applications. It simplifies the integration of Hyperkit features into your Spring Boot projects by automatically configuring the necessary beans and dependencies.

## Key Features
- Auto-configuration of Hyperkit components

## Usage

### Adding the Dependency

To use Hyperkit in your Spring Boot project, add the hyperkit-spring-boot-starter dependency to your build file:

#### Gradle
```gradle
implementation 'solutions.sulfura:hyperkit-spring-boot-starter:latest.version'
```

#### Maven
```xml
<dependency>
    <groupId>solutions.sulfura</groupId>
    <artifactId>hyperkit-spring-boot-starter</artifactId>
    <version>latest.version</version>
</dependency>
```

### Auto-Configuration

The starter automatically configures the following components:

1. **HyperMapper** - Tool for mapping between DTOs and entities
2. **Argument Resolvers** - Resolves controllers arguments such as projections, sort, and RSQL filters
3. **Jackson Modules** - For serialization support of `Dto` and `ValueWrapper`
4. **OpenApi customizer** - Support for projections when creating the OpenApi specification

### Configuration Properties

You can customize the behavior of Hyperkit components using configuration properties in your `application.properties` or `application.yml` file:

```yaml
hyperkit:
  # Sort resolver configuration
  sort-resolver:
    treat-nulls-as-unsorted: true
  
  # Other configuration properties...
```

## Related Modules
- [hyperkit-dto-api](../hyperkit-dto-api/README.md): Core API for DTOs
- [hyperkit-projections-dsl](../hyperkit-projections-dsl/README.md): DSL for defining projections
- [hyperkit-utils-spring](../hyperkit-utils-spring/README.md): Spring utilities including HyperMapper and argument resolvers
- [hyperkit-utils-serialization](../hyperkit-utils-serialization/README.md): Serialization utilities