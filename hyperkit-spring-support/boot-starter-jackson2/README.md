# HyperKit Jackson2 Boot Starter

Spring Boot starter providing support for HyperKit Jackson2 integration

## Overview

This starter automatically configures Jackson ObjectMapper with HyperKit-specific modules, and Adds a message converter to handle projection field aliases, enabling
seamless JSON serialization/deserialization with support for:

- Value wrapper objects
- DTO projections
- Projection field aliases

## Features

- **Automatic Jackson Module Registration**: Registers HyperKit-specific Jackson modules
    - `ValueWrapperJacksonModule`: Handles value wrapper serialization
    - `DtoJacksonModule`: Base DTO serialization support
    - `ProjectedDtoJacksonModule`: Projection-based DTO serialization

- **Projection-Aware Converters**: Custom HTTP message converters that support field projections
    - `ProjectionAwareJacksonConverter`: For Jackson2 (com.fasterxml.jackson)
    - `ProjectionAwareJacksonJsonConverter`: Disables ProjectionAwareJacksonJsonConverter

- **Auto-Configuration**: Zero-configuration setup via Spring Boot auto-configuration

