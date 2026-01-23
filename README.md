# Hyperkit Project

## Overview
Hyperkit is a set of tools for developing applications in Java, providing features for entity generation from database tables,
DTO generation, applying projections to projections and spring tools to facilitate development of spring applications using these features

## Project Structure
The project is organized into several modules, each with its own documentation:

- [hyperkit-entity-generator](hyperkit-entity-generator/README.md): Tool for generating JPA entity classes from database schemas. Available as a command line tool and as a Gradle plugin
- [hyperkit-dto-api](hyperkit-dto-api/README.md): Core API for DTOs, including annotations for DTO generation and property customization
- [hyperkit-dto-generator-plugin](hyperkit-dto-generator-plugin/README.md): Gradle plugin for generating DTOs from annotated classes
- [hyperkit-projections-dsl](hyperkit-projections-dsl/README.md): DSL for defining projections on DTOs
- [hyperkit-utils-serialization](hyperkit-utils-serialization/README.md): Utilities for serialization
- [hyperkit-utils-spring](hyperkit-utils-spring/README.md): Spring-specific utilities including HyperRepository and HyperMapper
- [hyperkit-spring-boot-starter](hyperkit-spring-boot-starter/README.md): Spring Boot starter for Hyperkit

## Getting Started

For detailed documentation on each module, please refer to the module-specific README files linked above. Here's a quick overview of how to get started with Hyperkit:

### Adding Hyperkit to Your Project

To use Hyperkit in your Spring Boot project, add the hyperkit-spring-boot-starter dependency:

### Gradle
```kotlin

plugins {
    id("solutions.sulfura.hyperkit-dto-generator") version "latest.version"
    id("solutions.sulfura.hyperkit-entity-generator-plugin") version "latest.version"
    // Other plugins...
}

repositories {
    maven {
        url = uri("https://public-package-registry.sulfura.solutions/")
    }
    // Other repositories... 
}

dependencies {
    implementation('solutions.sulfura:hyperkit-spring-boot-starter:latest.version')
    // Other dependencies...
}
```

### Maven (no support for entity and dto generation)
```xml
<dependency>
    <groupId>solutions.sulfura</groupId>
    <artifactId>hyperkit-spring-boot-starter</artifactId>
    <version>latest.version</version>
</dependency>
```

### Entity Generation (Gradle only)

- Configure the hyperkit/GenerateEntities Gradle task with database connection and the output package
```kotlin

hyperKitEntityGenerator {
    databaseUrl.set("<your-database-jdbc-string>")
    databaseUsername.set("<your-database-user>")
    databasePassword.set("<your-database-password>")
    basePackage.set(project.property("<output-package>"))
    // Allowed values: org.h2.Driver, org.postgresql.Driver, org.mariadb.jdbc.Driver, com.mysql.cj.jdbc.Driver
    databaseDriver.set("<database-driver>")
}

```
- Run the Gradle task hyperkit/GenerateEntities to generate the entity classes
- The task output can be modified by specifying a custom velocity template for the generated code using the property `templatePath`

### DTO Generation (Gradle only)

- Annotate the entity classes whose Dtos you want to generate with `@Dto`
- Annotate the fields that will be included for the generated Dtos with `@DtoProperty`
```java
@Dto(include = DtoProperty.class)
@Entity
public class User {
    @Id
    @DtoProperty
    public Long id;
    @DtoProperty
    public String name;
    @DtoProperty
    public String email;
    @OneToMany
    @DtoProperty
    public Set<Role> roles;
}
```
- The task output can be modified by specifying a custom velocity template for the generated code using the property `templatePath`
- Run the Gradle task hyperkit/generateDtos to generate the DTOs

