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
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.spring.HyperRepositoryImpl;
import solutions.sulfura.hyperkit.utils.spring.TransactionUtils;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.ManyToOneEntity;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.OneToManyEntity;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto.ManyToOneEntityDto;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto.OneToManyEntityDto;
import solutions.sulfura.hyperkit.utils.test.model.dtos.DeliveryNoteDto;
import solutions.sulfura.hyperkit.utils.test.model.dtos.DeliveryNoteLineDto;
import solutions.sulfura.hyperkit.utils.test.model.scm.shipments.DeliveryNote;
import solutions.sulfura.hyperkit.utils.test.model.scm.shipments.DeliveryNoteLine;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class HyperMapperOneToManyTests {

    @Autowired
    private HyperMapper<Object> dtoMapper;
    @Autowired
    private HyperRepositoryImpl<Object> hyperRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private TransactionUtils transactionUtils;


    @Test
    @DisplayName("Persisting a dto for a oneToMany graph of transient entities should create those entities with their relationships mapped on both sides of the relationship")
    @Transactional
    void testPersistTransientOneToMany() {

        // Given: Two entities in a OneToManyRelationship
        Object userContextInfo = new Object();

        DeliveryNoteDto deliveryNoteDto = new DeliveryNoteDto();
        deliveryNoteDto.year = ValueWrapper.of("2026");

        DeliveryNoteLineDto deliveryNoteLine = new DeliveryNoteLineDto();
        deliveryNoteLine.concept = ValueWrapper.of("Toaster");

        deliveryNoteDto.deliveryNoteLines = ValueWrapper.of(new java.util.HashSet<>());
        deliveryNoteDto.deliveryNoteLines.get().add(ListOperation.valueOf(deliveryNoteLine,
                ListOperation.ListOperationType.ADD,
                ListOperation.ItemOperationType.UPSERT)
        );

        // When: the DTO is mapped and persisted
        DeliveryNote persistedDeliveryNote = dtoMapper.persistDtoToEntity(deliveryNoteDto, userContextInfo);

        // Then
        assertNotNull(persistedDeliveryNote);
        // The field "year" keeps the original value
        assertEquals(deliveryNoteDto.year.get(), persistedDeliveryNote.year);
        var persistedLine = persistedDeliveryNote.deliveryNoteLines.iterator().next();
        // The field "concept" keeps the original value
        assertEquals(deliveryNoteLine.concept.get(), persistedLine.concept);
        assertEquals(1, persistedDeliveryNote.deliveryNoteLines.size());
        // The retrieved entities are the same entities that were persisted
        assertEquals(deliveryNoteLine.concept.get(), persistedLine.concept);
        assertEquals(deliveryNoteDto.year.get(), persistedLine.deliveryNote.year);

        entityManager.refresh(persistedLine);

        // The relationships are persisted correctly
        assertTrue(persistedDeliveryNote.deliveryNoteLines.contains(persistedLine));
        assertSame(persistedLine.deliveryNote, persistedDeliveryNote);

    }

    @Test
    @DisplayName("Persisting a dto for a oneToMany graph of already persisted entities should update the values of the entities in the graph with the values from the dto")
    @Transactional
    void testSavePersistentOneToMany() {
        // Given: Two entities in a OneToManyRelationship
        DeliveryNote deliveryNote = new DeliveryNote();
        deliveryNote.year = "2026";
        deliveryNote.deliveryNoteLines = new java.util.HashSet<>();

        DeliveryNoteLine deliveryNoteLine = new DeliveryNoteLine();
        deliveryNoteLine.code = "L1";
        deliveryNoteLine.concept = "Item 1";
        deliveryNoteLine.deliveryNote = deliveryNote;

        deliveryNote.deliveryNoteLines.add(deliveryNoteLine);

        hyperRepository.save(deliveryNote, null);
        hyperRepository.save(deliveryNoteLine, null);

        entityManager.flush();
        entityManager.clear();

        Object userContextInfo = new Object();
        DeliveryNoteDto deliveryNoteDto = new DeliveryNoteDto();
        deliveryNoteDto.id = ValueWrapper.of(deliveryNote.id);
        deliveryNoteDto.year = ValueWrapper.of("2025");

        DeliveryNoteLineDto deliveryNoteLineDto = new DeliveryNoteLineDto();
        deliveryNoteLineDto.concept = ValueWrapper.of("Item 1 updated");
        deliveryNoteLineDto.id = ValueWrapper.of(deliveryNoteLine.id);
        deliveryNoteLineDto.deliveryNote = ValueWrapper.of(deliveryNoteDto);

        deliveryNoteDto.deliveryNoteLines = ValueWrapper.of(new java.util.HashSet<>());
        deliveryNoteDto.deliveryNoteLines.get().add(ListOperation.valueOf(deliveryNoteLineDto, ListOperation.ListOperationType.NONE, ListOperation.ItemOperationType.UPDATE));

        // Act
        DeliveryNote mappedDeliverNote = dtoMapper.persistDtoToEntity(deliveryNoteDto, userContextInfo);

        // Assert
        assertNotNull(mappedDeliverNote);
        //Assert that the field "year" has been overwritten with the values from the DTO
        assertEquals(deliveryNoteDto.year.get(), mappedDeliverNote.year);
        var mappedLines = mappedDeliverNote.deliveryNoteLines.iterator().next();
        //Assert that the field "code" keeps the original value
        assertEquals(deliveryNoteLine.code, mappedLines.code);
        //Assert that the field "concept" has been overwritten with the values from the DTO
        assertEquals(deliveryNoteLineDto.concept.get(), mappedLines.concept);
        assertEquals(1, mappedDeliverNote.deliveryNoteLines.size());
        //Assert the retrieved entities are the same entities that were persisted
        assertEquals(deliveryNoteLine.id, mappedLines.id);
        assertEquals(deliveryNote.id, mappedLines.deliveryNote.id);
    }


    @Test
    @DisplayName("ListOperations with REMOVE should sever the relationship on both sides of OneToMany relationships")
    @Transactional
    void testRemovalOperationFromDto() {
        // Given: A delivery note with two lines
        DeliveryNote deliveryNote = new DeliveryNote();
        deliveryNote.year = "2026";
        deliveryNote.deliveryNoteLines = new java.util.HashSet<>();

        DeliveryNoteLine deliveryNoteLine1 = new DeliveryNoteLine();
        deliveryNoteLine1.code = "L1";
        deliveryNoteLine1.concept = "Item 1";
        deliveryNoteLine1.deliveryNote = deliveryNote;

        DeliveryNoteLine deliveryNoteLine2 = new DeliveryNoteLine();
        deliveryNoteLine2.code = "L2";
        deliveryNoteLine2.concept = "Item 2";
        deliveryNoteLine2.deliveryNote = deliveryNote;

        deliveryNote.deliveryNoteLines.add(deliveryNoteLine1);
        deliveryNote.deliveryNoteLines.add(deliveryNoteLine2);

        hyperRepository.save(deliveryNote, null);
        hyperRepository.save(deliveryNoteLine1, null);
        hyperRepository.save(deliveryNoteLine2, null);

        entityManager.flush();
        entityManager.clear();

        // And a DTO with a removal operation for the first line
        DeliveryNoteDto deliveryNoteDto = new DeliveryNoteDto();
        deliveryNoteDto.id = ValueWrapper.of(deliveryNote.id);

        DeliveryNoteLineDto deliveryNoteLineDto1 = new DeliveryNoteLineDto();
        deliveryNoteLineDto1.id = ValueWrapper.of(deliveryNoteLine1.id);

        deliveryNoteDto.deliveryNoteLines = ValueWrapper.of(new java.util.HashSet<>());
        deliveryNoteDto.deliveryNoteLines.get().add(ListOperation.valueOf(deliveryNoteLineDto1, ListOperation.ListOperationType.REMOVE, ListOperation.ItemOperationType.NONE));

        // When the DTO is mapped to an entity
        DeliveryNote mappedDeliveryNote = dtoMapper.persistDtoToEntity(deliveryNoteDto, null);

        // Assert
        assertNotNull(mappedDeliveryNote);
        // The retrieved delivery note is the expected one
        assertEquals(deliveryNote.id, mappedDeliveryNote.id);
        // Assert that the field "year" keeps the original value
        assertEquals(deliveryNote.year, mappedDeliveryNote.year);
        // Assert the number of lines entities is updated
        assertEquals(1, mappedDeliveryNote.deliveryNoteLines.size());
        // Assert the remaining line is the one that was not removed
        var remainingEntity = mappedDeliveryNote.deliveryNoteLines.iterator().next();
        assertEquals(deliveryNoteLine2.id, remainingEntity.id);

        // Assert that the removed line is no longer associated with the delivery note
        var removedLine = hyperRepository.findById(DeliveryNoteLine.class, deliveryNoteLine1.id, null).orElseThrow();
        assertNull(removedLine.deliveryNote);
    }

    @Test
    @DisplayName("Should persist Set modifications via ListOperation when using persistDtoToEntity")
    void testPersistSetModificationsWithListOperation() {
        // Given: a parent entity with two children
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

        // The corresponding DTOs
        OneToManyEntityDto parentDto = new OneToManyEntityDto();
        parentDto.id = ValueWrapper.of(parent.id);

        ManyToOneEntityDto child1Dto = new ManyToOneEntityDto();
        child1Dto.id = ValueWrapper.of(child1.id);

        ManyToOneEntityDto child2Dto = new ManyToOneEntityDto();
        child2Dto.id = ValueWrapper.of(child2.id);
        child2Dto.description = ValueWrapper.of("Child2-updated");
        child2Dto.oneToManyEntity = ValueWrapper.of(parentDto);

        // A DTO for a new entity
        ManyToOneEntityDto child3Dto = new ManyToOneEntityDto();
        child3Dto.name = ValueWrapper.of("Child3");
        child3Dto.description = ValueWrapper.of("Child3");
        child3Dto.oneToManyEntity = ValueWrapper.of(parentDto);

        // A List of operations that remove child1, update child2 and add child3
        java.util.Set<ListOperation<ManyToOneEntityDto>> ops = new java.util.HashSet<>();
        ops.add(ListOperation.valueOf(child1Dto, ListOperation.ListOperationType.REMOVE, ListOperation.ItemOperationType.NONE));
        ops.add(ListOperation.valueOf(child2Dto, ListOperation.ListOperationType.NONE, ListOperation.ItemOperationType.UPDATE));
        ops.add(ListOperation.valueOf(child3Dto, ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.INSERT));
        parentDto.manyToOneEntities = ValueWrapper.of(ops);

        // When: persisting DTO to entity
        transactionUtils.runOnNewTransaction(() -> {

            OneToManyEntity persisted = dtoMapper.persistDtoToEntity(parentDto, null);

            // Then: the relationship set reflects REMOVE/UPDATE/ADD
            assertNotNull(persisted);
            assertEquals(parent.id, persisted.id);
            assertEquals(2, persisted.manyToOneEntities.size(), "Parent should have exactly two children after modifications");

            // Child1 has been removed
            boolean containsChild1 = persisted.manyToOneEntities.stream().anyMatch(e -> child1.id.equals(e.id));
            assertFalse(containsChild1, "Child1 should have been removed from the parent's set");
            var persistedChild1 = hyperRepository.findById(ManyToOneEntity.class, child1.id, null).orElseThrow();
            boolean right1HasParent = persistedChild1.oneToManyEntity != null;
            assertFalse(right1HasParent, "Parent should be removed from Child1");

            // Child2 has been updated
            ManyToOneEntity persistedChild2 = persisted.manyToOneEntities.stream()
                    .filter(e -> child2.id.equals(e.id))
                    .findFirst()
                    .orElse(null);
            assertNotNull(persistedChild2, "Child2 should still be present");
            assertEquals("Child2-updated", persistedChild2.description, "Child2 description should be updated");
            assertNotNull(persistedChild2.oneToManyEntity);
            assertEquals(parent.id, persistedChild2.oneToManyEntity.id);

            // Child 3 has been added
            boolean containsChild3 = persisted.manyToOneEntities.stream()
                    .anyMatch(e -> child3Dto.name.get().equals(e.name));
            assertTrue(containsChild3, "Child3 should have been added to the parent's set");

            // There are no other children
            assertEquals(2, persisted.manyToOneEntities.size());

            return null;

        });

        // Double check that after the transaction was commited everything is still fine
        transactionUtils.runOnNewTransaction(() -> {

            OneToManyEntity persisted = hyperRepository.findById(OneToManyEntity.class, parent.id, null).orElseThrow();

            // Then: the relationship set reflects REMOVE/UPDATE/ADD
            assertNotNull(persisted);
            assertEquals(parent.id, persisted.id);
            assertEquals(2, persisted.manyToOneEntities.size(), "Parent should have exactly two children after modifications");

            // Assert child1 has been removed
            boolean containsChild1 = persisted.manyToOneEntities.stream().anyMatch(e -> child1.id.equals(e.id));
            assertFalse(containsChild1, "Child1 should have been removed from the parent's set");
            var persistedChild1 = hyperRepository.findById(ManyToOneEntity.class, child1.id, null).orElseThrow();
            boolean right1HasParent = persistedChild1.oneToManyEntity != null;
            assertFalse(right1HasParent, "Parent should be removed from Child1");

            // Assert child2 has been updated
            ManyToOneEntity persistedChild2 = persisted.manyToOneEntities.stream()
                    .filter(e -> child2.id.equals(e.id))
                    .findFirst()
                    .orElse(null);
            assertNotNull(persistedChild2, "Child2 should still be present");
            assertEquals("Child2-updated", persistedChild2.description, "Child2 description should be updated");
            assertNotNull(persistedChild2.oneToManyEntity);
            assertEquals(parent.id, persistedChild2.oneToManyEntity.id);

            // Assert child 3 has been added
            boolean containsChild3 = persisted.manyToOneEntities.stream()
                    .anyMatch(e -> child3Dto.name.get().equals(e.name));
            assertTrue(containsChild3, "Child3 should have been added to the parent's set");

            // There are no other children
            assertEquals(2, persisted.manyToOneEntities.size());

            return null;

        });

    }

    @Test
    @DisplayName("When using a dto to add entities to a oneToMany relationship of a persisted entity, backreferences should be mapped to the same entity instance")
    @Transactional
    void testMapBackReferences() {
        // Given a persisted delivery note
        DeliveryNote deliveryNote = new DeliveryNote();
        deliveryNote.year = "2026";
        deliveryNote.deliveryNoteLines = new HashSet<>();
        hyperRepository.save(deliveryNote, null);

        entityManager.flush();
        entityManager.clear();

        // And DTOs for the delivery note and two new lines
        DeliveryNoteDto deliveryNoteDto = new DeliveryNoteDto();
        deliveryNoteDto.id = ValueWrapper.of(deliveryNote.id);

        DeliveryNoteLineDto deliveryNoteLineDto1 = new DeliveryNoteLineDto();
        deliveryNoteLineDto1.code = ValueWrapper.of("L1");

        DeliveryNoteLineDto deliveryNoteLineDto2 = new DeliveryNoteLineDto();
        deliveryNoteLineDto2.code = ValueWrapper.of("L2");

        // Add the lines to the delivery note dto
        deliveryNoteDto.deliveryNoteLines = ValueWrapper.of(new HashSet<>());
        deliveryNoteDto.deliveryNoteLines.get().add(
                ListOperation.valueOf(deliveryNoteLineDto1, ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.INSERT)
        );
        deliveryNoteDto.deliveryNoteLines.get().add(
                ListOperation.valueOf(deliveryNoteLineDto2, ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.INSERT)
        );

        // When mapping the dto to an entity
        DeliveryNote result = dtoMapper.mapDtoToEntity(deliveryNoteDto, null).mappedValue();

        // Then the delivery note and the lines match the dtos
        assertEquals(deliveryNote.id, result.id, "The retrieved delivery note should be the same as the one that was persisted");
        assertEquals(2, result.deliveryNoteLines.size(), "Delivery note should have one line");
        DeliveryNoteLine deliveryNoteLine1 = result.deliveryNoteLines.stream().filter(l -> "L1".equals(l.code)).findFirst().orElseThrow();
        assertEquals(result, deliveryNoteLine1.deliveryNote, "Child should reference the same parent instance");
        DeliveryNoteLine deliveryNoteLine2 = result.deliveryNoteLines.stream().filter(l -> "L1".equals(l.code)).findFirst().orElseThrow();
        assertEquals(result, deliveryNoteLine2.deliveryNote, "Child should reference the same parent instance");

    }

    @Test
    @DisplayName("When using a dto to create graph of entities for a oneToMany relationship, backreferences should be mapped to the same entity instance")
    @Transactional
    void testMapBackReferencesToNewEntity() {
        // Given DTOs for a new delivery note with two lines
        DeliveryNoteDto deliveryNoteDto = new DeliveryNoteDto();
        deliveryNoteDto.id = ValueWrapper.empty();

        DeliveryNoteLineDto deliveryNoteLineDto1 = new DeliveryNoteLineDto();
        deliveryNoteLineDto1.code = ValueWrapper.of("L1");

        DeliveryNoteLineDto deliveryNoteLineDto2 = new DeliveryNoteLineDto();
        deliveryNoteLineDto2.code = ValueWrapper.of("L2");

        deliveryNoteDto.deliveryNoteLines = ValueWrapper.of(new HashSet<>());
        deliveryNoteDto.deliveryNoteLines.get().add(
                ListOperation.valueOf(deliveryNoteLineDto1, ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.INSERT)
        );
        deliveryNoteDto.deliveryNoteLines.get().add(
                ListOperation.valueOf(deliveryNoteLineDto2, ListOperation.ListOperationType.ADD, ListOperation.ItemOperationType.INSERT)
        );

        // When mapping the dto to an entity
        DeliveryNote result = dtoMapper.mapDtoToEntity(deliveryNoteDto, null).mappedValue();

        // Then the delivery note and the lines match the dtos
        assertEquals(2, result.deliveryNoteLines.size(), "Delivery note should have one line");
        DeliveryNoteLine deliveryNoteLine1 = result.deliveryNoteLines.stream().filter(l -> "L1".equals(l.code)).findFirst().orElseThrow();
        assertEquals(result, deliveryNoteLine1.deliveryNote, "Child should reference the same parent instance");
        DeliveryNoteLine deliveryNoteLine2 = result.deliveryNoteLines.stream().filter(l -> "L1".equals(l.code)).findFirst().orElseThrow();
        assertEquals(result, deliveryNoteLine2.deliveryNote, "Child should reference the same parent instance");
    }

}
