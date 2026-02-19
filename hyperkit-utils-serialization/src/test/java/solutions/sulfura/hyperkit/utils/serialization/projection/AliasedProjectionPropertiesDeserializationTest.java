package solutions.sulfura.hyperkit.utils.serialization.projection;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.utils.serialization.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.ValueWrapperAdapterImpl;
import solutions.sulfura.hyperkit.utils.serialization.alias.ProjectedDtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.projection.dtos.AuthorizationDto;
import solutions.sulfura.hyperkit.utils.serialization.projection.dtos.UserDto;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperJacksonModule;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for serialization and deserialization of projected dtos with field name aliasing.
 */
public class AliasedProjectionPropertiesDeserializationTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ValueWrapperAdapterImpl adapter = new ValueWrapperAdapterImpl();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new DtoJacksonModule());
        objectMapper.registerModule(new ValueWrapperJacksonModule(adapter));
        objectMapper.registerModule(new ProjectedDtoJacksonModule());
    }

    @Test
    void deserializeDtoWithAliasedSimpleProperty() throws IOException {
        // Given
        AuthorizationDto.Projection projection = ProjectionDsl.parse("name as code", AuthorizationDto.Projection.class);
        String sourceJson = """
                {"code":"AdminAuth"}""";


        // When
        AuthorizationDto result = objectMapper.reader().withAttribute("hyperkit-projection", projection).readValue(sourceJson, AuthorizationDto.class);

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
        AuthorizationDto result = objectMapper.reader().withAttribute("hyperkit-projection", projection).readValue(sourceJson, AuthorizationDto.class);

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
        AuthorizationDto result = objectMapper.reader().withAttribute("hyperkit-projection", projection).readValue(sourceJson, AuthorizationDto.class);

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
        AuthorizationDto result = objectMapper.reader().withAttribute("hyperkit-projection", projection).readValue(sourceJson, AuthorizationDto.class);

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
        UserDto result = objectMapper.reader().withAttribute("hyperkit-projection", projection).readValue(sourceJson, UserDto.class);

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
        AuthorizationDto result = objectMapper.reader().withAttribute("hyperkit-projection", projection).readValue(sourceJson, AuthorizationDto.class);

        // Then
        assertEquals("Users$READ", result.role.get().actions.get().iterator().next().getValue().id.get());
    }


}
