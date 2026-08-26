package solutions.sulfura.hyperkit.utils.spring.hypermapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionTemplate;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.ManyToOneEntity;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.OneToManyEntity;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto.ManyToOneEntityDto;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto.OneToManyEntityDto;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class HyperMapperUninitializedCollectionsTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private HyperMapper<Object> hyperMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;


    @Test
    @DisplayName("Should not initialize collection when adding an item to an uninitialized collection property")
    void testAddToUninitializedCollectionProperty() {
        // Given: a parent entity with an uninitialized collection property, a potential child entity and a dto that represents the addition of the child entity to the parent's collection property
        Long id = transactionTemplate.execute(status -> {
            OneToManyEntity entity = new OneToManyEntity();
            entity.name = "Parent";
            entityManager.persist(entity);
            return entity.id;
        });

        Long firstChildId = transactionTemplate.execute(status -> {
            ManyToOneEntity firstChild = new ManyToOneEntity();
            firstChild.name = "Child";
            entityManager.persist(firstChild);
            return firstChild.id;
        });

        transactionTemplate.executeWithoutResult(status -> {

            ManyToOneEntityDto firstChildDto = ManyToOneEntityDto.Builder
                    .newInstance()
                    .id(ValueWrapper.of(firstChildId))
                    .build();
            OneToManyEntityDto parentDto = OneToManyEntityDto.Builder
                    .newInstance()
                    .id(ValueWrapper.of(id))
                    .manyToOneEntities(ValueWrapper.of(Set.of(ListOperation.valueOf(firstChildDto, ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.NONE))))
                    .build();

            // When adding the child to the parent by using persistDtoToEntity
            hyperMapper.persistDtoToEntity(parentDto, null);

            ManyToOneEntity firstChild = entityManager.find(ManyToOneEntity.class, firstChildId);
            OneToManyEntity parent = entityManager.find(OneToManyEntity.class, id);

            // Then the collection is not initialized, and after flushing and initializing the collection, it contains the added entity
            assertFalse(Hibernate.isInitialized(parent.manyToOneEntities), "Collection should not be initialized by persistDtoToEntity");
            assertTrue(parent.manyToOneEntities.contains(firstChild), "Collection should contain the new firstChild entity");

        });
    }


    @Test
    @DisplayName("Should not initialize collection when removing an item from  an uninitialized collection property")
    void testRemoveFromUninitializedCollectionProperty() {
        // Given: a parent entity with an uninitialized collection property, a potential child entity and a dto that represents the addition of the child entity to the parent's collection property
        Long id = transactionTemplate.execute(status -> {
            OneToManyEntity entity = new OneToManyEntity();
            entity.name = "Parent";
            entityManager.persist(entity);
            return entity.id;
        });

        Long firstChildId = transactionTemplate.execute(status -> {
            ManyToOneEntity firstChild = new ManyToOneEntity();
            firstChild.name = "Child";
            firstChild.oneToManyEntity = entityManager.find(OneToManyEntity.class, id);
            entityManager.persist(firstChild);
            return firstChild.id;
        });

        transactionTemplate.executeWithoutResult(status -> {

            ManyToOneEntityDto firstChildDto = ManyToOneEntityDto.Builder
                    .newInstance()
                    .id(ValueWrapper.of(firstChildId))
                    .build();
            OneToManyEntityDto parentDto = OneToManyEntityDto.Builder
                    .newInstance()
                    .id(ValueWrapper.of(id))
                    .manyToOneEntities(ValueWrapper.of(Set.of(ListOperation.valueOf(firstChildDto, ListOperation.ListOperationType.REMOVE, ListOperation.ItemOperationType.NONE))))
                    .build();

            // When removing the child from the parent by using persistDtoToEntity
            hyperMapper.persistDtoToEntity(parentDto, null);

            ManyToOneEntity firstChild = entityManager.find(ManyToOneEntity.class, firstChildId);
            OneToManyEntity parent = entityManager.find(OneToManyEntity.class, id);

            // Then the collection is not initialized, and after flushing and initializing the collection, it contains the added entity
            assertFalse(Hibernate.isInitialized(parent.manyToOneEntities), "Collection should not be initialized by persistDtoToEntity");
            assertFalse(parent.manyToOneEntities.contains(firstChild), "Collection should not contain the firstChild entity");

        });
    }
}
