package solutions.sulfura.hyperkit.dsl.projections.validator;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.dtos.Dto;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

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
    public List<String> validateAll(String... basePackages) {
        List<String> errors = new ArrayList<>();
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(basePackages)
                .scan()) {
            for (ClassInfo classInfo : scanResult.getAllClasses()) {
                errors.addAll(validate(classInfo.loadClass()));
            }
        }
        return errors;
    }

    /**
     * Validates all {@link DtoProjectionSpec} annotations on the given classes.
     *
     * @param classes the classes to validate
     * @return a list of validation error messages, or an empty list if all are valid
     */
    public List<String> validate(Class<?>... classes) {
        List<String> errors = new ArrayList<>();
        for (Class<?> clazz : classes) {
            errors.addAll(validate(clazz));
        }
        return errors;
    }

    /**
     * Validates all {@link DtoProjectionSpec} annotations on the given class, including those on methods and parameters.
     * Also validates custom annotations that are annotated with {@link DtoProjectionSpec}.
     *
     * @param clazz the class to validate
     * @return a list of validation error messages, or an empty list if valid
     */
    public List<String> validate(Class<?> clazz) {
        List<String> errors = new ArrayList<>();

        // Validate methods
        for (Method method : clazz.getDeclaredMethods()) {
            errors.addAll(validateElement(method));

            // Validate parameters
            for (Parameter parameter : method.getParameters()) {
                errors.addAll(validateElement(parameter));
            }
        }

        return errors;
    }

    private List<String> validateElement(AnnotatedElement element) {

        DtoProjectionSpec spec = solutions.sulfura.hyperkit.dsl.projections.ProjectionUtils.findDtoProjectionSpec(element);

        if (spec == null) {
            return Collections.emptyList();
        }

        return validateProjection(spec, element);

    }

    private List<String> validateProjection(DtoProjectionSpec annotation, AnnotatedElement element) {

        try {

            Class<? extends Dto> projectedClass = annotation.projectedClass();
            ProjectionDsl.parse(annotation);
            return validateTypeAssignability(element, projectedClass);

        } catch (Exception e) {

            List<String> errors = new ArrayList<>();
            errors.add("Failed to parse projection on " + element + ": " + e.getMessage());
            return errors;

        }

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

    private boolean validateAnyGenericTypeArgumentIsOfTypeDto(ParameterizedType parameterizedType, Class<? extends Dto> projectedClass, HashSet<Type> visitedTypes) {


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

            if (validateAnyGenericTypeArgumentIsOfTypeDto((ParameterizedType) actualTypeArgument, projectedClass, visitedTypes)) {
                return true;
            }

        }

        return false;
    }

    private boolean validateAnyGenericTypeArgumentIsOfTypeDto(java.lang.reflect.Type type, Class<? extends Dto> projectedClass) {

        if (!isParameterizedType(type)) {
            return true;
        }

        return validateAnyGenericTypeArgumentIsOfTypeDto((ParameterizedType) type, projectedClass, new HashSet<>());

    }
}
