package solutions.sulfura.hyperkit.dsl.projections;

import org.jspecify.annotations.NonNull;

import java.lang.reflect.Method;

public class ProjectionUtils {


    public static DtoProjectionSpec getMethodProjectionAnnotationOrMetaAnnotation(@NonNull Method method) {

        // Get the method's return type annotation
        DtoProjectionSpec projectionAnnotation = method.getAnnotation(DtoProjectionSpec.class);

        // If the method itself doesn't have the annotation, check if it has a meta-annotation
        if (projectionAnnotation != null) {
            return projectionAnnotation;
        }

        for (var annotation : method.getAnnotations()) {
            projectionAnnotation = annotation.annotationType().getAnnotation(DtoProjectionSpec.class);
            if (projectionAnnotation != null) {
                return projectionAnnotation;
            }
        }

        return null;

    }

}
