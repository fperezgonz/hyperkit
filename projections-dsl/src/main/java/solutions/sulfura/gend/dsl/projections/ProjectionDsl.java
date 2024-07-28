package solutions.sulfura.gend.dsl.projections;

import java.lang.reflect.InvocationTargetException;

public class ProjectionDsl {

    String OPTIONAL = "@Optional";
    String MANDATORY = "@Mandatory";
    String READONLY = "@Readonly";
    String ALLOW_INSERT = "@Insertable";
    String ALLOW_DELETE = "@Deletable";
    String ALLOW_UPDATE = "@Updatable";

    public static solutions.sulfura.gend.dtos.projection.DtoProjection<?> parse(String projectionDef, Class<solutions.sulfura.gend.dtos.projection.DtoProjection> rootType) {
        try {
            //TODO parse the spec
            solutions.sulfura.gend.dtos.projection.DtoProjection<?> result = rootType.getDeclaredConstructor().newInstance();

            return result;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static solutions.sulfura.gend.dtos.projection.DtoProjection<?> parse(DtoProjectionDef annotation, Class<solutions.sulfura.gend.dtos.projection.DtoProjection> rootType) {
        return ProjectionDsl.parse(annotation.projectionDef(), rootType);
    }

}
