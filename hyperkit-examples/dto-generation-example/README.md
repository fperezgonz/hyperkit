# Dto Generation Example

This project demonstrates how to use the `hyperkit-dto-generator-plugin` to generate JPA dto classes from an existing database schema.

## Overview

This project uses the following components:
- **HyperKit Dto Generator**: Generates JPA dtos based on the entity classes tables.

## How to Run this project

To generate the dtos, follow these steps:
1. Run `gradlew :generateDtos` to generate the dtos from the entity classes.
2. The generated dtos will be available in `src/main/java/solutions/sulfura/hyperkit/dtos`.

## Configuration

No configuration is required to run this project; however, some plugin properties can be configured in the `build.gradle.kts` file, on the block `hyperKitDtoGenerator`

## Sample Schema

The sample schema defined in `src/main/resources/db/changelog/db.changelog-master.sql` includes:
- `users` table
- `posts` table with a foreign key to `users`
