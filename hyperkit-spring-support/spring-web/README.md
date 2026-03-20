# Hyperkit Utils Spring Web

## Overview
The Hyperkit Utils Spring Web module provides web-specific utilities for working with DTOs, projections, and common request parameters in Spring MVC.

## Key Features
- Argument resolvers for RSQL filters, sorting, and DTO projections.
- Request and Response body advice for applying projections.
- Support for standardized DTO request and response envelopes.

## Components

### Argument Resolvers

#### RSQL Filter Argument Resolver

The `RsqlFilterArgumentResolver` resolves [RSQL](https://github.com/jirutka/rsql-parser) filter strings in HTTP requests to Spring Data JPA `Specification` objects.

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

The `DtoProjectionRequestBodyAdvice` applies projections defined in `@DtoProjectionSpec` annotations to method arguments before they reach the controller.

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

## Related Modules

- [spring-jackson2](../spring-jackson2/README.md): Jackson converter used for serialization
- [hyperkit-dto-api](../../hyperkit-dto-api/README.md): Core API for DTOs
- [hyperkit-projections-dsl](../../hyperkit-projections-dsl/README.md): DSL for defining projections