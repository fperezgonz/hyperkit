package solutions.sulfura.hyperkit.dsl.projections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.sulfura.hyperkit.dsl.projections.test_aux.SourceClassGetterSetterDto;
import solutions.sulfura.hyperkit.dsl.projections.test_aux.circular_dependencies.SourceClassADto;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProjectionDslTest {

    @SuppressWarnings("unused")
    void testMethodInput(@DtoProjectionSpec(projectedClass = SourceClassGetterSetterDto.class, value = """
            stringPropertyWithGetter
            stringPropertyWithSetter @Mandatory""") SourceClassGetterSetterDto testParameter) {

    }

    @Test
    void parseSimpleDto() throws NoSuchMethodException {
        Method method = this.getClass().getDeclaredMethod("testMethodInput", SourceClassGetterSetterDto.class);
        Parameter parameter = method.getParameters()[0];
        DtoProjectionSpec annotation = parameter.getAnnotation(DtoProjectionSpec.class);
        SourceClassGetterSetterDto.Projection result = ProjectionDsl.parse(annotation, SourceClassGetterSetterDto.Projection.class);
        assertEquals(FieldConf.Presence.OPTIONAL, result.stringPropertyWithGetter.getPresence());
        assertEquals(FieldConf.Presence.MANDATORY, result.stringPropertyWithSetter.getPresence());
    }

    @Test
    void parseNestedDto() {
        SourceClassADto.Projection result = ProjectionDsl.parse("""
                property{
                    property{}
                }
                propertyArray{}
                genericProperty{}
                """, SourceClassADto.Projection.class);
        assertEquals(FieldConf.Presence.OPTIONAL, result.property.getPresence());
        assertEquals(FieldConf.Presence.OPTIONAL, result.property.dtoProjection.property.getPresence());
        assertEquals(FieldConf.Presence.OPTIONAL, result.propertyArray.getPresence());
        assertEquals(FieldConf.Presence.OPTIONAL, result.genericProperty.getPresence());
    }

    @Test
    void parseDtoWithFieldAlias() {

        SourceClassADto.Projection result = ProjectionDsl.parse("property as prop {}", SourceClassADto.Projection.class);
        assertEquals("prop", result.property.getFieldAlias());

        result = ProjectionDsl.parse("property prop {}", SourceClassADto.Projection.class);
        assertEquals("prop", result.property.getFieldAlias());

        result = ProjectionDsl.parse("""
                property prop {
                    property nestedProperty{}
                }
                propertyArray propArray{}
                genericProperty{}
                """, SourceClassADto.Projection.class);

        assertEquals("prop", result.property.getFieldAlias());
        assertEquals("nestedProperty", result.property.dtoProjection.property.getFieldAlias());
        assertEquals("propArray", result.propertyArray.getFieldAlias());
        assertEquals(null, result.genericProperty.getFieldAlias());
    }

    @Test
    void parseDtoWithLiteralFieldAlias() {
        SourceClassADto.Projection result = ProjectionDsl.parse("""
                property `pr\\`op` {
                    property `nestedPro\\\\perty`{}
                }
                genericProperty{}
                """, SourceClassADto.Projection.class);

        assertEquals("pr`op", result.property.getFieldAlias());
        assertEquals("nestedPro\\perty", result.property.dtoProjection.property.getFieldAlias());
    }

    @Test
    void failParsingWithInvalidFieldAlias() {
        assertThrows(Exception.class, () -> ProjectionDsl.parse("property `pr`op` {}", SourceClassADto.Projection.class));
        assertThrows(Exception.class, () -> ProjectionDsl.parse("property `pr\\op` {}", SourceClassADto.Projection.class));
        assertThrows(Exception.class, () -> ProjectionDsl.parse("property pr\\op {}", SourceClassADto.Projection.class));
        assertThrows(Exception.class, () -> ProjectionDsl.parse("property pr`op {}", SourceClassADto.Projection.class));
        assertThrows(Exception.class, () -> ProjectionDsl.parse("property p`r`op {}", SourceClassADto.Projection.class));
    }

    @Test
    void failParsingWithInvalidPropertyName() {
        assertThrows(Exception.class, () -> ProjectionDsl.parse("pro\\`perty {}", SourceClassADto.Projection.class));
        assertThrows(Exception.class, () -> ProjectionDsl.parse("pro\\\\perty {}", SourceClassADto.Projection.class));
        assertThrows(Exception.class, () -> ProjectionDsl.parse("pro\\perty {}", SourceClassADto.Projection.class));
    }

    @Test
    void parseDtoWithProjectionTypeAlias() {
        SourceClassADto.Projection result = ProjectionDsl.parse("property prop: ProjectedProperty {}", SourceClassADto.Projection.class);
        assertEquals("prop", result.property.getFieldAlias());
        result = ProjectionDsl.parse("property prop : ProjectedProperty {}", SourceClassADto.Projection.class);
        assertEquals("prop", result.property.getFieldAlias());


        result = ProjectionDsl.parse("""
                property prop : ProjectedProperty {
                    property nestedProperty : NestedProjectedProperty{}
                }
                propertyArray propArray :  ProjectedArrayProperty {}
                genericProperty genProp {}
                """, SourceClassADto.Projection.class);

        assertEquals("prop", result.property.getFieldAlias());
        assertEquals("nestedProperty", result.property.dtoProjection.property.getFieldAlias());
        assertEquals("propArray", result.propertyArray.getFieldAlias());
        assertEquals("genProp", result.genericProperty.getFieldAlias());

        assertEquals("ProjectedProperty", result.property.projectionTypeAlias);
        assertEquals("NestedProjectedProperty", result.property.dtoProjection.property.projectionTypeAlias);
        assertEquals("ProjectedArrayProperty", result.propertyArray.projectionTypeAlias);
        assertEquals(null, result.genericProperty.projectionTypeAlias);
    }

    @Test
    void failParsingWithInvalidTypeAlias() {
        assertThrows(Exception.class, () -> ProjectionDsl.parse("property : `pr`op` {}", SourceClassADto.Projection.class));
        assertThrows(Exception.class, () -> ProjectionDsl.parse("property : `pr\\op` {}", SourceClassADto.Projection.class));
        assertThrows(Exception.class, () -> ProjectionDsl.parse("property : pr\\op {}", SourceClassADto.Projection.class));
        assertThrows(Exception.class, () -> ProjectionDsl.parse("property : pr`op {}", SourceClassADto.Projection.class));
        assertThrows(Exception.class, () -> ProjectionDsl.parse("property : p`r`op {}", SourceClassADto.Projection.class));
    }


}