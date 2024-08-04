package solutions.sulfura.gend.dsl.projections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.sulfura.gend.dsl.projections.test_aux.SourceClassGetterSetterDto;
import solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassADto;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.*;

class ProjectionDslTest {

    void testMethodInput(@DtoProjectionDef(projectionDef = """
            stringPropertyWithGetter
            stringPropertyWithSetter @Mandatory""") SourceClassGetterSetterDto testParameter) {

    }

    @Test
    void parseSimpleDto() throws NoSuchMethodException {
        Method method = this.getClass().getDeclaredMethod("testMethodInput", SourceClassGetterSetterDto.class);
        Parameter parameter = method.getParameters()[0];
        DtoProjectionDef annotation = parameter.getAnnotation(DtoProjectionDef.class);
        SourceClassGetterSetterDto.Projection result = ProjectionDsl.parse(annotation, SourceClassGetterSetterDto.Projection.class);
        Assertions.assertEquals(FieldConf.Presence.OPTIONAL, result.stringPropertyWithGetter.getPresence());
        Assertions.assertEquals(FieldConf.Presence.MANDATORY, result.stringPropertyWithSetter.getPresence());
    }

    @Test
    void parseNestedDto() throws NoSuchMethodException {
        SourceClassADto.Projection result = ProjectionDsl.parse("""
                property{
                    property{}
                }
                propertyArray{}
                genericProperty{}
                """, SourceClassADto.Projection.class);
        Assertions.assertEquals(FieldConf.Presence.OPTIONAL, result.property.getPresence());
        Assertions.assertEquals(FieldConf.Presence.OPTIONAL, result.property.dtoProjection.property.getPresence());
        Assertions.assertEquals(FieldConf.Presence.OPTIONAL, result.propertyArray.getPresence());
        Assertions.assertEquals(FieldConf.Presence.OPTIONAL, result.genericProperty.getPresence());
    }


}