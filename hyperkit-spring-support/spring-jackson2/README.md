# Hyperkit Utils Spring Jackson2

## Overview
This module provides Jackson2-specific utilities that ensure that field aliases in projections declared in controllers are taken into account during the serialization and deserialization processes.

## Key Features
- `ProjectionAwareJacksonConverter`: A `MappingJackson2HttpMessageConverter` subclass that handles projection's field aliases during serialization and deserialization.
- Seamless integration with Spring's `HttpMessageConverter` mechanism.

## Related Modules

- [spring-web](../spring-web/README.md): Web-specific utilities that use this converter
- [hyperkit-dto-api](../../hyperkit-dto-api/README.md): Core API for DTOs
- [hyperkit-projections-dsl](../../hyperkit-projections-dsl/README.md): DSL for defining projections