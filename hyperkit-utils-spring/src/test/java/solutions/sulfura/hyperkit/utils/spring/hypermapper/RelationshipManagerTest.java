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
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfig;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.ManyToManyLeftEntity;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.ManyToManyRightEntity;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.ManyToOneEntity;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.OneToManyEntity;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.OwningOneToManyChildEntity;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.OwningOneToManyEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import(SpringTestConfig.class)
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

    @Test
    @DisplayName("Should initialize ManyToMany collection property on owning side when adding an item")
    void testAddManyToManyToUninitializedCollectionProperty() {
        // Given: a left entity with an uninitialized ManyToMany collection property and a potential right entity
        Long leftId = transactionTemplate.execute(status -> {
            ManyToManyLeftEntity entity = new ManyToManyLeftEntity();
            entity.name = "Left";
            entityManager.persist(entity);
            return entity.id;
        });

        Long rightId = transactionTemplate.execute(status -> {
            ManyToManyRightEntity right = new ManyToManyRightEntity();
            right.label = "Right";
            entityManager.persist(right);
            return right.id;
        });

        transactionTemplate.executeWithoutResult(status -> {
            ManyToManyRightEntity right = entityManager.find(ManyToManyRightEntity.class, rightId);
            ManyToManyLeftEntity left = entityManager.find(ManyToManyLeftEntity.class, leftId);

            // When adding the right entity to the left entity's collection property
            try {
                RelationshipManager.addToCollectionProperty(left, "rights", right);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            entityManager.flush();

            // Then the collection is initialized and contains the added entity
            assertTrue(Hibernate.isInitialized(left.rights), "Collection should be initialized for owning ManyToMany");
            assertTrue(left.rights.contains(right), "Collection should contain the added right entity");
        });
    }

    @Test
    @DisplayName("Should initialize ManyToMany collection property on owning side when removing an item")
    void testRemoveManyToManyFromUninitializedCollectionProperty() {
        // Given: a left entity with a right entity in its ManyToMany collection
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
            left.rights = new HashSet<>(Set.of(right));
            entityManager.persist(left);
            return left.id;
        });

        transactionTemplate.executeWithoutResult(status -> {
            ManyToManyLeftEntity left = entityManager.find(ManyToManyLeftEntity.class, leftId);
            ManyToManyRightEntity right = entityManager.find(ManyToManyRightEntity.class, rightId);

            // Verify the collection is uninitialized
            assertFalse(Hibernate.isInitialized(left.rights), "Collection should initially be uninitialized");

            // When removing the right entity from the left entity's collection
            try {
                RelationshipManager.removeFromCollectionProperty(left, "rights", right);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            entityManager.flush();

            // Then the collection is initialized and does not contain the removed right entity
            assertTrue(Hibernate.isInitialized(left.rights), "Collection should be initialized after removing from owning ManyToMany");
            assertFalse(left.rights.contains(right), "Collection should not contain the removed right entity");
        });
    }

    @Test
    @DisplayName("Should initialize OneToMany collection property on owning side when adding an item")
    void testAddOwningOneToManyToUninitializedCollectionProperty() {
        // Given: a parent entity with an uninitialized owning OneToMany collection property and a potential child entity
        Long parentId = transactionTemplate.execute(status -> {
            OwningOneToManyEntity entity = new OwningOneToManyEntity();
            entity.name = "Parent";
            entityManager.persist(entity);
            return entity.id;
        });

        Long childId = transactionTemplate.execute(status -> {
            OwningOneToManyChildEntity child = new OwningOneToManyChildEntity();
            child.name = "Child";
            entityManager.persist(child);
            return child.id;
        });

        transactionTemplate.executeWithoutResult(status -> {
            OwningOneToManyChildEntity child = entityManager.find(OwningOneToManyChildEntity.class, childId);
            OwningOneToManyEntity parent = entityManager.find(OwningOneToManyEntity.class, parentId);

            // When adding the child entity to the parent's collection property
            try {
                RelationshipManager.addToCollectionProperty(parent, "children", child);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            entityManager.flush();

            // Then the collection is initialized and contains the added entity
            assertTrue(Hibernate.isInitialized(parent.children), "Collection should be initialized for owning OneToMany");
            assertTrue(parent.children.contains(child), "Collection should contain the added child entity");
        });
    }

    @Test
    @DisplayName("Should initialize OneToMany collection property on owning side when removing an item")
    void testRemoveOwningOneToManyFromUninitializedCollectionProperty() {
        // Given: a parent entity with a child entity in its owning OneToMany collection
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
            parent.children = new HashSet<>(Set.of(child));
            entityManager.persist(parent);
            return parent.id;
        });

        transactionTemplate.executeWithoutResult(status -> {
            OwningOneToManyEntity parent = entityManager.find(OwningOneToManyEntity.class, parentId);
            OwningOneToManyChildEntity child = entityManager.find(OwningOneToManyChildEntity.class, childId);

            // Verify the collection is uninitialized
            assertFalse(Hibernate.isInitialized(parent.children), "Collection should initially be uninitialized");

            // When removing the child entity from the parent's collection
            try {
                RelationshipManager.removeFromCollectionProperty(parent, "children", child);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            entityManager.flush();

            // Then the collection is initialized and does not contain the removed child entity
            assertTrue(Hibernate.isInitialized(parent.children), "Collection should be initialized after removing from owning OneToMany");
            assertFalse(parent.children.contains(child), "Collection should not contain the removed child entity");
        });
    }
}
