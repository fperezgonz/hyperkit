package solutions.sulfura.hyperkit.utils.spring.hypermapper;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.utils.spring.HyperRepositoryImpl;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.ManyToOneEntity;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.ManyToOneEntityDto;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.OneToManyEntity;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.OneToManyEntityDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class HyperMapperTest {

    @Autowired
    private HyperMapper<Object> dtoMapper;

    @Autowired
    private HyperRepositoryImpl<Object> hyperRepository;

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