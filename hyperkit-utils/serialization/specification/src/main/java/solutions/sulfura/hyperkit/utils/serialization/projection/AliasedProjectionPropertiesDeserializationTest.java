package solutions.sulfura.hyperkit.utils.serialization.projection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.utils.serialization.DeserializationProvider;
import solutions.sulfura.hyperkit.utils.test.model.dtos.AuthorizationDto;
import solutions.sulfura.hyperkit.utils.test.model.dtos.UserDto;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for deserialization of projected dtos with field name aliasing.
 */
public abstract class AliasedProjectionPropertiesDeserializationTest {

    private DeserializationProvider deserializationProvider;

    protected abstract DeserializationProvider getDeserializationProvider();

    @BeforeEach
    void setUp() {
        deserializationProvider = getDeserializationProvider();
    }

    @Test
    void deserializeDtoWithAliasedSimpleProperty() throws IOException {
        // Given
        AuthorizationDto.Projection projection = ProjectionDsl.parse("name as code", AuthorizationDto.Projection.class);
        String sourceJson = """
                {"code":"AdminAuth"}""";


        // When
        AuthorizationDto result = deserializationProvider.read(sourceJson, AuthorizationDto.class, projection);

        // Then
        assertEquals("AdminAuth", result.name.get());
    }

    @Test
    void deserializeDtoWithAliasedSimplePropertyAndPropertyWithoutConfigInProjection() throws IOException {
        // Given
        AuthorizationDto.Projection projection = ProjectionDsl.parse("name", AuthorizationDto.Projection.class);
        String sourceJson = """
                {"id":"1", "name":"AdminAuth"}""";

        // When
        AuthorizationDto result = deserializationProvider.read(sourceJson, AuthorizationDto.class, projection);

        // Then
        assertEquals("AdminAuth", result.name.get());
    }

    @Test
    void deserializeDtoWithAliasedDtoProperty() throws IOException {
        // Given
        AuthorizationDto.Projection projection = ProjectionDsl.parse("role as rl { name }", AuthorizationDto.Projection.class);
        String sourceJson = """
                {"rl":{"name":"AdminRole"}}""";

        // When
        AuthorizationDto result = deserializationProvider.read(sourceJson, AuthorizationDto.class, projection);

        // Then
        assertEquals("AdminRole", result.role.get().name.get());
    }

    @Test
    void deserializeDtoWithAliasedListProperty() throws IOException {
        // Given
        AuthorizationDto.Projection projection = ProjectionDsl.parse("resourceReferences resources { name }", AuthorizationDto.Projection.class);
        String sourceJson = """
                {"resources":[{"operationType":"NONE","itemOperationType":"NONE","value":{"name":"MainResource"}}]}""";

        // When
        AuthorizationDto result = deserializationProvider.read(sourceJson, AuthorizationDto.class, projection);

        // Then
        assertEquals("MainResource", result.resourceReferences.get().iterator().next().getValue().name.get());
    }

    @Test
    void deserializeDtoWithNestedAliasedSimpleProperty() throws IOException {
        // Given
        AuthorizationDto.Projection projection = ProjectionDsl.parse("role { name as code }", AuthorizationDto.Projection.class);

        String sourceJson = """
                {"role":{"code":"AdminRole"}}""";

        // When
        AuthorizationDto result = deserializationProvider.read(sourceJson, AuthorizationDto.class, projection);

        // Then
        assertEquals("AdminRole", result.role.get().name.get());
    }

    @Test
    void deserializeDtoWithNestedAliasedDtoProperty() throws IOException {
        // Given
        UserDto.Projection projection = ProjectionDsl.parse("authorizations { role rl { name } }", UserDto.Projection.class);
        String sourceJson = """
                {"authorizations":[{"operationType":"NONE","itemOperationType":"NONE","value":{"rl":{"name":"AdminRole"}}}]}""";

        // When
        UserDto result = deserializationProvider.read(sourceJson, UserDto.class, projection);

        // Then
        assertEquals("AdminRole", result.authorizations.get().stream().findFirst().get().getValue().role.get().name.get());
    }

    @Test
    void deserializeDtoWithNestedAliasedListProperty() throws IOException {
        // Given
        AuthorizationDto.Projection projection = ProjectionDsl.parse("role { actions as perms { id } }", AuthorizationDto.Projection.class);
        String sourceJson = """
                {"role":{"perms":[{"operationType":"NONE","itemOperationType":"NONE","value":{"id":"Users$READ"}}]}}""";

        // When
        AuthorizationDto result = deserializationProvider.read(sourceJson, AuthorizationDto.class, projection);

        // Then
        assertEquals("Users$READ", result.role.get().actions.get().iterator().next().getValue().id.get());
    }

    // TODO Add tests for objects that are not dtos to verify this does not break normal serialization


}
