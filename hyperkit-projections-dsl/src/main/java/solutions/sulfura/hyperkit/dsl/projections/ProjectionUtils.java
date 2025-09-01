package solutions.sulfura.hyperkit.dsl.projections;

import org.jspecify.annotations.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ProjectionUtils {


    public static DtoProjectionSpec getMethodProjectionAnnotationOrMetaAnnotation(@NonNull Method method) {
        var result = getReturnTypeAnnotationInfo(method, DtoProjectionSpec.class);
        return result == null ? null : result.targetAnnotation;
    }

    public static <T extends Annotation> AnnotationInfo<Annotation, T> getFirstMetaAnnotatedAnnotationInfo(AnnotatedType annotatedType, Annotation[] annotations, Class<T> metaAnnotationClass) {

        for (var annotation : annotations) {
            var projectionAnnotation = annotation.annotationType().getAnnotation(metaAnnotationClass);
            if (projectionAnnotation != null) {
                return new AnnotationInfo<>(annotatedType, annotation, projectionAnnotation);
            }
        }

        return null;

    }

    /**
     * Returns the first annotation meta-annotated with the specified meta-annotation
     */
    public static <T extends Annotation> AnnotationInfo<Annotation, T> getReturnTypeAnnotationInfo(@NonNull Method method, @NonNull Class<T> metaAnnotationClass) {

        // Get the method's return type annotation
        T directAnnotation = method.getAnnotation(metaAnnotationClass);

        if (directAnnotation != null) {
            return new AnnotationInfo<>(method.getAnnotatedReturnType(), directAnnotation);
        }

        // If the method itself doesn't have the annotation, check if it has a meta-annotation
        return getFirstMetaAnnotatedAnnotationInfo(method.getAnnotatedReturnType(), method.getAnnotations(), metaAnnotationClass);

    }

    /**
     * Returns the annotation info of a direct annotation of the specified type, or the first annotation meta-annotated with the specified type
     */
    public static <T extends Annotation> AnnotationInfo<Annotation, T> getAnnotationInfo(@NonNull Parameter parameter, @NonNull Class<T> metaAnnotationClass) {

        T directAnnotation = parameter.getAnnotation(metaAnnotationClass);

        if (directAnnotation != null) {
            return new AnnotationInfo<>(parameter.getAnnotatedType(), directAnnotation);
        }

        return getFirstMetaAnnotatedAnnotationInfo(parameter.getAnnotatedType(), parameter.getAnnotations(), metaAnnotationClass);

    }

    public static class AnnotationInfo<D extends Annotation, M extends Annotation> {
        public AnnotatedType annotatedType;
        /**
         * This is the annotation on the type
         */
        public D directAnnotation;
        /**
         * This is the annotation that you were looking for
         */
        public M targetAnnotation;

        public AnnotationInfo() {
        }

        public AnnotationInfo(@NonNull AnnotatedType annotatedType, @NonNull D directAnnotation, @NonNull M targetAnnotation) {
            this.annotatedType = annotatedType;
            this.directAnnotation = directAnnotation;
            this.targetAnnotation = targetAnnotation;
        }

        public <A extends Annotation> AnnotationInfo(@NonNull AnnotatedType annotatedType, @NonNull D directAnnotation) {
            this.annotatedType = annotatedType;
            this.directAnnotation = directAnnotation;
            // If only the direct annotation is supplied, this is a direct annotation AnnotationInfo and the meta-annotation must match the direct annotation
            //noinspection unchecked
            this.targetAnnotation = (M) this.directAnnotation;
        }

        public boolean isDirectlyAnnotated() {
            return targetAnnotation == directAnnotation;
        }

        public boolean isMetaAnnotated() {
            return targetAnnotation != directAnnotation;
        }

    }

}
