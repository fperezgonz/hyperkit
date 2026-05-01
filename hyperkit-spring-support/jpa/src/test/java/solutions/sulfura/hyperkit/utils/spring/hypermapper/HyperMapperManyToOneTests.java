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
import solutions.sulfura.hyperkit.utils.test.model.dtos.DeliveryNoteDto;
import solutions.sulfura.hyperkit.utils.test.model.dtos.DeliveryNoteLineDto;
import solutions.sulfura.hyperkit.utils.test.model.scm.shipments.DeliveryNote;
import solutions.sulfura.hyperkit.utils.test.model.scm.shipments.DeliveryNoteLine;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class HyperMapperManyToOneTests {

    @Autowired
    private HyperMapper<Object> dtoMapper;
    @Autowired
    private HyperRepositoryImpl<Object> hyperRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private TransactionUtils transactionUtils;

    @Test
    @DisplayName("Persisting a dto for a manyToOne graph of transient entities should create those entities with their relationships mapped on both sides of the relationship")
    @Transactional
    void testPersistGraphOfTransientEntitiesWithOwningRoot() {
        // Given a DeliveryNoteLineDto with a DeliveryNoteDto as a nested entity
        Object userContextInfo = new Object();
        DeliveryNoteDto deliveryNoteDto = new DeliveryNoteDto();
        deliveryNoteDto.year = ValueWrapper.of("2025");
        deliveryNoteDto.code = ValueWrapper.of("DN1");

        DeliveryNoteLineDto deliveryNoteLineDto = new DeliveryNoteLineDto();
        deliveryNoteLineDto.code = ValueWrapper.of("L1");
        deliveryNoteLineDto.concept = ValueWrapper.of("Item 1");
        deliveryNoteLineDto.deliveryNote = ValueWrapper.of(deliveryNoteDto);

        // When the DTO is mapped and persisted
        DeliveryNoteLine manyToOne = dtoMapper.persistDtoToEntity(deliveryNoteLineDto, userContextInfo);

        // Then
        assertNotNull(manyToOne);
        // The fields of the root entity are mapped correctly
        assertEquals(deliveryNoteLineDto.code.get(), manyToOne.code);
        assertEquals(deliveryNoteLineDto.concept.get(), manyToOne.concept);
        var oneToManyPersisted = manyToOne.deliveryNote;
        assertNotNull(oneToManyPersisted);
        // The fields of the nested entity are mapped correctly
        assertEquals(deliveryNoteDto.code.get(), oneToManyPersisted.code);
        assertEquals(deliveryNoteDto.year.get(), oneToManyPersisted.year);
        /* After refreshing, the non-owning side of the relationship contains the new entities
           (it does not contain them before because the non-owning side gets serialized first to prevent errors due to saving entities that own relationships to transient entities)
        */
        entityManager.refresh(oneToManyPersisted);
        assertEquals(1, oneToManyPersisted.deliveryNoteLines.size());
        assertEquals(manyToOne, oneToManyPersisted.deliveryNoteLines.iterator().next());

    }


    @Test
    @DisplayName("Persisting a dto for a manyToOne graph of already persisted entities should update the values of the entities in the graph with the values from the dtos")
    @Transactional
    void testSaveManyToOneFromDto() {
        // Given a Delivery note with a line
        DeliveryNote deliveryNote = new DeliveryNote();
        deliveryNote.code = "DN1";
        deliveryNote.year = "2025";
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

        // And a DTO for the line with the delivery note
        Object userContextInfo = new Object();
        DeliveryNoteDto deliveryNoteDto = new DeliveryNoteDto();
        deliveryNoteDto.id = ValueWrapper.of(deliveryNote.id);
        deliveryNoteDto.year = ValueWrapper.of(null);

        DeliveryNoteLineDto deliveryNoteLineDto = new DeliveryNoteLineDto();
        deliveryNoteLineDto.concept = ValueWrapper.of("Item 1 updated");
        deliveryNoteLineDto.id = ValueWrapper.of(deliveryNoteLine.id);
        deliveryNoteLineDto.deliveryNote = ValueWrapper.of(deliveryNoteDto);

        deliveryNoteDto.deliveryNoteLines = ValueWrapper.of(new java.util.HashSet<>());
        deliveryNoteDto.deliveryNoteLines.get().add(ListOperation.valueOf(deliveryNoteLineDto, ListOperation.ListOperationType.NONE, ListOperation.ItemOperationType.UPDATE));

        // Act
        DeliveryNoteLine mappedDeliveryNoteLine = dtoMapper.persistDtoToEntity(deliveryNoteLineDto, userContextInfo);

        // Assert
        assertNotNull(mappedDeliveryNoteLine);
        // Assert that the field "code" keeps the original value
        assertEquals(deliveryNoteLine.code, mappedDeliveryNoteLine.code);
        // Assert that the field "concept" has been overwritten with the value from the DTO
        assertEquals(deliveryNoteLineDto.concept.get(), mappedDeliveryNoteLine.concept);
        // Assert that the field "code" keeps the original value
        assertEquals(deliveryNote.code, mappedDeliveryNoteLine.deliveryNote.code);
        // Assert that the field "year" has been overwritten with the value from the DTO
        assertNull(mappedDeliveryNoteLine.deliveryNote.year);
        assertEquals(1, mappedDeliveryNoteLine.deliveryNote.deliveryNoteLines.size());
        // Assert the retrieved entities are the same entities that were persisted
        assertEquals(deliveryNoteLine.id, mappedDeliveryNoteLine.deliveryNote.deliveryNoteLines.iterator().next().id);
        assertEquals(deliveryNote.id, mappedDeliveryNoteLine.deliveryNote.id);
    }

}
