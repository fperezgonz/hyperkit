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


}