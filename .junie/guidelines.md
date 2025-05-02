# Java Style Guide for Hyperkit Project

## Introduction
This style guide outlines the coding conventions and best practices for the Hyperkit project. Following these guidelines ensures consistency, readability, and maintainability across the codebase.

## Table of Contents
1. [Code Organization](#code-organization)
2. [Naming Conventions](#naming-conventions)
3. [Documentation](#documentation)
4. [Annotations](#annotations)
5. [Exception Handling](#exception-handling)
6. [Architectural Patterns](#architectural-patterns)
7. [Generics](#generics)
8. [Collections](#collections)
9. [Null Safety](#null-safety)
10. [Common Pitfalls to Avoid](#common-pitfalls-to-avoid)
11. [Test Coverage](#test-coverage)

## Code Organization

### Package Structure
- Use reverse domain name notation: `solutions.sulfura.hyperkit.*`
- Organize packages by feature or component, not by layer
- Keep related classes in the same package

### Class Structure
- One top-level class per file
- Order class members as follows:
  1. Static fields
  2. Instance fields
  3. Constructors
  4. Methods
  5. Nested classes/interfaces

### Imports
- Group imports in the following order:
  1. Java standard library imports
  2. Third-party imports (e.g., Spring, Jakarta)
  3. Project imports
- Use static imports for utility methods when appropriate

## Naming Conventions

### General
- Use clear, descriptive names that convey purpose
- Avoid abbreviations unless they are widely understood

### Classes and Interfaces
- Use PascalCase (e.g., `HyperRepository`, `ValueWrapperAdapter`)
- Use nouns for classes (e.g., `HyperMapper`)
- Use adjectives or nouns for interfaces (e.g., `Serializable`)

### Methods
- Use camelCase (e.g., `findById`, `mapDtoToEntity`)
- Use verbs or verb phrases (e.g., `save`, `delete`)
- Prefix boolean methods with "is", "has", or "can" (e.g., `isEntity`, `isEmpty`)

### Variables
- Use camelCase (e.g., `entityManager`, `contextInfo`)
- Use meaningful names that indicate purpose and type
- Avoid single-letter variable names except for loop counters

### Constants
- Use UPPER_SNAKE_CASE (e.g., `MAX_RETRY_COUNT`)

## Documentation

### Javadoc
- Provide Javadoc for public classes and methods
- Include descriptions for parameters, return values, and exceptions
- Use HTML tags for formatting when necessary
- Example:
  ```java
  /**
   * Retrieves an entity from the repository based on its class and identifier.
   *
   * @param entityClass the class type of the entity to retrieve
   * @param entityId    the identifier of the entity to retrieve
   * @param contextInfo additional context information required for the repository operation
   * @param <T>         the type of the entity
   * @param <I>         the type of the identifier
   * @return the entity retrieved from the repository
   * @throws HyperMapperException if the entity is not found in the repository
   */
  ```

### Comments
- Use comments to explain complex logic or non-obvious behavior
- Keep comments up to date with code changes
- Avoid redundant comments that merely restate the code

## Annotations

### Common Annotations
- Use `@NonNull` from org.jspecify.annotations for parameters that must not be null
- Use `@Override` when implementing interface methods or overriding superclass methods
- Use `@SuppressWarnings` with specific warning types and explanatory comments
- Use Spring annotations appropriately:
  - `@Service` for service classes
  - `@Repository` for repository classes
  - `@Transactional` for methods that require transaction management
  - `@PersistenceContext` for injecting EntityManager

## Exception Handling

### Custom Exceptions
- Create custom exception classes for specific error scenarios
- Extend appropriate exception types (e.g., `RuntimeException` for unchecked exceptions)
- Provide constructors for different initialization scenarios

### Error Messages
- Use clear, descriptive error messages
- Include relevant context information in error messages
- Format complex error messages for readability

### Try-Catch Blocks
- Catch specific exceptions rather than general Exception
- Avoid empty catch blocks
- Properly propagate or wrap exceptions when necessary

## Architectural Patterns

### Dependency Injection
- Use constructor injection for required dependencies
- Avoid field injection with `@Autowired`
- Make dependencies final when possible

### Repository Pattern
- Use Spring Data repositories when appropriate
- Create custom repository interfaces for complex data access requirements
- Implement repository interfaces with JPA/Hibernate

### Service Layer
- Use service classes to encapsulate business logic
- Keep services focused on specific functionality
- Use interfaces for services when appropriate for testing or multiple implementations

### DTO Pattern
- Use DTOs to transfer data between layers
- Keep DTOs simple and focused on data transfer
- Use HyperMapper to convert between entities and DTOs

## Generics

### Type Parameters
- Use meaningful names for type parameters:
  - `T` for general types
  - `E` for element types
  - `K` for key types
  - `V` for value types
  - `R` for return types
- Use bounded type parameters when necessary (e.g., `<T extends Serializable>`)

### Wildcards
- Use `<?>` for unknown types
- Use `<? extends T>` for covariance (read-only)
- Use `<? super T>` for contravariance (write-only)

## Collections

### Collection Types
- Use interfaces for variable declarations (e.g., `List<T>` instead of `ArrayList<T>`)
- Choose appropriate collection types:
  - `List` for ordered collections
  - `Set` for unique elements
  - `Map` for key-value pairs

### Collection Operations
- Use Java 8+ stream operations for functional-style collection processing
- Use `forEach`, `map`, `filter`, etc. for cleaner code
- Consider performance implications for large collections

## Null Safety

### Null Handling
- Use `@NonNull` annotations to document non-null parameters and return values
- Validate parameters with explicit null checks when necessary
- Avoid returning null from methods when possible

### Optional Usage
- Use `Optional.ofNullable()` for values that might be null
- Use `Optional.empty()` for absent values
- Avoid using `Optional` as method parameters

## Common Pitfalls to Avoid

### Resource Management
- Always close resources in a finally block or use try-with-resources
- Avoid resource leaks by properly managing connections, streams, etc.

### Concurrency Issues
- Be aware of thread safety concerns
- Use thread-safe collections when necessary
- Avoid mutable static state

### Performance Considerations
- Avoid premature optimization
- Profile before optimizing
- Consider the performance impact of reflection operations

### Code Duplication
- Extract common code into utility methods
- Use inheritance or composition to avoid duplication
- Follow the DRY (Don't Repeat Yourself) principle

## Testing

### Test Organization
- Mirror the package structure of the main code
- Name test classes with "Test" suffix (e.g., `HyperMapperTest`)
- Group related tests in the same class

### Test Methods
- Add @DisplayName annotations to test methods, that succinctly describe what is being tested
- Follow the Given/When/Then pattern
- Add concise comments to explain the Given, When and Then in natural language
- Keep tests independent and isolated
- As a rule of thumb, avoid testing several things in the same method (e.g.: serialize and deserialize) and instead make several tests, unless it is a series of steps of the same process and the setup to be able to test the intermediate and final steps is very complicated

### Mocking
- Avoid mocking except for integration tests with external services
- Mock only what is necessary
- Avoid excessive mocking that leads to brittle tests

### Test Coverage
- The purpose of the tests is to serve as an executable specification
- Details of the specification that are obvious and very simple to implement (such as direct getters, setters and constructors) do not need tests. This reduces the cost of maintaining tests
- Do not chase high test coverage
- Keep the tests maintainable by limiting test coverage to key functionality
- Test the expected results, not the implementation details, e.g.: do not test the size and contents of enums
- Do not test simple constructors that do not have logic
- Do not test simple factory methods that do not have logic
- Test edge cases and error scenarios when there is complex logic involved
- Use parameterized tests for testing multiple inputs