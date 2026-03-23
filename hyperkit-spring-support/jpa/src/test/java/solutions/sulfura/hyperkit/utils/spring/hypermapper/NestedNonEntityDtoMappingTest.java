package solutions.sulfura.hyperkit.utils.spring.hypermapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.nonentity.PlainNestedDto;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.nonentity.PlainRoot;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.nonentity.PlainRootDto;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class NestedNonEntityDtoMappingTest {

    @Autowired
    private HyperMapper<Object> dtoMapper;

    @Test
    @DisplayName("Should map nested DTOs when the source class is not an entity")
    void mapNestedDtosWhoseSourceIsNotEntity() {
        // Given: a non-entity DTO type with a nested DTO inside
        PlainNestedDto innermost = new PlainNestedDto();
        innermost.id = ValueWrapper.of(10L);
        innermost.name = ValueWrapper.of("Inner");

        PlainNestedDto middleNested = new PlainNestedDto();
        middleNested.id = ValueWrapper.of(5L);
        middleNested.name = ValueWrapper.of("MidNested");
        middleNested.child = ValueWrapper.of(innermost);

        PlainRootDto root = new PlainRootDto();
        root.id = ValueWrapper.of(1L);
        root.title = ValueWrapper.of("Root");
        root.nested = ValueWrapper.of(middleNested);

        // When: mapping the DTO to a non-entity object
        var toEntity = dtoMapper.mapDtoToEntity(root, null);
        PlainRoot mapped = toEntity.getEntity();

        // Then: the mapping should preserve the nested structure
        assertNotNull(mapped);
        assertEquals(1L, mapped.id);
        assertEquals("Root", mapped.title);

        assertNotNull(mapped.nested);
        var lvl1 = mapped.nested;
        assertEquals(5L, lvl1.id);
        assertEquals("MidNested", lvl1.name);
        assertNotNull(lvl1.child);

        var lvl2 = lvl1.child;
        assertEquals(10L, lvl2.id);
        assertEquals("Inner", lvl2.name);
        assertNull(lvl2.child);
    }
}
