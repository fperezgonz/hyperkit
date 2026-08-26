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
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.ManyToOneEntity;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.OneToManyEntity;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RelationshipManagerTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    @DisplayName("Should not initialize collection when adding an item to an uninitialized collection property, and item should show up in the collection after flushing and initializing")
    void testAddToUninitializedCollectionProperty() {
        // Given: a parent entity with an uninitialized collection property and a potential child entity
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
            ManyToOneEntity firstChild = entityManager.find(ManyToOneEntity.class, firstChildId);
            OneToManyEntity parent = entityManager.find(OneToManyEntity.class, id);
            // When creating a relationship between the child and the parent
            firstChild.oneToManyEntity = parent;
            try {
                RelationshipManager.addToCollectionProperty(parent, "manyToOneEntities", firstChild);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            entityManager.flush();

            // Then the collection is not initialized, and after flushing and initializing the collection, it contains the added entity
            // Verify it remains uninitialized
            assertFalse(Hibernate.isInitialized(parent.manyToOneEntities), "Collection should not be initialized by addToCollectionProperty");
            // Verify that if the collection is initialized, it contains the new firstChild entity
            assertTrue(parent.manyToOneEntities.contains(firstChild), "Collection should contain the new firstChild entity");
        });
    }

    @Test
    @DisplayName("Should not initialize collection when removing an item from an uninitialized collection property, and item should not show up in the collection after flushing and initializing")
    void testRemoveFromUninitializedCollectionProperty() {
        // Given: a parent entity with an uninitialized collection property and a child entity
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

            OneToManyEntity parent = entityManager.find(OneToManyEntity.class, id);
            ManyToOneEntity firstChild = entityManager.find(ManyToOneEntity.class, firstChildId);

            // Verify the collection is not initialized
            assertFalse(Hibernate.isInitialized(parent.manyToOneEntities));

            // When severing the relationship between the child and the parent
            try {
                firstChild.oneToManyEntity = null;
                RelationshipManager.removeFromCollectionProperty(parent, "manyToOneEntities", firstChild);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            entityManager.flush();

            // Then
            assertFalse(Hibernate.isInitialized(parent.manyToOneEntities), "The collection should not be initialized by removeFromCollectionProperty");
            assertFalse(parent.manyToOneEntities.contains(firstChild), "After initialization, the collection should not contain the removed firstChild entity");
        });
    }

    @Test
    @DisplayName("Added child should show up in the collection")
    void testAddToInitializedCollectionProperty() {
        // Given: a parent entity with an uninitialized collection property and a potential child entity
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
            ManyToOneEntity firstChild = entityManager.find(ManyToOneEntity.class, firstChildId);
            OneToManyEntity parent = entityManager.find(OneToManyEntity.class, id);
            assertTrue(parent.manyToOneEntities.isEmpty(), "Collection should be empty");

            // When creating a relationship between the child and the parent
            firstChild.oneToManyEntity = parent;
            try {
                RelationshipManager.addToCollectionProperty(parent, "manyToOneEntities", firstChild);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            // Then the collection contains the new child
            assertTrue(Hibernate.isInitialized(parent.manyToOneEntities), "The collection should remain initialized after addToCollectionProperty");
            assertTrue(parent.manyToOneEntities.contains(firstChild), "Collection should contain the new firstChild entity");
        });
    }

    @Test
    @DisplayName("Should not initialize collection when removing an item from an uninitialized collection property, and item should not show up in the collection after flushing and initializing")
    void testRemoveFromInitializedCollectionProperty() {
        // Given: a parent entity with a collection property that contains a child entity
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

            OneToManyEntity parent = entityManager.find(OneToManyEntity.class, id);
            ManyToOneEntity firstChild = entityManager.find(ManyToOneEntity.class, firstChildId);

            // Make sure it is initialized before trying to remove the child
            assertTrue(parent.manyToOneEntities.contains(firstChild), "The collection should contain the firstChild entity");

            // When severing the relationship between the child and the parent
            try {
                firstChild.oneToManyEntity = null;
                RelationshipManager.removeFromCollectionProperty(parent, "manyToOneEntities", firstChild);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            // Then
            assertTrue(Hibernate.isInitialized(parent.manyToOneEntities), "The collection should remain initialized after removeFromCollectionProperty");
            assertFalse(parent.manyToOneEntities.contains(firstChild), "The collection should not contain the removed firstChild entity");
        });
    }
}
