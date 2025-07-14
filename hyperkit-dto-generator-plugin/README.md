# Hyperkit DTO Generator Plugin

## Overview
The Hyperkit DTO Generator Plugin is a Gradle plugin that automatically generates Data Transfer Object (DTO) classes from annotated source classes. This plugin simplifies the creation and maintenance of DTOs by generating them at build time based on annotations in your domain model.

## Key Features
- Automatic generation of DTO classes from annotated source classes
- Customizable output paths, packages and template for generated code
- Support for property customization through annotations

## Usage

### Adding the Plugin

Add the plugin to your Gradle build script:

```gradle
plugins {
    id("solutions.sulfura.hyperkit-dto-generator-plugin") version "latest.version"
}
```

### Configuring the Plugin

Configure the plugin using the `hyperKitDtoGenerator` extension:

```gradle
hyperKitDtoGenerator {
    // Paths to the input source files (default: ["src/main/java/"])
    inputPaths = setOf("src/main/java/")

    // Output path for the generated sources (default: "src/main/java/")
    rootOutputPath = "src/main/java/"

    // Default package where the generated DTOs will be placed (default: "solutions.sulfura.hyperkit.dtos")
    defaultOutputPackage = "solutions.sulfura.hyperkit.dtos"
}
```

### Running the Plugin

To generate the DTOs, run the `generateDtos` task:

```bash
./gradlew generateDtos
```

This will process all classes annotated with `@Dto` and generate the corresponding DTO classes in the configured output directory.

## Example

Given a domain class annotated with `@Dto`, that contains properties annotated with @DtoProperty:

```java
import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.dtos.annotations.DtoProperty;

@Dto
public class User {
    @DtoProperty
    public String username;    
    @DtoProperty
    public String emailAddress;
    private String password;
    
    // Getters and setters...
}
```

The plugin will generate a `UserDto` class similar to:

```java
package solutions.sulfura.hyperkit.dtos;
// imports...

@DtoFor(User.class)
public class UserDto extends Dto<User> {
    public String username = ValueWrapper.empty();
    public String emailAddress = ValueWrapper.empty();
    
    // Constructors, builders, equals, hashCode, toString methods...
    
    public static class Projection{
        public FieldConf username;
        public FieldConf emailAddress;
        
    }
    
    public Projection(){}

    public void applyProjectionTo(UserDto dto) throws DtoProjectionException {
        dto.username = ProjectionUtils.getProjectedValue(dto.username, this.username);
        dto.emailAddress = ProjectionUtils.getProjectedValue(dto.emailAddress, this.emailAddress);
    }

}
```


## Related Modules
- [hyperkit-dto-api](../hyperkit-dto-api/README.md): Core API for DTOs, including annotations used by this plugin