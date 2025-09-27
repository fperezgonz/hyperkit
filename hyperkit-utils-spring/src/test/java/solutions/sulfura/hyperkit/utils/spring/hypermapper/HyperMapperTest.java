package solutions.sulfura.hyperkit.utils.spring.hypermapper;

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
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static solutions.sulfura.hyperkit.dtos.ListOperation.ListOperationType.ADD;
import static solutions.sulfura.hyperkit.dtos.ListOperation.ItemOperationType.NONE;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class HyperMapperTest {

    @Autowired
    private HyperMapper<Object> dtoMapper;

    @Autowired
    private HyperRepositoryImpl<Object> hyperRepository;

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
        EntityWithPrimitiveList entity = dtoMapper.mapDtoToEntity(dto, null).getEntity();

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
     * Test to verify that a nested ManyToOneEntity is saved correctly when included in a OneToManyEntityDto.
     */
    @Test
    @Transactional
    void testSaveOneToManyFromDto() {
        // Arrange
        OneToManyEntity oneToMany = new OneToManyEntity();
        oneToMany.name = "OneToManyEntity";
        oneToMany.manyToOneEntities = new java.util.HashSet<>();

        ManyToOneEntity manyToOne = new ManyToOneEntity();
        manyToOne.name = "ManyToOneEntity";
        manyToOne.description = "Description";
        manyToOne.oneToManyEntity = oneToMany;

        oneToMany.manyToOneEntities.add(manyToOne);

        hyperRepository.save(oneToMany, null);
        hyperRepository.save(manyToOne, null);

        Object userContextInfo = new Object();
        OneToManyEntityDto oneToManyDto = new OneToManyEntityDto();
        oneToManyDto.id = ValueWrapper.of(oneToMany.id);
        oneToManyDto.description = ValueWrapper.of(null);

        ManyToOneEntityDto manyToOneDto = new ManyToOneEntityDto();
        manyToOneDto.description = ValueWrapper.of("Updated ManyToOne Description");
        manyToOneDto.id = ValueWrapper.of(manyToOne.id);
        manyToOneDto.oneToManyEntity = ValueWrapper.of(oneToManyDto);

        oneToManyDto.manyToOneEntities = ValueWrapper.of(new java.util.HashSet<>());
        oneToManyDto.manyToOneEntities.get().add(ListOperation.valueOf(manyToOneDto, ListOperation.ListOperationType.NONE, ListOperation.ItemOperationType.UPDATE));

        // Act
        OneToManyEntity result = dtoMapper.persistDtoToEntity(oneToManyDto, userContextInfo);

        // Assert
        assertNotNull(result);
        //Assert that the field "name" keeps the original value
        assertEquals(oneToMany.name, result.name);
        //Assert that the field "description" has been overwritten with the values name the DTO
        assertNull(result.description);
        var manyToOneMapped = result.manyToOneEntities.iterator().next();
        //Assert that the field "name" keeps the original value
        assertEquals("ManyToOneEntity", manyToOneMapped.name);
        //Assert that the field "description" has been overwritten with the values name the DTO
        assertEquals(manyToOneDto.description.get(), manyToOneMapped.description);
        assertEquals(1, result.manyToOneEntities.size());
        //Assert the retrieved entities are the same entities that were persisted
        assertEquals(manyToOne.id, manyToOneMapped.id);
        assertEquals(oneToMany.id, manyToOneMapped.oneToManyEntity.id);
    }


    /**
     * Test to verify that a nested ManyToOneEntity is saved correctly when included in a OneToManyEntityDto.
     */
    @Test
    @Transactional
    void testSaveManyToOneFromDto() {
        // Arrange
        OneToManyEntity oneToMany = new OneToManyEntity();
        oneToMany.name = "OneToManyEntity";
        oneToMany.manyToOneEntities = new java.util.HashSet<>();

        ManyToOneEntity manyToOne = new ManyToOneEntity();
        manyToOne.name = "ManyToOneEntity";
        manyToOne.description = "Description";
        manyToOne.oneToManyEntity = oneToMany;

        oneToMany.manyToOneEntities.add(manyToOne);

        hyperRepository.save(oneToMany, null);
        hyperRepository.save(manyToOne, null);

        Object userContextInfo = new Object();
        OneToManyEntityDto oneToManyDto = new OneToManyEntityDto();
        oneToManyDto.id = ValueWrapper.of(oneToMany.id);
        oneToManyDto.description = ValueWrapper.of(null);

        ManyToOneEntityDto manyToOneDto = new ManyToOneEntityDto();
        manyToOneDto.description = ValueWrapper.of("Updated ManyToOne Description");
        manyToOneDto.id = ValueWrapper.of(manyToOne.id);
        manyToOneDto.oneToManyEntity = ValueWrapper.of(oneToManyDto);

        oneToManyDto.manyToOneEntities = ValueWrapper.of(new java.util.HashSet<>());
        oneToManyDto.manyToOneEntities.get().add(ListOperation.valueOf(manyToOneDto, ListOperation.ListOperationType.NONE, ListOperation.ItemOperationType.UPDATE));

        // Act
        ManyToOneEntity result = dtoMapper.persistDtoToEntity(manyToOneDto, userContextInfo);

        // Assert
        assertNotNull(result);
        //Assert that the field "name" keeps the original value
        assertEquals("ManyToOneEntity", result.name);
        //Assert that the field "description" has been overwritten with the values name the DTO
        assertEquals(manyToOneDto.description.get(), result.description);
        //Assert that the field "name" keeps the original value
        assertEquals("OneToManyEntity", result.oneToManyEntity.name);
        //Assert that the field "description" has been overwritten with the values name the DTO
        assertNull(result.oneToManyEntity.description);
        assertEquals(1, result.oneToManyEntity.manyToOneEntities.size());
        //Assert the retrieved entities are the same entities that were persisted
        assertEquals(manyToOne.id, result.oneToManyEntity.manyToOneEntities.iterator().next().id);
        assertEquals(oneToMany.id, result.oneToManyEntity.id);
    }


    /**
     * Test to verify that a ManyToOneEntity is removed correctly when included in a OneToManyEntityDto.
     */
    @Test
    @Transactional
    void testRemovalOperationFromDto() {
        // Arrange
        OneToManyEntity oneToMany = new OneToManyEntity();
        oneToMany.name = "OneToManyEntity";

        ManyToOneEntity manyToOne1 = new ManyToOneEntity();
        manyToOne1.name = "ManyToOneEntity1";
        manyToOne1.description = "Description 1";
        manyToOne1.oneToManyEntity = oneToMany;

        ManyToOneEntity manyToOne2 = new ManyToOneEntity();
        manyToOne2.name = "ManyToOneEntity2";
        manyToOne2.description = "Description 2";
        manyToOne2.oneToManyEntity = oneToMany;

        oneToMany.manyToOneEntities = new java.util.HashSet<>();
        oneToMany.manyToOneEntities.add(manyToOne1);
        oneToMany.manyToOneEntities.add(manyToOne2);

        hyperRepository.save(oneToMany, null);
        hyperRepository.save(manyToOne1, null);
        hyperRepository.save(manyToOne2, null);

        OneToManyEntityDto oneToManyDto = new OneToManyEntityDto();
        oneToManyDto.id = ValueWrapper.of(oneToMany.id);

        ManyToOneEntityDto manyToOneDto1 = new ManyToOneEntityDto();
        manyToOneDto1.id = ValueWrapper.of(manyToOne1.id);

        oneToManyDto.manyToOneEntities = ValueWrapper.of(new java.util.HashSet<>());
        oneToManyDto.manyToOneEntities.get().add(ListOperation.valueOf(manyToOneDto1, ListOperation.ListOperationType.REMOVE, ListOperation.ItemOperationType.NONE));

        // Act
        OneToManyEntity result = dtoMapper.persistDtoToEntity(oneToManyDto, null);

        // Assert
        assertNotNull(result);
        //Assert that the field "name" keeps the original value
        assertEquals("OneToManyEntity", result.name);
        //Assert the number of many-to-one entities is updated
        assertEquals(1, result.manyToOneEntities.size());
        //Assert the remaining entity is the one that was not removed
        var remainingEntity = result.manyToOneEntities.iterator().next();
        assertEquals(manyToOne2.id, remainingEntity.id);
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
    @DisplayName("Should persist Set modifications via ListOperation when using persistDtoToEntity")
    @Transactional
    void testPersistSetModificationsWithListOperation() {
        // Given: parent with two children
        OneToManyEntity parent = new OneToManyEntity();
        parent.name = "Parent";
        parent.manyToOneEntities = new java.util.HashSet<>();

        ManyToOneEntity child1 = new ManyToOneEntity();
        child1.name = "Child1";
        child1.description = "Child1";
        child1.oneToManyEntity = parent;

        ManyToOneEntity child2 = new ManyToOneEntity();
        child2.name = "Child2";
        child2.description = "Child2";
        child2.oneToManyEntity = parent;

        parent.manyToOneEntities.add(child1);
        parent.manyToOneEntities.add(child2);

        hyperRepository.save(parent, null);
        hyperRepository.save(child1, null);
        hyperRepository.save(child2, null);

        // And a DTO that removes child1, updates child2, and adds child3
        OneToManyEntityDto parentDto = new OneToManyEntityDto();
        parentDto.id = ValueWrapper.of(parent.id);

        ManyToOneEntityDto child1Dto = new ManyToOneEntityDto();
        child1Dto.id = ValueWrapper.of(child1.id);

        ManyToOneEntityDto child2Dto = new ManyToOneEntityDto();
        child2Dto.id = ValueWrapper.of(child2.id);
        child2Dto.description = ValueWrapper.of("Child2-updated");
        child2Dto.oneToManyEntity = ValueWrapper.of(parentDto);

        ManyToOneEntityDto child3Dto = new ManyToOneEntityDto();
        child3Dto.name = ValueWrapper.of("Child3");
        child3Dto.description = ValueWrapper.of("Child3");
        child3Dto.oneToManyEntity = ValueWrapper.of(parentDto);

        // Remove child1, update child2, add child3
        java.util.Set<ListOperation<ManyToOneEntityDto>> ops = new java.util.HashSet<>();
        ops.add(ListOperation.valueOf(child1Dto, ListOperation.ListOperationType.REMOVE, ListOperation.ItemOperationType.NONE));
        ops.add(ListOperation.valueOf(child2Dto, ListOperation.ListOperationType.NONE, ListOperation.ItemOperationType.UPDATE));
        ops.add(ListOperation.valueOf(child3Dto, ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.INSERT));
        parentDto.manyToOneEntities = ValueWrapper.of(ops);

        // When: persisting DTO to entity
        OneToManyEntity persisted = dtoMapper.persistDtoToEntity(parentDto, null);

        // Then: the relationship set reflects REMOVE/UPDATE/ADD
        assertNotNull(persisted);
        assertEquals(parent.id, persisted.id);
        assertEquals(2, persisted.manyToOneEntities.size(), "Parent should have exactly two children after modifications");

        // Child1 removed
        boolean containsChild1 = persisted.manyToOneEntities.stream().anyMatch(e -> child1.id.equals(e.id));
        assertFalse(containsChild1, "Child1 should have been removed from the parent's set");
        boolean right1HasParent = child1.oneToManyEntity != null;
        assertFalse(right1HasParent, "Parent should be removed from Child1");

        // Child2 updated
        ManyToOneEntity persistedChild2 = persisted.manyToOneEntities.stream().filter(e -> child2.id.equals(e.id)).findFirst().orElse(null);
        assertNotNull(persistedChild2, "Child2 should still be present");
        assertEquals("Child2-updated", persistedChild2.description, "Child2 description should be updated");
        assertNotNull(persistedChild2.oneToManyEntity);
        assertEquals(parent.id, persistedChild2.oneToManyEntity.id);

        // Implicitly, the second element is the newly added child; size assertion above verifies ADD was applied.
    }

    @Test
    @DisplayName("Should persist ManyToMany modifications via ListOperation when using persistDtoToEntity")
    @Transactional
    void testPersistManyToManyModificationsWithListOperation() {
        // Given: left with two rights
        ManyToManyLeftEntity left = new ManyToManyLeftEntity();
        left.name = "L";

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

        // persist initial graph
        hyperRepository.save(right1, null);
        hyperRepository.save(right2, null);
        hyperRepository.save(left, null);

        // Build DTO with REMOVE right1, UPDATE right2 (label), ADD right3
        ManyToManyLeftEntityDto leftDto = new ManyToManyLeftEntityDto();
        leftDto.id = ValueWrapper.of(left.id);

        ManyToManyRightEntityDto right1Dto = new ManyToManyRightEntityDto();
        right1Dto.id = ValueWrapper.of(right1.id);

        ManyToManyRightEntityDto right2Dto = new ManyToManyRightEntityDto();
        right2Dto.id = ValueWrapper.of(right2.id);
        right2Dto.label = ValueWrapper.of("Right2-updated");

        ManyToManyRightEntityDto right3Dto = new ManyToManyRightEntityDto();
        right3Dto.label = ValueWrapper.of("Right3");

        // Remove right1, update right2, add right3
        java.util.Set<ListOperation<ManyToManyRightEntityDto>> ops = new java.util.HashSet<>();
        ops.add(ListOperation.valueOf(right1Dto, ListOperation.ListOperationType.REMOVE, ListOperation.ItemOperationType.NONE));
        ops.add(ListOperation.valueOf(right2Dto, ListOperation.ListOperationType.NONE, ListOperation.ItemOperationType.UPDATE));
        ops.add(ListOperation.valueOf(right3Dto, ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.INSERT));
        leftDto.rights = ValueWrapper.of(ops);

        // When: persist DTO to entity
        ManyToManyLeftEntity persisted = dtoMapper.persistDtoToEntity(leftDto, null);

        // Then: left has two rights: updated right2 and new right3; right1 has been removed
        assertNotNull(persisted);
        assertEquals(left.id, persisted.id);
        assertEquals(2, persisted.rights.size());

        boolean hasRight1 = persisted.rights.stream().anyMatch(x -> x.id != null && x.id.equals(right1.id));
        assertFalse(hasRight1, "Right1 should be removed from left.rights");
        boolean right1HasParent = right1.lefts.contains(persisted);
        assertFalse(right1HasParent, "Parent should be removed from Right1");

        ManyToManyRightEntity persistedRight2 = persisted.rights.stream().filter(x -> x.id != null && x.id.equals(right2.id)).findFirst().orElse(null);
        assertNotNull(persistedRight2, "Right2 should remain related to left");
        assertEquals("Right2-updated", persistedRight2.label, "Right2 label should be updated");

    }
}