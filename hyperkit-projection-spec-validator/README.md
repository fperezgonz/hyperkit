# Hyperkit Projection Spec Validator

The Hyperkit Projection Spec Validator is a tool for validating `@DtoProjectionSpec` annotations in your project. It ensures that the projections defined using the Hyperkit DSL are both syntactically correct and type-safe

## Features

- **DSL Validation**: Checks if the projection string follows the Hyperkit DSL syntax
- **Type Safety**: Verifies that the projected fields exist in the target DTO and that the projection is assignable to the method return type or parameter type
- **Classpath Scanning**: Can automatically scan packages to validate all `@DtoProjectionSpec` annotations

The validator checks for common mistakes like:

1. **Syntax errors in the DSL string**:
   ```java
   // Missing closing brace
   @DtoProjectionSpec(projectedClass = UserDto.class, value = "id {")
   public void someMethod() {}
   ```

2. **Missing properties**:
   Checks if the properties defined in the DSL actually exist in the `projectedClass`
   ```java
   // Non-existent property
   @DtoProjectionSpec(projectedClass = UserDto.class, value = "{ missingProperty }")
   public void someMethod() {}
   ```

3. **Type mismatch**:
   ```java
   // UserDto is not assignable to String
   @DtoProjectionSpec(projectedClass = UserDto.class, value = "id")
   public String someMethod() { ... }
   ```

## Usage

Add the dependency to your project:

#### Gradle

```kotlin
testImplementation("solutions.sulfura:hyperkit-projection-spec-validator:latest.version")
```

#### Maven

```xml
<dependency>
    <groupId>solutions.sulfura</groupId>
    <artifactId>hyperkit-projection-spec-validator</artifactId>
    <version>latest.version</version>
</dependency>
```

Use the `DtoProjectionSpecValidator` class to validate specific classes or entire packages

```java
class ProjectionValidationTest {

   @Test
   void validateProjections() {
      DtoProjectionSpecValidator validator = new DtoProjectionSpecValidator();
      List<String> errors = validator.validateAll("com.example.controllers");
      assertTrue(errors.isEmpty(), "Projection errors found: " + errors);
   }

}

```
