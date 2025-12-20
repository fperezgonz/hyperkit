# Entity Generation Example

This project demonstrates how to use the `hyperkit-entity-generator-plugin` to generate JPA entity classes from an existing database schema.

## Overview

This project uses the following components:
- **HSQLDB**: An embedded HSQLDB database.
- **Liquibase**: Manage the database schema.
- **HyperKit Entity Generator**: Generates JPA entities based on the database tables.

## How to Run this project

To generate the entities, follow these steps:
1. Run `gradlew :updateDatabase` to recreate the HSQLDB database and build the database schema.
2. Run `gradlew :generateEntities` to generate the entities from the database schema.
3. The generated entities will be available in `src/main/java/solutions/sulfura/hyperkit/examples/entities`.

## Configuration

The plugin is configured in the `build.gradle.kts` file, on the block `hyperKitEntityGenerator`

## Sample Schema

The sample schema defined in `src/main/resources/db/changelog/db.changelog-master.sql` includes:
- `users` table
- `posts` table with a foreign key to `users`
