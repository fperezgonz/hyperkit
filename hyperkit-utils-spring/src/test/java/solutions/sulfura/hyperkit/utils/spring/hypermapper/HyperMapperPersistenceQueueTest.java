package solutions.sulfura.hyperkit.utils.spring.hypermapper;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.spring.HyperRepositoryImpl;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.*;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to verify that items modified through REMOVE, ADD and UPDATE operations
 * are added to the persistence queue in HyperMapper.
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
class HyperMapperPersistenceQueueTest {

    @Autowired
    private HyperMapper<Object> dtoMapper;

    @Autowired
    private HyperRepositoryImpl<Object> hyperRepository;

    /**
     * Test to verify that items added through ADD operation are added to the persistence queue.
     */
    @Test
    @DisplayName("Items added through ADD operation should be added to the persistence queue")
    @Transactional
    void testAddOperationAddsToPersistenceQueue() {
        // Given
        OneToManyEntity oneToMany = new OneToManyEntity();
        oneToMany.name = "Root";
        oneToMany.manyToOneEntities = new HashSet<>();
        hyperRepository.save(oneToMany, null);

        // Create a DTO with a new ManyToOneEntity to be added
        OneToManyEntityDto oneToManyDto = new OneToManyEntityDto();
        oneToManyDto.id = ValueWrapper.of(oneToMany.id);
        
        ManyToOneEntityDto newManyToOneDto = new ManyToOneEntityDto();
        newManyToOneDto.name = ValueWrapper.of("Added entity");
        
        oneToManyDto.manyToOneEntities = ValueWrapper.of(new HashSet<>());
        oneToManyDto.manyToOneEntities.get().add(
            ListOperation.valueOf(newManyToOneDto, ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.INSERT)
        );

        // When the dto is mapped to an entity
        HyperMapper.ToEntityResult<OneToManyEntity> result = dtoMapper.mapDtoToEntity(oneToManyDto, null);

        // Then the items added with ADD should be on the persistence queue
        List<Object> persistenceQueue = result.getPersistenceQueue();
        assertFalse(persistenceQueue.isEmpty(), "Persistence queue should not be empty");

        boolean foundNewEntity = false;
        for (Object obj : persistenceQueue) {
            if (obj instanceof ManyToOneEntity manyToOne && 
                "Added entity".equals(manyToOne.name)) {
                foundNewEntity = true;
                break;
            }
        }

        assertTrue(foundNewEntity, "The new ManyToOneEntity should be in the persistence queue");

    }

    /**
     * Test to verify that items modified using an UPDATE operation are added to the persistence queue
     */
    @Test
    @DisplayName("Items updated using an UPDATE operation should be added to the persistence queue")
    @Transactional
    void testUpdateOperationAddsToPersistenceQueue() {
        // Given
        OneToManyEntity oneToMany = new OneToManyEntity();
        oneToMany.name = "Root";
        oneToMany.manyToOneEntities = new HashSet<>();
        
        ManyToOneEntity existingManyToOne = new ManyToOneEntity();
        existingManyToOne.name = "Existing ManyToOneEntity";
        existingManyToOne.oneToManyEntity = oneToMany;
        
        oneToMany.manyToOneEntities.add(existingManyToOne);
        
        hyperRepository.save(oneToMany, null);
        hyperRepository.save(existingManyToOne, null);

        OneToManyEntityDto oneToManyDto = new OneToManyEntityDto();
        oneToManyDto.id = ValueWrapper.of(oneToMany.id);
        
        ManyToOneEntityDto updateManyToOneDto = new ManyToOneEntityDto();
        updateManyToOneDto.id = ValueWrapper.of(existingManyToOne.id);
        
        oneToManyDto.manyToOneEntities = ValueWrapper.of(new HashSet<>());
        oneToManyDto.manyToOneEntities.get().add(
            ListOperation.valueOf(updateManyToOneDto, ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.UPDATE)
        );

        // When mapping the dto to an entity
        HyperMapper.ToEntityResult<OneToManyEntity> result = dtoMapper.mapDtoToEntity(oneToManyDto, null);

        // Then the UPDATE item should be on the persistence queue
        List<Object> persistenceQueue = result.getPersistenceQueue();
        assertFalse(persistenceQueue.isEmpty(), "Persistence queue should not be empty");
        
        // The updated ManyToOneEntity should be in the persistence queue
        boolean foundUpdatedEntity = false;
        for (Object obj : persistenceQueue) {
            if (obj instanceof ManyToOneEntity manyToOne && 
                existingManyToOne.id.equals(manyToOne.id)) {
                foundUpdatedEntity = true;
                break;
            }
        }
        assertTrue(foundUpdatedEntity, "The updated ManyToOneEntity should be in the persistence queue");
    }

    /**
     * Test to verify that items removed using a REMOVE operation are added to the persistence queue.
     */
    @Test
    @DisplayName("Items removed using a REMOVE operation should be added to the persistence queue")
    @Transactional
    void testRemoveOperationAddsToPersistenceQueue() {
        // Given
        OneToManyEntity oneToMany = new OneToManyEntity();
        oneToMany.name = "Root";
        oneToMany.manyToOneEntities = new HashSet<>();
        
        ManyToOneEntity manyToOne = new ManyToOneEntity();
        manyToOne.name = "ManyToOneEntity";
        manyToOne.description = "Description";
        manyToOne.oneToManyEntity = oneToMany;
        
        oneToMany.manyToOneEntities.add(manyToOne);
        
        hyperRepository.save(oneToMany, null);
        hyperRepository.save(manyToOne, null);

        // Create a DTO to remove the ManyToOneEntity from
        OneToManyEntityDto oneToManyDto = new OneToManyEntityDto();
        oneToManyDto.id = ValueWrapper.of(oneToMany.id);
        
        ManyToOneEntityDto removeManyToOneDto = new ManyToOneEntityDto();
        removeManyToOneDto.id = ValueWrapper.of(manyToOne.id);
        
        oneToManyDto.manyToOneEntities = ValueWrapper.of(new HashSet<>());
        oneToManyDto.manyToOneEntities.get().add(
            ListOperation.valueOf(removeManyToOneDto, ListOperation.ListOperationType.REMOVE, ListOperation.ItemOperationType.NONE)
        );

        // When mapping the dto to an entity
        HyperMapper.ToEntityResult<OneToManyEntity> result = dtoMapper.mapDtoToEntity(oneToManyDto, null);

        // Then
        List<Object> persistenceQueue = result.getPersistenceQueue();
        assertFalse(persistenceQueue.isEmpty(), "Persistence queue should not be empty");
        
        // The removed ManyToOneEntity should be in the persistence queue
        boolean foundRemovedEntity = false;
        for (Object obj : persistenceQueue) {
            if (obj instanceof ManyToOneEntity manyToOneEntity && 
                manyToOne.id.equals(manyToOneEntity.id)) {
                foundRemovedEntity = true;
                break;
            }
        }
        assertTrue(foundRemovedEntity, "The removed ManyToOneEntity should be in the persistence queue");
    }
}