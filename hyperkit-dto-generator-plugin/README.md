# Hyperkit DTO Generator Plugin

## Overview
The Hyperkit DTO Generator Plugin is a Gradle plugin that automatically generates Data Transfer Object (DTO) classes from annotated source classes. This plugin simplifies the creation and maintenance of DTOs by generating them at build time based on annotations in your domain model.

## Key Features
- Automatic generation of DTO classes from annotated source classes
- Customizable output paths, packages and template for generated code
- Support for property customization through annotations

## Usage

### Adding the Plugin

Add the plugin repository to your Gradle settings file:
```kotlin
pluginManagement {
    repositories {
        maven{
            url = uri("https://public-package-registry.sulfura.solutions/")
        }
        // Other repositories...
    }
}
```

Add the dependencies repository, the dependencies and the plugin to your Gradle build script:

```kotlin
plugins {
    id("solutions.sulfura.hyperkit-dto-generator-plugin") version "latest.version"
}

repositories {
    maven {
        url = uri("https://public-package-registry.sulfura.solutions/")
    }
    // Other repositories... 
}

dependencies {
    implementation("solutions.sulfura:hyperkit-dto-api:6.2.0-SNAPSHOT")
    // Other dependencies...
}
```

### Configuring the Plugin

Configure the plugin using the `hyperKitDtoGenerator` extension:

```kotlin
hyperKitDtoGenerator {
    // Paths to the input source files (default: ["src/main/java/"])
    inputPaths = setOf("src/main/java/")

    // Output path for the generated sources (default: "src/main/java/")
    rootOutputPath = "src/main/java/"

    // Default package where the generated DTOs will be placed (default: "solutions.sulfura.hyperkit.dtos")
    defaultOutputPackage = "solutions.sulfura.hyperkit.dtos"
}
```

### Annotating Source Classes

Annotate the classes to be processed by the plugin with `@Dto`

```java
@Dto
public class User {
    public Long id;
    public String name;
    public String email;
    public Set<AuthRole> roles;

}
```

### Running the Plugin

To generate the DTOs, run the `generateDtos` task:

```bash
./gradlew generateDtos
```

This will process all classes annotated with `@Dto` and generate the corresponding DTO classes in the configured output directory.

## Example

Given a domain class annotated with `@Dto`:

```java
package solutions.sulfura.hyperkit.examples.app;

import solutions.sulfura.hyperkit.dtos.annotations.Dto;

import java.util.Set;

@Dto
public class User {
    public Long id;
    public String name;
    public String email;
    public Set<AuthRole> roles;

}

```

The plugin will generate a `UserDto` class similar to this:

```java
package solutions.sulfura.hyperkit.dtos;

import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf.Presence;
import solutions.sulfura.hyperkit.examples.app.User;

import java.util.Objects;
import java.util.Set;

@DtoFor(User.class)
public class UserDto implements Dto<User> {

    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<String> email = ValueWrapper.empty();
    public ValueWrapper<Set<ListOperation<AuthRoleDto>>> roles = ValueWrapper.empty();

    // Constructor, getters and setters, builder...

    @ProjectionFor(UserDto.class)
    public static class Projection extends DtoProjection<UserDto> {

        public FieldConf id;
        public FieldConf name;
        public FieldConf email;
        public DtoListFieldConf<AuthRoleDto.Projection> roles;

        public Projection() {
        }

        public void applyProjectionTo(UserDto dto) throws DtoProjectionException {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.email = ProjectionUtils.getProjectedValue(dto.email, this.email);
            dto.roles = ProjectionUtils.getProjectedValue(dto.roles, this.roles);
        }

        // Hashcode, equals and Builder...

    }

    public static class DtoModel {

        public static final String _id = "id";
        public static final String _name = "name";
        public static final String _email = "email";
        public static final String _roles = "roles";

    }

}
```



If you only want to generate a subset of the properties of a class, you can configure the @Dto to include only properties with specific annotations:

Example: this would generate a Dto that only has the `id` and `roles` properties:
```java
@Dto(include = DtoProperty.class)
public class User {
    @DtoProperty
    public Long id;
    public String name;
    public String email;
    @DtoProperty
    public Set<AuthRole> roles;

}
```


## Related Modules
- [hyperkit-dto-api](../hyperkit-dto-api/README.md): Core API for DTOs, including annotations used by this plugin