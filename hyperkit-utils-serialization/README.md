# Hyperkit Utils Serialization

## Overview
The Hyperkit Utils Serialization module provides utilities for serializing and deserializing DTOs and value wrappers. It includes Jackson modules and adapters to handle Hyperkit-specific types and structures.

## Key Features
- Jackson modules for DTO and ValueWrapper serialization

## Usage

### Jackson Module Integration

The `DtoJacksonModule` and `ValueWrapperJacksonModule` can be registered with an ObjectMapper to handle Hyperkit-specific types:

```java
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new DtoJacksonModule());
mapper.registerModule(new ValueWrapperJacksonModule(new ValueWrapperAdapterImpl()));

// Now the mapper can handle Hyperkit types
UserDto userDto = mapper.readValue(json, UserDto.class);
String json = mapper.writeValueAsString(userDto);
```

### Spring Boot Integration

When using the Hyperkit Spring Boot Starter, the Jackson modules are automatically registered with Spring's ObjectMapper

## Related Modules
- [hyperkit-dto-api](../hyperkit-dto-api/README.md): Core API for DTOs and value wrappers
- [hyperkit-spring-boot-starter](../hyperkit-spring-boot-starter/README.md): Spring Boot starter that automatically configures serialization