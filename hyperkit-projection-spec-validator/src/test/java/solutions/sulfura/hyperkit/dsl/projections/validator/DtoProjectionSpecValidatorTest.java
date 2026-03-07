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

class DtoProjectionSpecValidatorTest {

    private final DtoProjectionSpecValidator validator = new DtoProjectionSpecValidator();

    @Test
    @DisplayName("Should validate successfully with valid DtoProjectionSpec")
    void shouldValidateWithValidDtoProjectionSpec() {
        // Given
        class ValidProjection {
            @DtoProjectionSpec(projectedClass = UserDto.class, value = "id, username")
            public UserDto someMethod() {
                return null;
            }
        }

        // When
        List<String> errors = validator.validate(ValidProjection.class);

        // Then
        assertTrue(errors.isEmpty(), "Expected no errors, but found: " + errors);
    }

    @Test
    @DisplayName("Should fail validation with invalid DSL string")
    void shouldFailWithInvalidDslString() {
        // Given a method with an invalid projection dsl string
        class InvalidDsl {
            @DtoProjectionSpec(projectedClass = UserDto.class, value = "id {")
            public void someMethod() {}
        }

        // When
        List<String> errors = validator.validate(InvalidDsl.class);

        // Then
        assertFalse(errors.isEmpty(), "Expected errors for invalid DSL string");
        assertTrue(errors.get(0).contains("Failed to parse projection"), "Error message should mention parsing failure");
    }

    @Test
    @DisplayName("Should validate multiple classes")
    void shouldValidateMultipleClasses() {
        // Given
        class ValidProjection1 {
            @DtoProjectionSpec(projectedClass = UserDto.class, value = "id")
            public UserDto method1() {
                return null;
            }
        }

        class ValidProjection2 {
            @DtoProjectionSpec(projectedClass = UserDto.class, value = "username")
            public UserDto method2() {
                return null;
            }
        }

        // When
        List<String> errors = validator.validate(ValidProjection1.class, ValidProjection2.class);

        // Then
        assertTrue(errors.isEmpty(), "Expected no errors for multiple valid classes");
    }

    @Test
    @DisplayName("Should fail when projected class is not assignable to method return type")
    void shouldFailWhenNotAssignableToReturnType() {
        // Given
        class InvalidReturnType {
            @DtoProjectionSpec(projectedClass = UserDto.class, value = "id")
            public String someMethod() { return null; }
        }

        // When
        List<String> errors = validator.validate(InvalidReturnType.class);

        // Then
        assertFalse(errors.isEmpty(), "Expected errors for invalid return type");
        assertTrue(errors.get(0).contains("is not assignable to"), "Error message should mention assignability failure");
    }

    @Test
    @DisplayName("Should fail when projected class is not assignable to parameter type")
    void shouldFailWhenNotAssignableToParameterType() {
        // Given
        class InvalidParameterType {
            public void someMethod(@DtoProjectionSpec(projectedClass = UserDto.class, value = "id") String param) {}
        }

        // When
        List<String> errors = validator.validate(InvalidParameterType.class);

        // Then
        assertFalse(errors.isEmpty(), "Expected errors for invalid parameter type");
        assertTrue(errors.get(0).contains("is not assignable to"), "Error message should mention assignability failure");
    }

    @Test
    @DisplayName("Should validate when projected class is super type of type argument in a generic parameter")
    void shouldValidateWithGenericTypeArgumentOfProjectedClass() {
        // Given
        class ValidGenericTypeArgument {
            @DtoProjectionSpec(projectedClass = UserDto.class, value = "id")
            public List<UserDto> someMethod() { return null; }
        }

        // When
        List<String> errors = validator.validate(ValidGenericTypeArgument.class);

        // Then
        assertTrue(errors.isEmpty(), "Expected no errors for valid generic type");
    }

    @Test
    @DisplayName("Should fail when projected class is not a super type of the type argument in a generic parameter")
    void shouldFailWithoutGenericTypeArgumentOfProjectedClass() {
        // Given
        class InvalidGenericTypeArgument {
            @DtoProjectionSpec(projectedClass = UserDto.class, value = "id")
            public List<solutions.sulfura.hyperkit.utils.test.model.dtos.AccountDto> someMethod() { return null; }
        }

        // When
        List<String> errors = validator.validate(InvalidGenericTypeArgument.class);

        // Then
        assertFalse(errors.isEmpty(), "Expected errors for invalid generic type");
    }

    @Test
    @DisplayName("Should validate all classes in a package without invalid annotations")
    void shouldValidateAllClassesInPackageWithoutInvalidAnnotations() {
        // When
        // Validating the package containing our test models, which should be valid
        List<String> errors = validator.validateAll("solutions.sulfura.hyperkit.utils.test.model.dtos", "solutions.sulfura.hyperkit.examples.model");

        // Then
        assertTrue(errors.isEmpty(), "Expected no errors for scanned packages, but found: " + errors);
    }

    @Test
    @DisplayName("Should fail to validate for package with invalid annotations")
    void shouldFailForPackageWithInvalidAnnotations() {
        // When
        // Validating the package containing this same class, which has several invalid annotations
        List<String> errors = validator.validateAll("solutions.sulfura.hyperkit.dsl.projections.validator");

        // Then
        assertFalse(errors.isEmpty(), "Expected errors for scanned packages, but none were found");
    }

    @Test
    @DisplayName("Should validate when nested argument type is assignable to projected class")
    void shouldValidateWhenNestedArgumentTypeIsAssignableToProjectedClass() {

        // Given parameter with a deeply nested type argument that is assignable to the projected class
        class ValidNestedTypeArgument {
            public void someMethod(@DtoProjectionSpec(projectedClass = UserDto.class, value = "id") List<Optional<ValueWrapper<UserDto>>> optionalUserDtoList) {
            }
        }

        // When
        List<String> errors = validator.validate(ValidNestedTypeArgument.class);

        // Then
        assertTrue(errors.isEmpty());
    }

}
