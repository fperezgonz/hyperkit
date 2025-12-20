# Spring Boot Controller Example

This project demonstrates how to use **Hyperkit** in a Spring Boot application to handle DTO mapping and persistence using a standard Controller-Service-Repository pattern (for simplicity, in this example, logic is handled directly in the Controller).

## Overview

The example showcases several key features of Hyperkit:

- **HyperMapper**: Used to map between Entities and DTOs seamlessly. It supports automatic persistence of DTOs into entities.
- **DTO Projections**: Demonstrates how to use `@DtoProjectionSpec` and custom annotations (like `@StdUserResponseProjection`) to define which fields should be included in the API requests or responses, avoiding the need for multiple DTO classes for different views.
- **HyperRepository**: A thin wrapper around Spring Data JPA that provides convenient methods for common operations like finding all entities with specifications and paging.
- **RSQL Integration**: Shows how to use RSQL for dynamic filtering in GET requests.

The project uses an embedded **HSQLDB** database, so no external database setup is required.

## How to Run the Project

To start the Spring Boot application, run the following command from the project root:

```powershell
./gradlew :bootRun
```

The server will start on `http://localhost:8080`.
### OpenAPI Documentation

Once the application is running, you can access the OpenAPI UI (Swagger) at:
`http://localhost:8080/swagger-ui.html` (Note: In this version it might be at `http://localhost:8080/v3/api-docs` or similar depending on the exact springdoc configuration).

## How to Test the API

The project includes API client files for [Bruno](https://github.com/usebruno/bruno), located in the `api-client-files` directory.
