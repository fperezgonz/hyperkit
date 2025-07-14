# Hyperkit Utils Spring

## Overview
The Hyperkit Utils Spring module provides Spring-specific utilities for working with DTOs, repositories, and web controllers. It includes components for mapping between DTOs and entities, handling repository operations, and resolving controller arguments.

## Key Features
- HyperRepository for generic CRUD operations
- HyperMapper maps between DTOs and entities
- Argument resolvers for projections, sorting, and filtering
- OpenAPI integration using SpringDoc

## Components

### HyperRepository

The `HyperRepository` is a generic repository interface for basic CRUD operations, RSQL queries, pagination, and sorting. It provides a unified API for working with different entity types.

#### Key Features

- Generic interface parameterized with a context type `<C>` for additional operation information
- Type-safe methods for finding, saving, and deleting entities
- Support for sorting and pagination
- Integration with Spring Data JPA's Specification for filtering

#### Example Usage

```java
@Service
public class UserService {
    private final HyperRepository<UserContext> repository;
    
    // Constructor injection...
    
    public User findById(Long id, UserContext context) {
        return repository.findById(User.class, id, context);
    }
    
    public List<User> findAll(Specification<User> spec, Sort sort, UserContext context) {
        return repository.findAll(User.class, spec, sort, context);
    }
    
    public User save(User user, UserContext context) {
        return repository.save(user, context);
    }
}
```

### HyperMapper

The `HyperMapper` is a service for mapping between DTOs and entities. It provides methods for converting DTOs to entities and vice versa, handling relationships, and applying projections.

#### Key Features

- Bidirectional mapping between DTOs and entities
- Support for nested DTOs and entity relationships
- Designed to work with DTO projections to control which fields are included
- Handling of list operations (add, remove, remove) and item operations (insert, update, delete)

#### Example Usage

```java
@Service
public class UserService {
    private final HyperMapper hyperMapper;
    private final HyperRepository<UserContext> repository;
    
    // Constructor injection...
    
    public UserDto getUserDto(Long id, DtoProjection<UserDto> projection) {
        User user = repository.findById(User.class, id, new UserContext());
        return hyperMapper.mapEntityToDto(user, UserDto.class, projection);
    }
    
    public User createUser(UserDto userDto) {
        return hyperMapper.persistDtoToEntity(userDto, new UserContext());
    }
}
```

### Argument Resolvers

#### RSQL Filter Argument Resolver

The `RsqlFilterArgumentResolver` resolves [RSQL](https://github.com/jirutka/rsql-parser) filter strings in HTTP requests to `Specification` objects.

```java
@GetMapping("/users")
public List<UserDto> getUsers(@RequestParam(name = "filter", required = false) Specification<User> filter) {
    // The filter is automatically resolved from the RSQL string in the request parameter
    return userService.findAll(filter);
}
```

#### Sort Argument Resolver

The `SortArgumentResolver` resolves sort parameters in HTTP requests to `Sort` objects.

```java
@GetMapping("/users")
public List<UserDto> getUsers(@RequestParam(name = "sort", required = false) Sort sort) {
    // sort is automatically resolved from the request parameter
    return userService.findAll(sort);
}
```

Supported formats:
1. Field-direction format: `field1:asc,field2:desc`
2. Prefix format: `+field1,-field2`

#### DTO Projection Return Argument Resolver

The `DtoProjectionReturnArgumentResolver` resolves `DtoProjection` parameters in controller methods by extracting projection information from the method's return type annotation.

```java
@GetMapping("/users/{id}")
@DtoProjectionSpec("id, name, email, roles{id, name}")
public UserDto getUser(@PathVariable Long id, DtoProjection<UserDto> resultProjection) {
    // The projection is automatically resolved from the method's annotation
    User user = userService.findById(id);
    return hyperMapper.mapEntityToDto(user, UserDto.class, resultProjection);
}
```


### Using Projections with Spring Controllers

#### Projecting request parameters

The `DtoProjectionRequestBodyAdvice` applies projections defined in `@DtoProjectionSpec` annotations to method arguments.

```java
@PostMapping("/users")
public UserDto createUser(@DtoProjectionSpec(projectedClass = UserDto.class, value = "id, name, email") 
                          @RequestBody UserDto userDto) {
    return userService.createUser(userDto);
}
```

#### Response Projections

The `DtoProjectionResponseBodyAdvice` applies projections defined in `@DtoProjectionSpec` annotations to DTOs returned by controllers:

```java
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;

@GetMapping("/current-user/")
@DtoProjectionSpec("id, name, email, roles{id, name}")
public UserDto getUser() {
    UserDto currentUser;    
    // Retrieve the user and map it to a dto...    
    return currentUser;
}
```

#### Custom Projection Annotations

For frequently used projections, you can create custom annotations meta-annotated with `@DtoProjectionSpec`:

```java
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@DtoProjectionSpec(projectedClass = UserDto.class, value = "id, name, email")
public @interface UserProjection {}
```

Then use them in your controllers:

```java
@PostMapping("/users/")
@UserProjection
public UserDto updateUser(UserDto userDto) {
    return userService.createUser(userDto);
}
```

### SpringDoc OpenAPI Integration

The Hyperkit Utils Spring module provides integration with SpringDoc OpenAPI to take `ValueWrapper` and projections into account when building the API documentation for your REST endpoints.


## Related Modules

- [hyperkit-dto-api](../hyperkit-dto-api/README.md): Core API for DTOs
- [hyperkit-projections-dsl](../hyperkit-projections-dsl/README.md): DSL for defining projections
- [hyperkit-spring-boot-starter](../hyperkit-spring-boot-starter/README.md): Spring Boot starter that automatically configures these utilities