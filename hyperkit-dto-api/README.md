# Hyperkit DTO API

## Overview
The Hyperkit DTO API module provides core interfaces and annotations for working with Data Transfer Objects (DTOs) in Java applications. It includes annotations for DTO generation, property customization, and utilities for handling value wrapping and list operations, and serves as the foundation for DTO-based operations in the Hyperkit framework.

This module defines the contract and metadata for DTOs without implementing the actual generation logic, which is handled by the [hyperkit-dto-generator-plugin](../hyperkit-dto-generator-plugin/README.md).

## Key Features
- **DTO Definition**: Core annotations and interfaces for declaring and configuring DTOs
- **Projection Support**: Interfaces and utilities for creating and managing dto projections
- **List Operations**: Support for managing entity relationships with add, remove, and delete operations on collections
- **Value Wrapping**: Utilities for wrapping values, allowing to differentiate null values from the absence of a value

## Related Modules
- [hyperkit-dto-generator-plugin](../hyperkit-dto-generator-plugin/README.md): Gradle plugin for generating DTOs from annotated classes
- [hyperkit-projections-dsl](../hyperkit-projections-dsl/README.md): DSL for creating and managing entity projections

## Contributing
Contributions to improve the Hyperkit DTO API are welcome. Please ensure your code follows the project's coding standards and includes appropriate tests