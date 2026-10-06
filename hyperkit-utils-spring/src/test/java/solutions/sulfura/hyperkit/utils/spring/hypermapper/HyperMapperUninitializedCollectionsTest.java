package solutions.sulfura.hyperkit.utils.spring.hypermapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionTemplate;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfig;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import(SpringTestConfig.class)
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

    @Test
    @DisplayName("Should initialize collection when adding an item to an uninitialized ManyToMany collection property on owning side")
    void testAddManyToManyToUninitializedCollectionProperty() {
        // Given: a left entity with an uninitialized collection property, a potential right entity and a dto that represents the addition of the right entity to the left entity's collection property
        Long leftId = transactionTemplate.execute(status -> {
            ManyToManyLeftEntity left = new ManyToManyLeftEntity();
            left.name = "Left";
            entityManager.persist(left);
            return left.id;
        });

        Long rightId = transactionTemplate.execute(status -> {
            ManyToManyRightEntity right = new ManyToManyRightEntity();
            right.label = "Right";
            entityManager.persist(right);
            return right.id;
        });

        transactionTemplate.executeWithoutResult(status -> {

            ManyToManyRightEntityDto rightDto = ManyToManyRightEntityDto.Builder
                    .newInstance()
                    .id(ValueWrapper.of(rightId))
                    .build();
            ManyToManyLeftEntityDto leftDto = ManyToManyLeftEntityDto.Builder
                    .newInstance()
                    .id(ValueWrapper.of(leftId))
                    .rights(ValueWrapper.of(Set.of(ListOperation.valueOf(rightDto, ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.NONE))))
                    .build();

            // When adding the right entity to the left entity by using persistDtoToEntity
            hyperMapper.persistDtoToEntity(leftDto, null);

            ManyToManyRightEntity right = entityManager.find(ManyToManyRightEntity.class, rightId);
            ManyToManyLeftEntity left = entityManager.find(ManyToManyLeftEntity.class, leftId);

            // Then after flushing and initializing the collection, it contains the added entity
            assertTrue(left.rights.contains(right), "Collection should contain the added right entity");

        });
    }

    @Test
    @DisplayName("Should initialize collection when removing an item from the owning side of an uninitialized ManyToMany collection property")
    void testRemoveManyToManyFromUninitializedCollectionProperty() {
        // Given: a left entity with a right entity in its ManyToMany collection property, and a dto that represents removing the right entity from the left entity's collection
        Long rightId = transactionTemplate.execute(status -> {
            ManyToManyRightEntity right = new ManyToManyRightEntity();
            right.label = "Right";
            entityManager.persist(right);
            return right.id;
        });

        Long leftId = transactionTemplate.execute(status -> {
            ManyToManyLeftEntity left = new ManyToManyLeftEntity();
            left.name = "Left";
            ManyToManyRightEntity right = entityManager.find(ManyToManyRightEntity.class, rightId);
            left.rights = new java.util.HashSet<>(Set.of(right));
            entityManager.persist(left);
            return left.id;
        });

        transactionTemplate.executeWithoutResult(status -> {

            ManyToManyRightEntityDto rightDto = ManyToManyRightEntityDto.Builder
                    .newInstance()
                    .id(ValueWrapper.of(rightId))
                    .build();
            ManyToManyLeftEntityDto leftDto = ManyToManyLeftEntityDto.Builder
                    .newInstance()
                    .id(ValueWrapper.of(leftId))
                    .rights(ValueWrapper.of(Set.of(ListOperation.valueOf(rightDto, ListOperation.ListOperationType.REMOVE, ListOperation.ItemOperationType.NONE))))
                    .build();

            // When removing the right entity from the left entity by using persistDtoToEntity
            hyperMapper.persistDtoToEntity(leftDto, null);

            ManyToManyRightEntity right = entityManager.find(ManyToManyRightEntity.class, rightId);
            ManyToManyLeftEntity left = entityManager.find(ManyToManyLeftEntity.class, leftId);

            // Then after flushing and initializing the collection, it does not contain the removed right entity
            assertFalse(left.rights.contains(right), "Collection should not contain the removed right entity");

        });
    }

    @Test
    @DisplayName("Should initialize collection when adding an item to the owning side of an uninitialized OneToMany collection property")
    void testAddOwningOneToManyToUninitializedCollectionProperty() {
        // Given: a parent entity with an uninitialized owning OneToMany collection property, a potential child entity and a dto that represents the addition of the child entity
        Long parentId = transactionTemplate.execute(status -> {
            OwningOneToManyEntity parent = new OwningOneToManyEntity();
            parent.name = "Parent";
            entityManager.persist(parent);
            return parent.id;
        });

        Long childId = transactionTemplate.execute(status -> {
            OwningOneToManyChildEntity child = new OwningOneToManyChildEntity();
            child.name = "Child";
            entityManager.persist(child);
            return child.id;
        });

        transactionTemplate.executeWithoutResult(status -> {

            OwningOneToManyChildEntityDto childDto = OwningOneToManyChildEntityDto.Builder
                    .newInstance()
                    .id(ValueWrapper.of(childId))
                    .build();
            OwningOneToManyEntityDto parentDto = OwningOneToManyEntityDto.Builder
                    .newInstance()
                    .id(ValueWrapper.of(parentId))
                    .children(ValueWrapper.of(Set.of(ListOperation.valueOf(childDto, ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.NONE))))
                    .build();

            // When adding the child entity to the parent entity by using persistDtoToEntity
            hyperMapper.persistDtoToEntity(parentDto, null);

            OwningOneToManyChildEntity child = entityManager.find(OwningOneToManyChildEntity.class, childId);
            OwningOneToManyEntity parent = entityManager.find(OwningOneToManyEntity.class, parentId);

            // Then after flushing and initializing the collection, it contains the added entity
            assertTrue(parent.children.contains(child), "Collection should contain the added child entity");

        });
    }

    @Test
    @DisplayName("Should initialize collection when removing an item from the owning side of an uninitialized OneToMany collection property")
    void testRemoveOwningOneToManyFromUninitializedCollectionProperty() {
        // Given: a parent entity with a child entity in its owning OneToMany collection property, and a dto that represents removing the child entity
        Long childId = transactionTemplate.execute(status -> {
            OwningOneToManyChildEntity child = new OwningOneToManyChildEntity();
            child.name = "Child";
            entityManager.persist(child);
            return child.id;
        });

        Long parentId = transactionTemplate.execute(status -> {
            OwningOneToManyEntity parent = new OwningOneToManyEntity();
            parent.name = "Parent";
            OwningOneToManyChildEntity child = entityManager.find(OwningOneToManyChildEntity.class, childId);
            parent.children = new java.util.HashSet<>(Set.of(child));
            entityManager.persist(parent);
            return parent.id;
        });

        transactionTemplate.executeWithoutResult(status -> {

            OwningOneToManyChildEntityDto childDto = OwningOneToManyChildEntityDto.Builder
                    .newInstance()
                    .id(ValueWrapper.of(childId))
                    .build();
            OwningOneToManyEntityDto parentDto = OwningOneToManyEntityDto.Builder
                    .newInstance()
                    .id(ValueWrapper.of(parentId))
                    .children(ValueWrapper.of(Set.of(ListOperation.valueOf(childDto, ListOperation.ListOperationType.REMOVE, ListOperation.ItemOperationType.NONE))))
                    .build();

            // When removing the child entity from the parent entity by using persistDtoToEntity
            hyperMapper.persistDtoToEntity(parentDto, null);

            OwningOneToManyChildEntity child = entityManager.find(OwningOneToManyChildEntity.class, childId);
            OwningOneToManyEntity parent = entityManager.find(OwningOneToManyEntity.class, parentId);

            // Then after flushing and initializing the collection, it does not contain the removed child entity
            assertFalse(parent.children.contains(child), "Collection should not contain the removed child entity");

        });
    }
}
