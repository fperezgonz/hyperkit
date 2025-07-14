# Hyperkit Projections DSL

## Overview
The Hyperkit Projections DSL module provides a domain-specific language for defining projections on DTOs and vice versa. Projections allow you to specify which fields should be included when mapping entities to DTOs, enabling fine-grained control over data transfer and reducing unnecessary data transmission.

## Key Features
- Simple, expressive syntax for defining projections
- Support for nested projections
- The hyperkit-utils-spring module enables integration with Spring controllers and Hibernate

## Usage

### Defining Projections

Projections are defined using a simple, expressive syntax:

```
"field1, field2, nestedObject{nestedField1, nestedField2}"
or
field
field
nestedObject{
    nestedField1 {id, name}
    nestedField2}
```

This syntax allows you to:
- Include simple fields (`field1, field2`)
- Include nested object fields with their own projections (`nestedObject{nestedField1, nestedField2}`)
- Include collection fields with projections applied to each item


### Programmatic Projection Creation

You can create and apply projections programmatically:

```java
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;

// Create a projection from a string specification
DtoProjection<UserDto> projection = ProjectionDsl.parse("id, name, email, roles{id, name}", UserDto.class);

// Apply the projection
UserDto userDto = hyperMapper.mapEntityToDto(user, UserDto.class, projection);
```

## Related Modules
- [hyperkit-dto-api](../hyperkit-dto-api/README.md): Core API for DTOs
- [hyperkit-utils-spring](../hyperkit-utils-spring/README.md): Spring utilities including HyperMapper for applying projections