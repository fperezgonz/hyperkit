# Hyperkit Project

## Overview
Hyperkit is a set of tools for developing applications in Java, providing features for entity generation from database tables, DTO generation and applying projections to them and spring tools to facilitate development of spring applications using these features

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

### Key Features Overview

#### DTO Generation and Mapping
- Annotate your entity classes with `@Dto` to generate DTOs
- Apply projections to control which fields are included

#### Repository Operations
- Use `HyperRepository` for generic CRUD operations
- Support for sorting, pagination, and filtering

#### Spring MVC Integration
- Argument resolvers for projections, sorting, and RSQL filtering
- Jackson module for serialization of `ValueWrapper`
- Use `HyperMapper` to map between entities and DTOs
- OpenApi customizer that applies projections defined on controllers to the generated models
- Automatic configuration with Spring Boot starter

### Example Usage

Here's a simple example of a Spring Boot controller using Hyperkit:

```java
@RestController
@RequestMapping("/users")
public class UserController {
    private final HyperMapper hyperMapper;
    private final HyperRepository<UserContext> repository;

    // Constructor injection...

    // Example using meta-annotated annotation
    @GetMapping("/{id}")
    @StdUserDto
    public UserDto getUser(@PathVariable Long id, DtoProjection<UserDto> returnProjection) {
        User user = repository.findById(User.class, id, new UserContext());
        return hyperMapper.mapEntityToDto(user, UserDto.class, returnProjection);
    }

    // Example using direct annotation
    @GetMapping
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

    @PostMapping("/")
    @StdUserDto
    public UserDto createUser(@RequestBody @StdUserDto UserDto userDto, DtoProjection<UserDto> projection) {
        User user = hyperMapper.persistDtoToEntity(userDto, new UserContext());
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