### Projections and mapping between entities and DTOs
- Use the Dtos in your controllers and apply projections to them using the `@DtoProjectionSpec` annotation
```java
@GetMapping("/users/{id}")
public UserDto getUser(@PathVariable Long id, DtoProjection<UserDto> returnProjection) {
    
    User user = repository.findById(User.class, id, new UserContext());
    
    // Maps the entity graph to a dto graph
    val responseDto = hyperMapper.mapEntityToDto(user, UserDto.class, returnProjection);

    return responseDto;
}

@PostMapping("/users/{id}")
public UserDto postUser(@PathVariable Long id,
                        @UserDtoProjection
                        @RequestBody UserDto requestDto) {
    
    // Maps the dto graph to an entity graph and persists it
    User user = dtoMapper.persistDtoToEntity(requestDto, null);
    
    // Maps the entity graph back to a dto graph
    val responseDto = hyperMapper.mapEntityToDto(user, UserDto.class, returnProjection);
    
    return responseDto;
    
}

// Meta-annotation for specifying projections on controllers
@DtoProjectionSpec(projectedClass = UserDto.class, value ="""
        id
        email
        roles {
                id
                name
        }
        """)
@Retention(RetentionPolicy.RUNTIME)
@interface UserDtoProjection {
}

```
- Hyperkit integrates with springdoc and will also apply the projections to the openapi models generated by springdoc

### Intellij IDEA Plugin
- There is a <a href="https://plugins.jetbrains.com/plugin/25320-projections-dsl">Projections Dsl plugin</a> available for Intellij
that provides syntax highlighting, code completion and quick access to the dto properties referenced by the projections


## Key Features Overview

### Entity Generation (Gradle only)
- Generates entity classes from database schemas
- The code generation can be customized by providing a custom Velocity template

### DTO Generation (Gradle only)
- Generates Dtos from entity classes
- The code generation can be customized by providing a custom Velocity template

### Projections
- GraphQL-like DSL for defining dto projections

### Repository Operations
- Use `HyperRepository` for generic CRUD operations
- Support for sorting, pagination and filtering with JPA Specifications

### Mapping between entities and DTOs
- Use `HyperMapper` to map and persist Dtos to entities, or map entities to Dtos
- Extend `HyperRepository` to customize `HyperMapper` persistence

### Spring integration
- Automatic configuration with Spring Boot starter
- Define projections for requests and responses with annotations (or meta-annotations) on the mappings and its parameters
- Argument resolvers for projections, sorting and RSQL filtering
- Jackson module for serialization of `ValueWrapper`
- OpenApi customizer that applies projections defined on controllers to the generated models generated by springdoc

### Example Usage

Here's a simple example of a Spring Boot controller using Hyperkit:

```java
@RestController
@RequestMapping("/users")
public class UserController {
    private final HyperMapper hyperMapper;
    private final HyperRepository<UserContext> repository;

    public UserController(HyperMapper hyperMapper, HyperRepository<UserContext> repository) {
        this.hyperMapper = hyperMapper;
        this.repository = repository;
    }

    // Example using direct DtoProjectionSpec annotation
    @GetMapping("/users")
    @DtoProjectionSpec("id, name, email, roles{id, name}")
    public List<UserDto> getUsers(
            @RequestParam(name = "filter", required = false) Specification<User> filter,
            @RequestParam(name = "sort", required = false) Sort sort, DtoProjection<UserDto> returnProjection
    ) {

        List<User> users = repository.findAll(User.class, filter, sort, new UserContext());

        return users.stream()
                .map(user -> hyperMapper.mapEntityToDto(user, UserDto.class, returnProjection))
                .collect(Collectors.toList());

    }

    // Example using meta-annotated annotation
    @GetMapping("/users/{id}")
    @StdUserDto
    public UserDto getUser(@PathVariable Long id, DtoProjection<UserDto> returnProjection) {
        User user = repository.findById(User.class, id, new UserContext());
        return hyperMapper.mapEntityToDto(user, UserDto.class, returnProjection);
    }

    @PostMapping("/users")
    @StdUserDto
    public UserDto createUser(@RequestBody @StdUserDto UserDto userDto, DtoProjection<UserDto> projection) {
        User user = hyperMapper.persistDtoToEntity(userDto, null);
        return hyperMapper.mapEntityToDto(user, UserDto.class, returnProjection);
    }

    @DtoProjectionSpec(projectedClass = UserDto.class, 
            value = """
                        id
                        name
                        email
                        roles {id, name}
                    """)
    @Retention(RetentionPolicy.RUNTIME)
    @interface StdUserDto {
    }

}
```

For more detailed examples and documentation, please refer to the module-specific README files.