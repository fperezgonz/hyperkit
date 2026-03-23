# Hyperkit Utils Spring Persistence

## Overview
The Hyperkit Utils Spring Persistence module provides Spring-specific utilities for handling repository operations and mapping between DTOs and entities.

## Key Features
- `HyperRepository`: A generic repository interface for CRUD operations, RSQL queries, pagination, and sorting.
- `HyperMapper`: A service for mapping between DTOs and entities, handling relationships, and applying projections.
- Support for context-aware operations.

## Components

### HyperRepository

The `HyperRepository` is a generic repository interface for basic CRUD operations, RSQL queries, pagination, and sorting. It provides a unified API for working with different entity types.

#### Key Features

- Generic interface parameterized with a context type `<C>` for additional operation information.
- Type-safe methods for finding, saving, and deleting entities.
- Support for sorting and pagination.
- Integration with Spring Data JPA's Specification for filtering.

#### Example Usage

```java
@Service
public class UserService {
    private final HyperRepository<UserContext> repository;
    
    // Constructor injection...
    
    public User findById(Long id, UserContext context) {
        return repository.findById(User.class, id, context);
    }
    
    public List<User> findAll(Specification<User> spec, Sort sort, UserContext context) {
        return repository.findAll(User.class, spec, sort, context);
    }
    
    public User save(User user, UserContext context) {
        return repository.save(user, context);
    }
}
```

### HyperMapper

The `HyperMapper` is a service for mapping between DTOs and entities. It provides methods for converting DTOs to entities and vice versa, handling relationships, and applying projections.

#### Key Features

- Bidirectional mapping between DTOs and entities.
- Support for nested DTOs and entity relationships.
- Works with DTO projections to control which fields are included.
- Handling of list operations (add, remove) and item operations (insert, update, delete).

#### Example Usage

```java
@Service
public class UserService {
    private final HyperMapper hyperMapper;
    private final HyperRepository<UserContext> repository;
    
    // Constructor injection...
    
    public UserDto getUserDto(Long id, DtoProjection<UserDto> projection) {
        User user = repository.findById(User.class, id, new UserContext());
        return hyperMapper.mapEntityToDto(user, UserDto.class, projection);
    }
    
    public User createUser(UserDto userDto) {
        return hyperMapper.persistDtoToEntity(userDto, new UserContext());
    }
}
```

## Related Modules

- [hyperkit-dto-api](../../hyperkit-dto-api/README.md): Core API for DTOs
- [hyperkit-projections-dsl](../../hyperkit-projections-dsl/README.md): DSL for defining projections