package solutions.sulfura.hyperkit.utils.serialization.projection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.serialization.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.ValueWrapperAdapterImpl;
import solutions.sulfura.hyperkit.utils.serialization.projection.dtos.*;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperJacksonModule;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for serialization and deserialization of projected dtos with field name aliasing.
 */
public class AliasedProjectionPropertiesSerializationTest {

    private ObjectMapper objectMapper;
    private ValueWrapperAdapterImpl adapter;

    @BeforeEach
    void setUp() {
        adapter = new ValueWrapperAdapterImpl();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ValueWrapperJacksonModule(adapter));
        objectMapper.registerModule(new DtoJacksonModule());
        fail("ProjectedDtoJacksonModule does not exist yet");
//        objectMapper.registerModule(new ProjectedDtoJacksonModule());
    }

    @Test
    void serializeDtoWithAliasedSimpleProperty() throws JsonProcessingException {
        // Given
        AuthorizationDto.Projection projection = ProjectionDsl.parse("name as code", AuthorizationDto.Projection.class);
        AuthorizationDto dto = new AuthorizationDto.Builder()
                .name(ValueWrapper.of("AdminAuth"))
                .build();

        // When
        String result = objectMapper.writer().withAttribute("hyperkit-projection", projection).writeValueAsString(dto);
        // Then
        assertEquals("""
                {"code":"AdminAuth"}""", result);
    }

    @Test
    void serializeDtoWithAliasedDtoProperty() throws JsonProcessingException {
        // Given
        AuthorizationDto.Projection projection = ProjectionDsl.parse("role as rl { name }", AuthorizationDto.Projection.class);
        AuthorizationDto dto = new AuthorizationDto.Builder()
                .role(ValueWrapper.of(RoleDto.Builder.newInstance()
                        .name(ValueWrapper.of("AdminRole"))
                        .build()))
                .build();
        // When
        String result = objectMapper.writer().withAttribute("hyperkit-projection", projection).writeValueAsString(dto);
        // Then
        assertEquals("""
                {"rl":{"name":"AdminRole"}}""", result);
    }

    @Test
    void serializeDtoWithAliasedListProperty() throws JsonProcessingException {
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
        String result = objectMapper.writer().withAttribute("hyperkit-projection", projection).writeValueAsString(dto);
        // Then
        // Then
        assertEquals("""
                {"resources":[{"operationType":"NONE","itemOperationType":"NONE","value":{"name":"MainResource"}}]}""", result);
    }

    @Test
    void serializeDtoWithNestedAliasedSimpleProperty() throws JsonProcessingException {
        // Given
        AuthorizationDto.Projection projection = ProjectionDsl.parse("role { name as code }", AuthorizationDto.Projection.class);
        AuthorizationDto dto = new AuthorizationDto.Builder()
                .role(ValueWrapper.of(RoleDto.Builder.newInstance()
                        .name(ValueWrapper.of("AdminRole"))
                        .build()))
                .build();
        // When
        String result = objectMapper.writer().withAttribute("hyperkit-projection", projection).writeValueAsString(dto);
        // Then
        assertEquals("""
                {"role":{"code":"AdminRole"}}""", result);
    }

    @Test
    void serializeDtoWithNestedAliasedDtoProperty() throws JsonProcessingException {
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
        String result = objectMapper.writer().withAttribute("hyperkit-projection", projection).writeValueAsString(dto);
        // Then
        assertEquals("""
                {"authorizations":[{"operationType":"NONE","itemOperationType":"NONE","value":{"rl":{"name":"AdminRole"}}}]}""", result);
    }

    @Test
    void serializeDtoWithNestedAliasedListProperty() throws JsonProcessingException {
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
        String result = objectMapper.writer().withAttribute("hyperkit-projection", projection).writeValueAsString(dto);
        // Then
        assertEquals("""
                {"role":{"perms":[{"operationType":"NONE","itemOperationType":"NONE","value":{"id":"Users$READ"}}]}}""", result);
    }


}
