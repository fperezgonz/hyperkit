# Hyperkit Utils Spring Jackson3

## Overview
This module provides Jackson3-specific utilities that ensure that field aliases in projections declared in controllers are taken into account during the serialization and deserialization processes.

## Key Features
- `ProjectionAwareJacksonJsonConverter`: A `JacksonJsonHttpMessageConverter` subclass that handles projection's field aliases during serialization and deserialization.

## Related Modules

- [spring-web](../web/README.md): Web-specific utilities that use this converter
- [hyperkit-dto-api](../../hyperkit-dto-api/README.md): Core API for DTOs
- [hyperkit-projections-dsl](../../hyperkit-projections-dsl/README.md): DSL for defining projections