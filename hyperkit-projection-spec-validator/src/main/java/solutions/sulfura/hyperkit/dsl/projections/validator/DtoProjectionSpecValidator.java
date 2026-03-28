package solutions.sulfura.hyperkit.dsl.projections.validator;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;

import java.lang.reflect.*;
import java.util.*;

/**
 * Validator for {@link DtoProjectionSpec} annotations.
 */
public class DtoProjectionSpecValidator {

    /**
     * Validates all {@link DtoProjectionSpec} annotations on all classes found in the given base packages.
     * If no base packages are provided, it scans all packages.
     *
     * @param basePackages the base packages to scan
     * @return a list of validation error messages, or an empty list if all are valid
     */
    public ProjectionValidationResult validateAll(String... basePackages) {

        ProjectionValidationResult result = new ProjectionValidationResult();

        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(basePackages)
                .scan()) {
            for (ClassInfo classInfo : scanResult.getAllClasses()) {
                mergeValidationResults(result, validate(classInfo.loadClass()));
            }
        }

        return result;

    }

    /**
     * Validates all {@link DtoProjectionSpec} annotations on the given classes.
     *
     * @param classes the classes to validate
     * @return a list of validation error messages, or an empty list if all are valid
     */
    public ProjectionValidationResult validate(Class<?>... classes) {
        ProjectionValidationResult result = new ProjectionValidationResult();
        for (Class<?> clazz : classes) {
            mergeValidationResults(result, validate(clazz));
        }
        return result;
    }

    /**
     * Validates all {@link DtoProjectionSpec} annotations on the given class, including those on methods and parameters.
     * Also validates custom annotations that are annotated with {@link DtoProjectionSpec}.
     *
     * @param clazz the class to validate
     * @return a list of validation error messages, or an empty list if valid
     */
    private ProjectionValidationResult validate(Class<?> clazz) {
        ProjectionValidationResult result = new ProjectionValidationResult();

        // Validate methods
        for (Method method : clazz.getDeclaredMethods()) {
            mergeValidationResults(result, validateElement(method));

            // Validate parameters
            for (Parameter parameter : method.getParameters()) {
                mergeValidationResults(result, validateElement(parameter));
            }
        }
        for (Class<?> innerClass : clazz.getDeclaredClasses()) {
            var innerClassValidationResult = validate(innerClass);
            mergeValidationResults(result, innerClassValidationResult);
        }

        return result;
    }

    private ProjectionValidationResult validateElement(AnnotatedElement element) {

        DtoProjectionSpec spec = solutions.sulfura.hyperkit.dsl.projections.ProjectionUtils.findDtoProjectionSpec(element);

        if (spec == null) {
            return new ProjectionValidationResult();
        }

        return validateProjectionSpec(spec, element);

    }

    private ProjectionValidationResult validateProjectionSpec(DtoProjectionSpec annotation, AnnotatedElement element) {

        ProjectionValidationResult result = new ProjectionValidationResult();

        try {

            @SuppressWarnings("rawtypes")
            Class<? extends Dto> projectedClass = annotation.projectedClass();
            @SuppressWarnings("unchecked")
            DtoProjection<Dto<?>> projection = ProjectionDsl.parse(annotation);
            ProjectionValidationResult validationResult = validateProjection(annotation.namespace(), projection);
            mergeValidationResults(result, validationResult);
            result.projections.put(fullyQualifiedProjectionTypeName(annotation.namespace(), projection), projection);
            result.errors.addAll(validateTypeAssignability(element, projectedClass));

        } catch (Exception e) {
            result.errors.add("Failed to parse projection on " + element + ": " + e.getMessage());

        }

        return result;

    }

    private ProjectionValidationResult validateProjection(String namespace, DtoProjection<Dto<?>> projection) {

        ProjectionValidationResult result = new ProjectionValidationResult();

        for (Field field : projection.getClass().getFields()) {

            if (!DtoFieldConf.class.isAssignableFrom(field.getType())) {
                continue;
            }

            try {

                //noinspection unchecked
                DtoFieldConf<DtoProjection<Dto<?>>> dtoFieldConf = ((DtoFieldConf<DtoProjection<Dto<?>>>) field.get(projection));

                if (dtoFieldConf == null) {
                    continue;
                }

                DtoProjection<Dto<?>> nestedProjection = dtoFieldConf.dtoProjection;

                String fullyQualifiedProjectionTypeName = fullyQualifiedProjectionTypeName(namespace, nestedProjection);

                if (result.hasDifferentProjectionForSameNamespaceAndAlias(fullyQualifiedProjectionTypeName, nestedProjection)) {
                    result.errors.add("Duplicate projection type alias " + fullyQualifiedProjectionTypeName + " in " + projection.getClass().getName());
                }

                result.errors.addAll(validateNamespaceCollisionsValidationResults(result, validateProjection(fullyQualifiedProjectionTypeName, nestedProjection)));
                result.projections.put(fullyQualifiedProjectionTypeName, nestedProjection);
                continue;

            } catch (IllegalAccessException e) {
                result.errors.add("Failed to access projection field " + field.getName() + " on " + projection.getClass().getName());
                continue;
            }

        }

        return result;

    }

    private List<String> validateTypeAssignability(AnnotatedElement element, Class<? extends Dto> projectedClass) {

        if (element instanceof Method method) {
            return validateType(method.getGenericReturnType(), projectedClass, element);
        }

        if (element instanceof Parameter parameter) {
            return validateType(parameter.getParameterizedType(), projectedClass, element);
        }

        return Collections.emptyList();
    }

    private List<String> validateType(java.lang.reflect.Type targetType, Class<? extends Dto> projectedClass, AnnotatedElement element) {
        Class<?> targetClass = getRawType(targetType);

        if (Object.class.equals(targetClass)) {
            return Collections.emptyList();
        }

        if (targetClass == null || void.class.equals(targetClass)) {
            return List.of("Target type is null or void on " + element);
        }

//        // Check if it's a DtoProjection subclass (common for returnValueProjection parameters)
//        if (DtoProjection.class.isAssignableFrom(targetClass)) {
//
//            // It's a projection. Check if it's the correct projection for the projectedClass.
//            Class<? extends DtoProjection<Dto<?>>> expectedProjectionClass = ProjectionUtils.findDefaultProjectionClass(projectedClass);
//
//            if (expectedProjectionClass != null && !targetClass.isAssignableFrom(expectedProjectionClass)) {
//                return List.of("Projected class " + projectedClass.getName() + " has projection " + expectedProjectionClass.getName() + " which is not assignable to " + targetClass.getName() + " on " + element);
//            }
//
//            return Collections.emptyList();
//
//        }

        // Check if it's a Dto or a collection of Dtos
        if (Dto.class.isAssignableFrom(targetClass)) {

            if (targetClass.isAssignableFrom(projectedClass)) {
                return Collections.emptyList();
            }

            return List.of("Projected class " + projectedClass.getName() + " is not assignable to " + targetClass.getName() + " on " + element);

        } else if (isParameterizedType(targetType)) {

//            if (java.util.Collection.class.isAssignableFrom(targetClass)) {
//
//                if (validateAnyGenericTypeArgumentIsOfTypeDto(targetType, projectedClass)) {
//                    return Collections.emptyList();
//                }
//
//                return List.of("Projected class " + projectedClass.getName() + " is not assignable to collection element type " + targetClass.getName() + " on " + element);
//
//            }

            if (validateAnyGenericTypeArgumentIsOfTypeDto(targetType, projectedClass)) {
                return Collections.emptyList();
            }

            return List.of("Projected class " + projectedClass.getName() + " is not assignable to element type " + targetClass.getName() + " on " + element);

        }

        return List.of("Projected class " + projectedClass.getName() + " is not assignable to element type " + targetClass.getName() + " on " + element);

    }

    private Class<?> getRawType(java.lang.reflect.Type type) {
        if (type instanceof Class<?> clazz) {
            return clazz;
        } else if (type instanceof java.lang.reflect.ParameterizedType pt) {
            return getRawType(pt.getRawType());
        }
        return null;
    }

    private boolean isParameterizedType(java.lang.reflect.Type type) {
        return type instanceof java.lang.reflect.ParameterizedType;
    }

    private boolean validateAnyGenericTypeArgumentIsOfTypeDto(ParameterizedType parameterizedType, Class<? extends Dto> projectedClass) {


        for (java.lang.reflect.Type actualTypeArgument : parameterizedType.getActualTypeArguments()) {

            Class<?> rawType = getRawType(actualTypeArgument);

            if (rawType == null) {
                continue;
            }

            if (projectedClass.isAssignableFrom(rawType)) {
                return true;
            }

            if (!isParameterizedType(actualTypeArgument)) {
                continue;
            }

            if (validateAnyGenericTypeArgumentIsOfTypeDto((ParameterizedType) actualTypeArgument, projectedClass)) {
                return true;
            }

        }

        return false;
    }

    private boolean validateAnyGenericTypeArgumentIsOfTypeDto(java.lang.reflect.Type type, Class<? extends Dto> projectedClass) {

        if (!isParameterizedType(type)) {
            return true;
        }

        return validateAnyGenericTypeArgumentIsOfTypeDto((ParameterizedType) type, projectedClass);

    }

    public String effectiveTypeAlias(DtoProjection<Dto<?>> projection) {
        return projection.projectionTypeAlias() != null ? projection.projectionTypeAlias() : projection.getClass().getSimpleName();
    }

    public String fullyQualifiedProjectionTypeName(String namespace, DtoProjection<Dto<?>> projection) {
        return namespace + "_" + effectiveTypeAlias(projection);
    }

    public List<String> validateNamespaceCollisionsValidationResults(ProjectionValidationResult target, ProjectionValidationResult source) {

        List<String> errors = new ArrayList<>();

        for (String fullyQualifiedProjectionName : source.projections.keySet()) {
            if (target.hasDifferentProjectionForSameNamespaceAndAlias(fullyQualifiedProjectionName, source.projections.get(fullyQualifiedProjectionName))) {
                errors.add("Duplicate projection namespace " + fullyQualifiedProjectionName + " in " + source.projections.get(fullyQualifiedProjectionName).getClass().getName() + " and " + target.projections.get(fullyQualifiedProjectionName).getClass().getName());
            }
        }

        return errors;
    }

    public void mergeValidationResults(ProjectionValidationResult target, ProjectionValidationResult source) {
        target.errors.addAll(source.errors);
        target.errors.addAll(validateNamespaceCollisionsValidationResults(target, source));
        target.projections.putAll(source.projections);
    }


    public static class ProjectionValidationResult {

        public List<String> errors;
        public Map<String, DtoProjection<Dto<?>>> projections;

        public ProjectionValidationResult() {
            this(new ArrayList<>(), new HashMap<>());
        }

        public ProjectionValidationResult(List<String> errors, Map<String, DtoProjection<Dto<?>>> projections) {
            this.errors = errors;
            this.projections = projections;
        }

        public boolean hasDifferentProjectionForSameNamespaceAndAlias(String fullyQualifiedProjectionName, DtoProjection<Dto<?>> projection) {
            return projections.containsKey(fullyQualifiedProjectionName)
                    && !projections.get(fullyQualifiedProjectionName).equals(projection);
        }

    }

}
