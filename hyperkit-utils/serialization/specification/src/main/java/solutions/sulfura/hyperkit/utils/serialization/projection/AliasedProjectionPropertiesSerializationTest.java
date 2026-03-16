package solutions.sulfura.hyperkit.utils.serialization.projection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.serialization.SerializationProvider;
import solutions.sulfura.hyperkit.utils.test.model.dtos.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for serialization and deserialization of projected dtos with field name aliasing.
 */
public abstract class AliasedProjectionPropertiesSerializationTest {

    private SerializationProvider serializationProvider;

    protected abstract SerializationProvider getSerializationProvider();

    @BeforeEach
    void setUp() {
        serializationProvider = getSerializationProvider();
    }

    @Test
    void serializeDtoWithAliasedSimpleProperty() {
        // Given
        AuthorizationDto.Projection projection = ProjectionDsl.parse("name as code", AuthorizationDto.Projection.class);
        AuthorizationDto dto = new AuthorizationDto.Builder()
                .name(ValueWrapper.of("AdminAuth"))
                .build();

        // When
        String result = serializationProvider.write(dto, projection);
        // Then
        assertEquals("""
                {"code":"AdminAuth"}""", result);
    }

    @Test
    void serializeDtoWithAliasedDtoProperty() {
        // Given
        AuthorizationDto.Projection projection = ProjectionDsl.parse("role as rl { name }", AuthorizationDto.Projection.class);
        AuthorizationDto dto = new AuthorizationDto.Builder()
                .role(ValueWrapper.of(RoleDto.Builder.newInstance()
                        .name(ValueWrapper.of("AdminRole"))
                        .build()))
                .build();
        // When
        String result = serializationProvider.write(dto, projection);
        // Then
        assertEquals("""
                {"rl":{"name":"AdminRole"}}""", result);
    }

    @Test
    void serializeDtoWithAliasedListProperty() {
        // Given
        AuthorizationDto.Projection projection = ProjectionDsl.parse("resourceReferences resources { name }", AuthorizationDto.Projection.class);
        AuthorizationDto dto = AuthorizationDto.Builder.newInstance()
                .resourceReferences(ValueWrapper.of(Set.of(ListOperation.valueOf(
                        ResourceReferenceDto.Builder.newInstance()
                                .name(ValueWrapper.of("MainResource"))
                                .build()
                ))))
                .build();
        // When
        String result = serializationProvider.write(dto, projection);
        // Then
        // Then
        assertEquals("""
                {"resources":[{"operationType":"NONE","itemOperationType":"NONE","value":{"name":"MainResource"}}]}""", result);
    }

    @Test
    void serializeDtoWithNestedAliasedSimpleProperty() {
        // Given
        AuthorizationDto.Projection projection = ProjectionDsl.parse("role { name as code }", AuthorizationDto.Projection.class);
        AuthorizationDto dto = new AuthorizationDto.Builder()
                .role(ValueWrapper.of(RoleDto.Builder.newInstance()
                        .name(ValueWrapper.of("AdminRole"))
                        .build()))
                .build();
        // When
        String result = serializationProvider.write(dto, projection);
        // Then
        assertEquals("""
                {"role":{"code":"AdminRole"}}""", result);
    }

    @Test
    void serializeDtoWithNestedAliasedDtoProperty() {
        // Given
        UserDto.Projection projection = ProjectionDsl.parse("authorizations { role rl { name } }", UserDto.Projection.class);
        UserDto dto = UserDto.Builder.newInstance()
                .authorizations(ValueWrapper.of(Set.of(ListOperation.valueOf(AuthorizationDto.Builder.newInstance()
                        .role(ValueWrapper.of(RoleDto.Builder.newInstance()
                                .name(ValueWrapper.of("AdminRole"))
                                .build()))
                        .build()
                ))))
                .build();
        // When
        String result = serializationProvider.write(dto, projection);
        // Then
        assertEquals("""
                {"authorizations":[{"operationType":"NONE","itemOperationType":"NONE","value":{"rl":{"name":"AdminRole"}}}]}""", result);
    }

    @Test
    void serializeDtoWithNestedAliasedListProperty() {
        // Given
        AuthorizationDto.Projection projection = ProjectionDsl.parse("role { actions as perms { id } }", AuthorizationDto.Projection.class);
        AuthorizationDto dto = new AuthorizationDto.Builder()
                .role(ValueWrapper.of(RoleDto.Builder.newInstance()
                        .actions(ValueWrapper.of(Set.of(ListOperation.valueOf(
                                ActionDto.Builder.newInstance()
                                        .id(ValueWrapper.of("Users$READ"))
                                        .build()
                        ))))
                        .build()))
                .build();
        // When
        String result = serializationProvider.write(dto, projection);
        // Then
        assertEquals("""
                {"role":{"perms":[{"operationType":"NONE","itemOperationType":"NONE","value":{"id":"Users$READ"}}]}}""", result);
    }


}
