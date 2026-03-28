package solutions.sulfura.hyperkit.dsl.projections.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.test.model.dtos.UserDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NamespaceCollisionsTest {

    private final DtoProjectionSpecValidator validator = new DtoProjectionSpecValidator();

    @Test
    @DisplayName("Should fail to validate when different projections with the same type and without an explicit namespace are declared")
    void shouldFailWhenMultipleSameTypeDifferentProjectionsWithoutNamespaceAndWithoutAlias() {

        // Given two different projections without namespace for the same type
        class Container {
            class One {
                public void someMethod(@DtoProjectionSpec(projectedClass = UserDto.class, value = "id") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                }
            }

            class Two {
                public void someMethod(@DtoProjectionSpec(projectedClass = UserDto.class, value = "id, email") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                }
            }
        }

        // When
        List<String> errors = validator.validate(Container.class);

        // Then
        assertFalse(errors.isEmpty());
    }

    @Test
    @DisplayName("Should fail to validate when different projections with the same alias and without an explicit namespace are declared")
    void shouldFailWhenMultipleDifferentProjectionsWithoutNamespaceWithSameAlias() {

        // Given two different projections without namespace for the same type
        class Container {
            class One {
                public void someMethod(@DtoProjectionSpec(projectedClass = UserDto.class, value = "{id}:Alias1") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                }
            }

            class Two {
                public void someMethod(@DtoProjectionSpec(projectedClass = UserDto.class, value = "{id, email}:Alias1") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                }
            }
        }

        // When
        List<String> errors = validator.validate(Container.class);

        // Then
        assertFalse(errors.isEmpty());
    }

    @Test
    @DisplayName("Should fail to validate when different projections with the same type and same namespace are declared")
    void shouldFailWhenMultipleDifferentProjectionsWithSameTypeAndSameNamespaceAndWithoutAlias() {

        // Given two different projections without namespace for the same type
        class Container {
            class One {
                public void someMethod(@DtoProjectionSpec(namespace = "default", projectedClass = UserDto.class, value = "id") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                }
            }

            class Two {
                public void someMethod(@DtoProjectionSpec(namespace = "default", projectedClass = UserDto.class, value = "id, email") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                }
            }
        }

        // When
        List<String> errors = validator.validate(Container.class);

        // Then
        assertFalse(errors.isEmpty());
    }

    @Test
    @DisplayName("Should fail to validate when different projections with the same alias and same namespace are declared")
    void shouldFailWhenMultipleDifferentProjectionsWithSameNamespaceAndSameAlias() {

        // Given two different projections without namespace for the same type
        class Container {
            class One {
                public void someMethod(@DtoProjectionSpec(namespace = "default", projectedClass = UserDto.class, value = "{id}:Alias1") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                }
            }

            class Two {
                public void someMethod(@DtoProjectionSpec(namespace = "default", projectedClass = UserDto.class, value = "{id, email}:Alias1") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                }
            }
        }

        // When
        List<String> errors = validator.validate(Container.class);

        // Then
        assertFalse(errors.isEmpty());
    }

    @Test
    @DisplayName("Should fail to validate when different nested projections with the same alias and same namespace are declared")
    void shouldFailWhenMultipleDifferentNestedProjectionsWithSameNamespaceAndSameAlias() {

        // Given two different projections without namespace for the same type
        class Container {
            class One {
                public void someMethod(@DtoProjectionSpec(namespace = "default", projectedClass = UserDto.class, value = "{id, account { id }:Alias1 }") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                }
            }

            class Two {
                public void someMethod(@DtoProjectionSpec(namespace = "default", projectedClass = UserDto.class, value = "{id, account { id, name }:Alias1 }") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                }
            }
        }

        // When
        List<String> errors = validator.validate(Container.class);

        // Then
        assertFalse(errors.isEmpty());
    }

    @Test
    @DisplayName("Should validate when equal projections with the same alias and same namespace are declared")
    void shouldValidateWhenMultipleEqualProjectionsWithSameNamespaceAndSameAlias() {

        // Given two different projections without namespace for the same type
        class Container {
            class One {
                public void someMethod(@DtoProjectionSpec(namespace = "default", projectedClass = UserDto.class, value = "{id}:Alias1") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                }
            }

            class Two {
                public void someMethod(@DtoProjectionSpec(namespace = "default", projectedClass = UserDto.class, value = "{id}:Alias1") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                }
            }
        }

        // When
        List<String> errors = validator.validate(Container.class);

        // Then
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Should validate when different projections are projected with the same alias in a different namespace")
    void shouldValidateWhenSameTypeAliasesDeclaredInDifferentNamespace() {

        // Given two projections for the same type and namespace
        class ParentContainer {
            class Container1 {
                public void someMethod(@DtoProjectionSpec(projectedClass = UserDto.class, value = "id") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                }
            }

            class Container2 {
                class Two {
                    public void someMethod(@DtoProjectionSpec(projectedClass = UserDto.class, value = "id, email") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                    }
                }
            }
        }

        // When
        List<String> errors = validator.validate(ParentContainer.class);

        // Then
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Should validate when different projections are projected with different aliases in the same namespace")
    void shouldValidateWhenDifferentTypeAliasesDeclaredInSameNamespace() {

        // Given two projections for the same type and namespace
        class ParentContainer {
            class Container1 {
                public void someMethod(@DtoProjectionSpec(projectedClass = UserDto.class, value = "id") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                }
            }

            class Container2 {
                class Two {
                    public void someMethod(@DtoProjectionSpec(projectedClass = UserDto.class, value = "id, email") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
                    }
                }
            }
        }

        // When
        List<String> errors = validator.validate(ParentContainer.class);

        // Then
        assertTrue(errors.isEmpty());
    }

}
