# Hyperkit Projections DSL

## Overview
The Hyperkit Projections DSL module provides a domain-specific language for defining projections on DTOs and vice versa. 
Projections allow you to specify which fields should be included when mapping entities to DTOs or serializing and deserializing JSON strings,
simplifying the declaration of API endpoints.

## Key Features
- Simple, expressive syntax for defining projections
- Support for nested projections

## Usage

### Defining Projections

Projections are defined using a simple, expressive syntax. Given the following DTOs:
```java
class UserDto {
    public String name;
    public String email;
    public ValueWrapper<Set<ListOperation<RoleDto>>> roles;
}

class RoleDto {
    public String id;
    public String name;
    public GroupDto group;
}

class GroupDto {
    public String name;
}

```

You can define a projection that includes the following fields:
```
email, roles {id, group { name } }
```
or
```
id
name
email
roles {
    id
    group { name }
}
```

This syntax allows you to:
- Include simple properties (`email`)
- Include nested object properties with their own projections (`email, roles {id, group{ name } }`)
- Include collection properties with projections applied to each item
- Define aliases for properties, e.g.: `email as emailAddress` (the `as` keyword is optional)
- Define type aliases for projections, e.g.: `group :NamedGroup { name }` or `group{ name } :NamedGroup `
- Property aliases and type aliases support special characters using backticks (``roles :`Role Dto` { id}``)
- The backticks can be escaped using backslashes, e.g.: ``roles :`Role\`Dto` { id}`` would alias the roles projection type with the name ``Role`Dto``


### Programmatic Projection Creation

You can create and apply projections programmatically:

```java
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;

// Create a projection from a string specification
DtoProjection<UserDto> projection = ProjectionDsl.parse("id, email, roles {id, group { name } }", UserDto.class);

// Apply the projection
UserDto userDto = hyperMapper.mapEntityToDto(user, UserDto.class, projection);
```

## Related Modules
- [hyperkit-dto-api](../hyperkit-dto-api/README.md): Core API for DTOs
- [hyperkit-utils-spring](../hyperkit-utils-spring/README.md): Spring utilities including HyperMapper for applying projections