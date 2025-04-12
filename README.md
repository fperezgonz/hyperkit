# Hyperkit Project

## Overview
Hyperkit is a set of tools for developing applications in Java and Spring, providing features for Dto generation and mapping, value wrapping, list operations and applying projections

## Project Structure
The project is organized into several modules:
- `hyperkit-dto-api`: Core API for DTOs
- `hyperkit-dto-generator-plugin`: Gradle plugin for generating DTOs from annotated classes
- `hyperkit-projections-dsl`: DSL for defining projections
- `hyperkit-spring-boot-starter`: Spring Boot starter for Hyperkit
- `hyperkit-utils-serialization`: Utilities for serialization
- `hyperkit-utils-spring`: Spring-specific utilities

## Getting Started

### Spring boot starter
To use Hyperkit in your Spring Boot project, add the hyperkit-spring-boot-starter dependency to your build file

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

### Using the DTO Generator Plugin

The `hyperkit-dto-generator-plugin` is a Gradle plugin that generates DTO classes from annotated source classes.

#### Adding the Plugin

Add the plugin to your Gradle build script:

```gradle
plugins {
    id("solutions.sulfura.hyperkit-dto-generator-plugin") version "latest.version"
}
```

#### Configuring the Plugin

Configure the plugin using the `hyperKit` extension:

```gradle
hyperKit {
    // Paths to the input source files (default: ["src/main/java/"])
    inputPaths = setOf("src/main/java/")

    // Output path for the generated sources (default: "src/main/java/")
    rootOutputPath = "src/main/java/"

    // Default package where the generated DTOs will be placed (default: "solutions.sulfura.hyperkit.dtos")
    defaultOutputPackage = "solutions.sulfura.hyperkit.dtos"
}
```

### Annotating Classes for DTO Generation

To generate a DTO for a class, annotate it with `@Dto`:

```java
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

@Dto
public class User {
    public String username;
    public String email;

    // Getters and setters...
}
```

This will generate a `UserDto` class in the configured output package.

### Customizing DTO Generation

The `@Dto` annotation supports several attributes for customizing the generated DTO:

```java
@Dto(
    // Specify which annotations mark properties for inclusion in the DTO
    include = {DtoProperty.class, Deprecated.class},

    // Specify a custom package for the generated DTO
    destPackageName = "com.example.dtos",

    // Add a prefix to the generated DTO class name
    prefix = "My",

    // Change the suffix of the generated DTO class name (default: "Dto")
    suffix = "Dto"
)
public class User {
    // ...
}
```

### Customizing Properties

Use the `@DtoProperty` annotation to customize individual properties:

```java
import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.dtos.annotations.DtoProperty;

@Dto
public class User {
    // Custom property name in the DTO
    @DtoProperty(propertyName = "login")
    public String username;

    // Preserve case for properties with uppercase names
    @DtoProperty(preserveCase = true)
    public String EMAIL_ADDRESS;

    // Create getter and setter in the DTO
    @DtoProperty(createGetter = true, createSetter = true)
    private String password;

    // ...
}
```

### Running the Plugin

To generate the DTOs, run the `generateDtos` task:

```bash
./gradlew generateDtos
```

This will process all classes annotated with `@Dto` and generate the corresponding DTO classes in the configured output directory.

## Spring Utilities

### HyperRepository

The `HyperRepository` is a generic repository interface for basic CRUD operations, RSQL queries, pagination, and sorting. It provides a unified API for working with different entity types, allowing you to use the same repository for all entities.

#### Key Features

- Generic interface parameterized with a context type `<C>` for additional operation information
- Type-safe methods for finding, saving, and deleting entities
- Support for sorting and pagination
- Integration with Spring Data JPA's Specification for filtering

### HyperMapper

The `HyperMapper` is a service for mapping between DTOs and entities. It provides methods for converting DTOs to entities and vice versa, handling relationships, and applying projections.

#### Key Features

- Bidirectional mapping between DTOs and entities
- Support for nested DTOs and entity relationships
- Integration with `HyperRepository` for finding and persisting entities
- Support for DTO projections to control which fields are included
- Handling of list operations (add, remove, clear)

#### Example Usage

```java

// Map entity to DTO with projection
UserDto userDto = hyperMapper.mapEntityToDto(userEntity, UserDto.class, projection);

// Map DTO to entity
User user = hyperMapper.persistDtoToEntity(userDto, contextData);
```

### RSQL Filter Argument Resolver

The `RsqlFilterArgumentResolver` is a Spring `HandlerMethodArgumentResolver` that resolves RSQL filter strings in HTTP requests to `org.springframework.data.jpa.domain.Specification` objects. RSQL (RESTful Service Query Language) is a query language for parameterized filtering of resources.

#### Key Features

- Converts RSQL filter strings to JPA Specifications
- Supports custom parameter names via `@RequestParam` annotation
- Handles default values and required parameters
- Integrates with Spring Data JPA for filtering entities

#### Example Usage

```java
@GetMapping("/users")
public List<UserDto> getUsers(@RequestParam(name = "filter", required = false) Specification<User> filter) {
    // filter is automatically resolved from the RSQL string in the request parameter
    return userService.findAll(filter);
}
```

#### RSQL Syntax Examples

- Equality: `name==John`
- Not equal: `name!=John`
- Greater than: `age=gt=18`
- Less than: `age=lt=30`
- Between: `age=bt=(18,30)`
- In: `status=in=(ACTIVE,PENDING)`
- Logical AND: `name==John;age=gt=18`
- Logical OR: `name==John,name==Jane`

### DTO Projection Return Argument Resolver

The `DtoProjectionReturnArgumentResolver` is a Spring `HandlerMethodArgumentResolver` that resolves `DtoProjection` parameters in controller methods by extracting projection information from the method's return type annotation.

#### Key Features

- Resolves DtoProjection parameters in controller methods
- Extracts projection information from method annotations
- Supports both direct annotations and meta-annotations
- Integrates with Hyperkit's projection DSL

#### Example Usage

```java
@GetMapping("/users/{id}")
@DtoProjectionSpec("id, name, email, roles{id, name}")
public UserDto getUser(@PathVariable Long id, DtoProjection<UserDto> projection) {
    // projection is automatically resolved from the method's annotation
    User user = userService.findById(id);
    return hyperMapper.mapEntityToDto(user, UserDto.class, projection);
}
```

### Sort Argument Resolver

The `SortArgumentResolver` is a Spring `HandlerMethodArgumentResolver` that resolves sort parameters in HTTP requests to `org.springframework.data.domain.Sort` objects. It supports two formats:

1. Field-direction format: `field1:direction,field2:direction` (e.g., `name:asc,date:desc`)
2. Prefix format: `+-field1,+-field2` (e.g., `+name,-date`)

#### Configuration Properties

| Property | Description | Default |
|----------|-------------|---------|
| `hyperkit.sort-resolver.treat-nulls-as-unsorted` | When `true`, null sort parameters are treated as `Sort.unsorted()`. When `false`, null is returned. | `true` |

#### Example Configuration

```yaml
hyperkit:
  sort-resolver:
    treat-nulls-as-unsorted: false
```
