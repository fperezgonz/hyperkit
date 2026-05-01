package solutions.sulfura.hyperkit.utils.spring.hypermapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.utils.spring.HyperRepositoryImpl;
import solutions.sulfura.hyperkit.utils.spring.TransactionUtils;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.*;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static solutions.sulfura.hyperkit.dtos.ListOperation.ItemOperationType.NONE;
import static solutions.sulfura.hyperkit.dtos.ListOperation.ListOperationType.ADD;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class HyperMapperTest {

    @Autowired
    private HyperMapper<Object> dtoMapper;
    @Autowired
    private HyperRepositoryImpl<Object> hyperRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private TransactionUtils transactionUtils;

    @Test
    @DisplayName("Should map Entities with list properties containing types that cannot be mapped to Dtos")
    @Transactional
    void testMappingOfEntityWithNonDtosList() {
        // Given an entity that as a Dto class
        EntityWithPrimitiveList entity = new EntityWithPrimitiveList();
        entity.name = "Test Entity";
        entity.stringList = new ArrayList<>(Arrays.asList("value1", "value2", "value3"));
        entity.integerList = new ArrayList<>(Arrays.asList(1, 2, 3));

        hyperRepository.save(entity, null);

        EntityWithPrimitiveListDto.Projection projection = ProjectionDsl.parse("id, name, stringList, integerList", EntityWithPrimitiveListDto.Projection.class);

        // When mapping the entity to DTO
        EntityWithPrimitiveListDto dto = dtoMapper.mapEntityToDto(entity, EntityWithPrimitiveListDto.class, projection);

        // Then it should map as they are the contents of lists that contain items that cannot be mapped to dtos
        assertNotNull(dto);
        assertTrue(dto.stringList.isPresent());
        assertEquals(3, dto.stringList.get().size());
        assertEquals("value1", dto.stringList.get().get(0).getValue());
        assertEquals("value2", dto.stringList.get().get(1).getValue());
        assertEquals("value3", dto.stringList.get().get(2).getValue());
        assertTrue(dto.integerList.isPresent());
        assertEquals(3, dto.integerList.get().size());
        assertEquals(1, dto.integerList.get().get(0).getValue());
        assertEquals(2, dto.integerList.get().get(1).getValue());
        assertEquals(3, dto.integerList.get().get(2).getValue());

        // When the DTO receives changes and is mapped back to an entity
        dto.name = ValueWrapper.of("Updated Name");

        // Modify the string list: remove value2, add value4
        List<ListOperation<String>> modifiedStringList = new ArrayList<>();
        modifiedStringList.add(ListOperation.valueOf("value1", ListOperation.ListOperationType.NONE, ListOperation.ItemOperationType.UPDATE));
        modifiedStringList.add(ListOperation.valueOf("value2", ListOperation.ListOperationType.REMOVE, ListOperation.ItemOperationType.NONE));
        modifiedStringList.add(ListOperation.valueOf("value3", ListOperation.ListOperationType.NONE, ListOperation.ItemOperationType.UPDATE));
        modifiedStringList.add(ListOperation.valueOf("value4", ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.INSERT));
        dto.stringList = ValueWrapper.of(modifiedStringList);

        // Modify the integer list: remove 2, add 4
        List<ListOperation<Integer>> modifiedIntegerList = new ArrayList<>();
        modifiedIntegerList.add(ListOperation.valueOf(1, ListOperation.ListOperationType.NONE, ListOperation.ItemOperationType.UPDATE));
        modifiedIntegerList.add(ListOperation.valueOf(2, ListOperation.ListOperationType.REMOVE, ListOperation.ItemOperationType.NONE));
        modifiedIntegerList.add(ListOperation.valueOf(3, ListOperation.ListOperationType.NONE, ListOperation.ItemOperationType.UPDATE));
        modifiedIntegerList.add(ListOperation.valueOf(4, ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.INSERT));
        dto.integerList = ValueWrapper.of(modifiedIntegerList);

        // Persist DTO to entity
        Object userContextInfo = new Object();
        EntityWithPrimitiveList modifiedEntity = dtoMapper.persistDtoToEntity(dto, userContextInfo);

        // Assert updated entity values
        assertNotNull(modifiedEntity);
        assertEquals(entity.id, modifiedEntity.id);
        assertEquals("Updated Name", modifiedEntity.name);
        assertEquals(3, modifiedEntity.stringList.size());
        assertTrue(modifiedEntity.stringList.contains("value1"));
        assertFalse(modifiedEntity.stringList.contains("value2"));
        assertTrue(modifiedEntity.stringList.contains("value3"));
        assertTrue(modifiedEntity.stringList.contains("value4"));
        assertEquals(3, modifiedEntity.integerList.size());
        assertTrue(modifiedEntity.integerList.contains(1));
        assertFalse(modifiedEntity.integerList.contains(2));
        assertTrue(modifiedEntity.integerList.contains(3));
        assertTrue(modifiedEntity.integerList.contains(4));
    }

    @Test
    @DisplayName("Should map DTOs with list properties containing non-dto types")
    @Transactional
    void testMappingOfDtosWithNonDtosList() {

        // Given
        EntityWithPrimitiveListDto dto = new EntityWithPrimitiveListDto.Builder()
                .name(ValueWrapper.of("Test Entity"))
                .stringList(ValueWrapper.of(Arrays.asList(
                        ListOperation.valueOf("value1", ADD, NONE),
                        ListOperation.valueOf("value2", ADD, NONE),
                        ListOperation.valueOf("value3", ADD, NONE)
                )))
                .integerList(ValueWrapper.of(Arrays.asList(
                                ListOperation.valueOf(1, ADD, NONE),
                                ListOperation.valueOf(2, ADD, NONE),
                                ListOperation.valueOf(3, ADD, NONE)
                        )
                ))
                .build();

        // When mapping the Dto to entity
        EntityWithPrimitiveList entity = dtoMapper.mapDtoToEntity(dto, null).mappedValue();

        // Assert entity values
        assertNotNull(entity);
        assertNotNull(entity.stringList);
        assertEquals(3, entity.stringList.size());
        assertEquals("value1", entity.stringList.get(0));
        assertEquals("value2", entity.stringList.get(1));
        assertEquals("value3", entity.stringList.get(2));
        assertNotNull(entity.integerList);
        assertEquals(3, entity.integerList.size());
        assertEquals(1, entity.integerList.get(0));
        assertEquals(2, entity.integerList.get(1));
        assertEquals(3, entity.integerList.get(2));

    }

    /**
     * Test to verify a OneToManyEntity is saved correctly when a DTO is provided.
     */
    @Test
    @Transactional
    void testSaveOneToManyEntityFromDto() {

        Object userContextInfo = new Object();
        OneToManyEntityDto oneToManyDto = new OneToManyEntityDto();
        oneToManyDto.name = ValueWrapper.of("Existing OneToManyEntity");

        // Act
        OneToManyEntity result = dtoMapper.persistDtoToEntity(oneToManyDto, userContextInfo);

        // Assert
        assertNotNull(result);
        assertNotNull(result.id);
        assertEquals(oneToManyDto.name.get(), result.name);
        // fail();

    }

    /**
     * Test to verify the conversion from an entity to a DTO.
     */
    @Test
    @Transactional
    void testMapEntityToDto() {

        // Arrange
        OneToManyEntity entity = new OneToManyEntity();
        entity.id = 1L;
        entity.name = "Test Entity";
        entity.description = "Test Entity Description";

        ManyToOneEntity nestedEntity = new ManyToOneEntity();
        nestedEntity.id = 1L;
        nestedEntity.name = "Nested Entity";
        nestedEntity.description = "Nested Entity Description";

        ManyToOneEntity nestedEntity2 = new ManyToOneEntity();
        nestedEntity2.id = 2L;
        nestedEntity2.name = "Nested Entity2";
        nestedEntity2.description = "Nested Entity2 Description";

        entity.manyToOneEntities = new java.util.HashSet<>();
        entity.manyToOneEntities.add(nestedEntity);
        entity.manyToOneEntities.add(nestedEntity2);

        // Act
        OneToManyEntityDto dto = dtoMapper.mapEntityToDto(entity, OneToManyEntityDto.class, OneToManyEntityDto.Projection.Builder.newInstance()
                .id(FieldConf.Presence.MANDATORY)
                .name(FieldConf.Presence.MANDATORY)
                .description(FieldConf.Presence.MANDATORY)
                .manyToOneEntities(FieldConf.Presence.MANDATORY, ManyToOneEntityDto.Projection.Builder.newInstance()
                        .id(FieldConf.Presence.MANDATORY)
                        .name(FieldConf.Presence.MANDATORY)
                        .description(FieldConf.Presence.MANDATORY)
                        .build())
                .build()
        );

        // Assert
        assertNotNull(dto);
        assertEquals(entity.id, dto.id.get());
        assertEquals(entity.name, dto.name.get());
        assertNotNull(dto.manyToOneEntities.get());
        assertEquals(2, dto.manyToOneEntities.get().size());

        var nestedDto1 = dto.manyToOneEntities.get().stream()
                .filter(e -> e.getValue().id.get().equals(1L))
                .findFirst()
                .orElseThrow()
                .getValue();

        assertEquals(nestedEntity.id, nestedDto1.id.get());
        assertEquals(nestedEntity.name, nestedDto1.name.get());
        assertEquals(nestedEntity.description, nestedDto1.description.get());

        var nestedDto2 = dto.manyToOneEntities.get().stream()
                .filter(e -> e.getValue().id.get().equals(2L))
                .findFirst()
                .orElseThrow()
                .getValue();

        assertEquals(nestedEntity2.id, nestedDto2.id.get());
        assertEquals(nestedEntity2.name, nestedDto2.name.get());
        assertEquals(nestedEntity2.description, nestedDto2.description.get());

    }


    /**
     * Test to verify mapping ManyToMany entities from Entity to DTO.
     */
    @Test
    @DisplayName("Should map ManyToMany relationship from entity to DTO")
    @Transactional
    void testMapManyToManyEntityToDto() {
        // Given: a left entity with two right entities in a many-to-many relationship
        ManyToManyLeftEntity left = new ManyToManyLeftEntity();
        left.name = "Left 1";

        ManyToManyRightEntity right1 = new ManyToManyRightEntity();
        right1.label = "Right1";
        ManyToManyRightEntity right2 = new ManyToManyRightEntity();
        right2.label = "Right2";

        left.rights = new java.util.HashSet<>();
        left.rights.add(right1);
        left.rights.add(right2);

        right1.lefts = new java.util.HashSet<>();
        right1.lefts.add(left);
        right2.lefts = new java.util.HashSet<>();
        right2.lefts.add(left);

        // Persist initial graph
        hyperRepository.save(right1, null);
        hyperRepository.save(right2, null);
        hyperRepository.save(left, null);

        // When: mapping the entity to DTO with projection including the many-to-many collection
        ManyToManyLeftEntityDto dto = dtoMapper.mapEntityToDto(
                left,
                ManyToManyLeftEntityDto.class,
                ManyToManyLeftEntityDto.Projection.Builder.newInstance()
                        .id(FieldConf.Presence.MANDATORY)
                        .name(FieldConf.Presence.MANDATORY)
                        .rights(FieldConf.Presence.MANDATORY, ManyToManyRightEntityDto.Projection.Builder.newInstance()
                                .id(FieldConf.Presence.MANDATORY)
                                .label(FieldConf.Presence.MANDATORY)
                                .build())
                        .build());

        // Then: the DTO should contain the two right elements wrapped in ListOperation
        assertNotNull(dto);
        assertTrue(dto.id.isPresent());
        assertEquals(left.id, dto.id.get());
        assertTrue(dto.rights.isPresent());
        assertEquals(2, dto.rights.get().size());
        var labels = dto.rights.get().stream().map(lo -> lo.getValue().label.get()).sorted().toList();
        assertEquals(java.util.List.of("Right1", "Right2"), labels);
    }

    @Test
    @DisplayName("Should persist add to collections items added via ListOperation when using persistDtoToEntity and the item operation type is NONE")
    @Transactional
    void testAddToCollectionItemOperationsOfTypeNone() {
        // Given: A left entity with two right entities
        ManyToManyLeftEntity left = new ManyToManyLeftEntity();
        left.name = "L";

        ManyToManyRightEntity right1 = new ManyToManyRightEntity();
        right1.label = "Right1";

        hyperRepository.save(left, null);
        hyperRepository.save(right1, null);

        // The corresponding DTOs
        ManyToManyLeftEntityDto leftDto = new ManyToManyLeftEntityDto();
        leftDto.id = ValueWrapper.of(left.id);

        ManyToManyRightEntityDto right1Dto = new ManyToManyRightEntityDto();
        right1Dto.id = ValueWrapper.of(right1.id);

        // A List of operations that remove right1, update right2 and add right3
        HashSet<ListOperation<ManyToManyRightEntityDto>> ops = new HashSet<>();
        ops.add(ListOperation.valueOf(right1Dto, ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.NONE));
        leftDto.rights = ValueWrapper.of(ops);

        // When: persisting the DTO to entity
        ManyToManyLeftEntity persisted = dtoMapper.persistDtoToEntity(leftDto, null);

        // Then: right1 has been added
        assertNotNull(persisted);
        assertEquals(left.id, persisted.id);
        assertEquals(1, persisted.rights.size());

        ManyToManyRightEntity persistedRight1 = persisted.rights.stream()
                .findFirst()
                .orElse(null);
        assertNotNull(persistedRight1);
        assertEquals("Right1", persistedRight1.label, "Right1 should have been added to left");

    }

    @Test
    @DisplayName("Should persist ManyToMany modifications via ListOperation when using persistDtoToEntity")
    @Transactional
    void testPersistManyToManyModificationsWithListOperation() {
        // Given: A left entity with two right entities
        ManyToManyLeftEntity left = new ManyToManyLeftEntity();
        left.name = "L";

        ManyToManyRightEntity right1 = new ManyToManyRightEntity();
        right1.label = "Right1";
        ManyToManyRightEntity right2 = new ManyToManyRightEntity();
        right2.label = "Right2";

        left.rights = new HashSet<>();
        left.rights.add(right1);
        left.rights.add(right2);

        right1.lefts = new HashSet<>();
        right1.lefts.add(left);
        right2.lefts = new HashSet<>();
        right2.lefts.add(left);

        hyperRepository.save(right1, null);
        hyperRepository.save(right2, null);
        hyperRepository.save(left, null);

        // The corresponding DTOs
        ManyToManyLeftEntityDto leftDto = new ManyToManyLeftEntityDto();
        leftDto.id = ValueWrapper.of(left.id);

        ManyToManyRightEntityDto right1Dto = new ManyToManyRightEntityDto();
        right1Dto.id = ValueWrapper.of(right1.id);

        ManyToManyRightEntityDto right2Dto = new ManyToManyRightEntityDto();
        right2Dto.id = ValueWrapper.of(right2.id);
        right2Dto.label = ValueWrapper.of("Right2-updated");

        ManyToManyRightEntityDto right3Dto = new ManyToManyRightEntityDto();
        right3Dto.label = ValueWrapper.of("Right3");

        // A List of operations that remove right1, update right2 and add right3
        java.util.Set<ListOperation<ManyToManyRightEntityDto>> ops = new HashSet<>();
        ops.add(ListOperation.valueOf(right1Dto, ListOperation.ListOperationType.REMOVE, ListOperation.ItemOperationType.NONE));
        ops.add(ListOperation.valueOf(right2Dto, ListOperation.ListOperationType.NONE, ListOperation.ItemOperationType.UPDATE));
        ops.add(ListOperation.valueOf(right3Dto, ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.INSERT));
        leftDto.rights = ValueWrapper.of(ops);

        // When: persisting the DTO to entity
        ManyToManyLeftEntity persisted = dtoMapper.persistDtoToEntity(leftDto, null);

        // Then: right1 has been removed, right2 has been updated, and right3 was added
        assertNotNull(persisted);
        assertEquals(left.id, persisted.id);
        assertEquals(2, persisted.rights.size());

        boolean hasRight1 = persisted.rights.stream()
                .anyMatch(x -> x.id != null && x.id.equals(right1.id));
        assertFalse(hasRight1, "Right1 should be removed from left.rights");
        boolean right1HasParent = right1.lefts.contains(persisted);
        assertFalse(right1HasParent, "Parent should be removed from Right1");

        ManyToManyRightEntity persistedRight2 = persisted.rights.stream()
                .filter(x -> x.id != null && x.id.equals(right2.id))
                .findFirst().orElse(null);
        assertNotNull(persistedRight2, "Right2 should remain related to left");
        assertEquals("Right2-updated", persistedRight2.label, "Right2 label should be updated");

        ManyToManyRightEntity persistedRight3 = persisted.rights.stream()
                .filter(x -> x.label != null && x.label.equals(right3Dto.label.get()))
                .findFirst()
                .orElse(null);
        assertNotNull(persistedRight3, "Right3 should be related to left now");
        assertEquals("Right3", persistedRight3.label, "Right3 entity should be added");

    }

    @Test
    @DisplayName("Should map and persist entities with persistence annotations on private fields")
    @Transactional
    void testMappingOfEntityAnnotationsOnPrivateFields() {

        // Given: an entity with annotations on private fields and a corresponding DTO
        PrivateFieldsEntityDto dto = new PrivateFieldsEntityDto();
        dto.name = ValueWrapper.of("New Entity");

        // When: mapping and persisting
        PrivateFieldsEntity entity = dtoMapper.persistDtoToEntity(dto, null);
        hyperRepository.save(entity, null);

        // Then: The entity is persisted with the right name
        assertNotNull(entity.getId());
        assertEquals("New Entity", entity.getName());

        // When: map entity to DTO and verify fields
        var projection = PrivateFieldsEntityDto.Projection.Builder.newInstance()
                .id(FieldConf.of(FieldConf.Presence.MANDATORY))
                .name(FieldConf.of(FieldConf.Presence.MANDATORY))
                .build();

        PrivateFieldsEntityDto mappedDto = dtoMapper.mapEntityToDto(entity, PrivateFieldsEntityDto.class, projection);

        // Then: The DTO has been mapped correctly
        assertEquals(entity.getId(), mappedDto.id.get());
        assertEquals("New Entity", mappedDto.name.get());

        // When: Updating the entity name through a DTO
        PrivateFieldsEntityDto updateDto = new PrivateFieldsEntityDto();
        updateDto.id = ValueWrapper.of(entity.getId());
        updateDto.name = ValueWrapper.of("Updated Entity");
        PrivateFieldsEntity updated = dtoMapper.persistDtoToEntity(updateDto, null);

        // Then: The entity name has been updated
        assertEquals(entity.getId(), updated.getId());
        assertEquals("Updated Entity", updated.getName());
    }
}