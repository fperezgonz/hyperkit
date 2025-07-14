# Hyperkit Entity Generator

## Overview
The Hyperkit Entity Generator is a tool for generating JPA entity classes from database schemas. It simplifies the process of creating entity classes by automatically generating them based on database tables, columns, and relationships.

## Key Features
- Automatic generation of JPA entity classes from database schemas
- Support for various database types (MySQL, PostgreSQL, etc.)
- Customizable templates for entity generation

## Usage

### Command-Line Usage

The entity generator can be run as a command-line application:

```bash
java -jar hyperkit-entity-generator.jar --database-url=jdbc:mysql://localhost:3306/mydb --username=user --password=pass --output-dir=src/main/java --package=com.example.entities
```

### Configuration Properties

The entity generator supports various configuration properties:

```kotlin
val properties = EntityGeneratorProperties().apply {
    databaseUrl = "jdbc:mysql://localhost:3306/mydb"
    databaseUsername = "user"
    databasePassword = "pass"
    outputDirectory = "src/main/java"
    basePackage = "com.example.entities"
    generateJpaAnnotations = true
    generateLombokAnnotations = true
    tableNamePattern = "app_%"  // Only process tables with names starting with "app_"
    excludeTables = listOf("app_audit_log", "app_temp")
}

val generator = EntityGenerator(properties)
generator.generate()
```

### Gradle Plugin Usage

The entity generator can also be used as a Gradle plugin:

```gradle
plugins {
    id("solutions.sulfura.hyperkit-entity-generator") version "latest.version"
}

//Assuming the properties have been declared in gradle.properties
hyperKitEntityGenerator {
    databaseUrl.set(project.property("databaseUrl").toString()) // jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
    databaseUsername.set(project.property("databaseUsername").toString()) // username
    databasePassword.set(project.property("databasePassword").toString()) // password
    databaseDriver.set("org.h2.Driver")
    basePackage.set("solutions.sulfura.hyperkit.test.entities")
}
```

Then run the generation task:

```bash
./gradlew generateEntities
```

### Customizing Entity Generation

You can customize the entity generation process by providing a custom velocity template:

```gradle
plugins {
    id("solutions.sulfura.hyperkit-entity-generator") version "latest.version"
}

hyperKitEntityGenerator {
    databaseUrl.set(project.property("databaseUrl").toString()) // jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
    databaseUsername.set(project.property("databaseUsername").toString()) // username
    databasePassword.set(project.property("databasePassword").toString()) // password
    databaseDriver.set("org.h2.Driver")
    basePackage.set("solutions.sulfura.hyperkit.test.entities")
    templatePath.set("custom-template.vm") // Absolute or relative path of the template file
}
```

## Example Generated Entity

Here's an example of a generated entity class:

```java
package com.example.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "username", nullable = false, length = 50)
    private String username;
    
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "user")
    private List<Order> orders;
    
    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
}
```

## Related Modules
- [hyperkit-dto-api](../hyperkit-dto-api/README.md): Core API for DTOs that can be used with generated entities
- [hyperkit-dto-generator-plugin](../hyperkit-dto-generator-plugin/README.md): Plugin for generating DTOs from the entities created by this generator