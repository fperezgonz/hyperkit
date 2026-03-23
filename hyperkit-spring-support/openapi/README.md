# Hyperkit Utils Spring OpenAPI

## Overview
The Hyperkit Utils Spring OpenAPI module provides integration with [SpringDoc OpenAPI](https://springdoc.org/) to correctly represent projected types in your API documentation.

## Key Features
- Correct handling of `ValueWrapper` in OpenAPI schemas.
- Support for DTO projections in OpenAPI documentation
  - Only projected fields are shown in the schema
  - Type aliases declared in projections are correctly represented in the schema.

## Components

### SpringDoc OpenAPI Integration

The module provides several components to enhance OpenAPI documentation:

- `ValueWrapperModelConverter`: Ensures that `ValueWrapper<T>` is represented as the underlying type `T` in the OpenAPI schema.
- `ProjectionOpenApiCustomizer`: Adjusts the OpenAPI schemas for endpoints that use `@DtoProjectionSpec` or custom projection annotations.

## Related Modules

- [spring-web](../web/README.md): Web utilities that provide the integration with projections
- [hyperkit-dto-api](../../hyperkit-dto-api/README.md): Core API for DTOs
- [hyperkit-projections-dsl](../../hyperkit-projections-dsl/README.md): DSL for defining projections