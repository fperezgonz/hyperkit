package solutions.sulfura.hyperkit.utils.spring.hypermapper;

import jakarta.persistence.Id;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HyperMapperPropertyUtils {

    private static final ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, PropertyDescriptor>> descriptorMapCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, PropertyDescriptor> idsCache = new ConcurrentHashMap<>();
    private static final PropertyDescriptor NULL_MARKER = new PropertyDescriptor() {
    };

    public static <D> Object getProperty(D object, String propertyName) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        ConcurrentHashMap<String, PropertyDescriptor> descriptors = getPropertiesMap(object.getClass());
        return descriptors.get(propertyName).getValue(object);
    }

    public static <D> void setProperty(D object, String propertyName, Object value) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        ConcurrentHashMap<String, PropertyDescriptor> descriptors = getPropertiesMap(object.getClass());
        descriptors.get(propertyName).setValue(object, value);
    }

    private static Set<String> collectPropertyNames(Class<?> type) {

        Set<String> propertyNames = new HashSet<>();

        //Collect all public fields except those inherited from object
        for (Field field : type.getFields()) {
            if (field.getDeclaringClass() == Object.class) {
                continue;
            }
            propertyNames.add(field.getName());
        }

        //Collect all getters and Setters except those inherited from object
        for (Method method : type.getMethods()) {

            if (method.getDeclaringClass() == Object.class) {
                continue;
            }

            String propertyName = getPropertyName(method);

            if (propertyName != null) {
                propertyNames.add(propertyName);
            }

        }

        return propertyNames;

    }

    private static String getPropertyName(Method method) {

        if (method.getParameterCount() > 1) {
            return null;
        }

        String methodName = method.getName();

        if (methodName.length() > 3
                && Character.isUpperCase(methodName.charAt(3))
                && (methodName.startsWith("get") || methodName.startsWith("set"))) {

            return unCapitalize(methodName.substring(3));

        }

        if (methodName.startsWith("is") && methodName.length() > 2 && (Character.isUpperCase(methodName.charAt(2)))) {
            return unCapitalize(methodName.substring(2));
        }

        return null;

    }

    public static ConcurrentHashMap<String, PropertyDescriptor> getPropertiesMap(Class<?> type) {

        ConcurrentHashMap<String, PropertyDescriptor> result = descriptorMapCache.get(type);

        if (result != null) {
            return result;
        }

        result = new ConcurrentHashMap<>();

        var propertyNames = collectPropertyNames(type);

        for (String propertyName : propertyNames) {
            result.put(propertyName, new PropertyDescriptor(type, propertyName));
        }

        descriptorMapCache.put(type, result);

        return result;

    }

    public static Collection<PropertyDescriptor> getProperties(Class<?> type) {

        var propertiesMap = getPropertiesMap(type);
        return propertiesMap.values();

    }

    public static PropertyDescriptor getPropertyDescriptor(Class<?> type, String propertyName) {
        return getPropertiesMap(type).get(propertyName);
    }

    public static PropertyDescriptor getPropertyDescriptor(Object instance, String propertyName) {
        return getPropertyDescriptor(instance.getClass(), propertyName);
    }

    public static PropertyDescriptor getIdPropertyDescriptor(Class<?> entityClass) {

        boolean cacheHit = idsCache.containsKey(entityClass);

        if (cacheHit) {
            return idsCache.get(entityClass);
        }


        Collection<PropertyDescriptor> entityProperties = getProperties(entityClass);

        for (PropertyDescriptor propDescriptor : entityProperties) {

            if (propDescriptor.getAnnotation(Id.class) != null) {
                idsCache.put(entityClass, propDescriptor);
                return propDescriptor;
            }

        }

        idsCache.put(entityClass, NULL_MARKER);

        return null;

    }

    private static Class<?> findContainedType(Type type) {

        if (type instanceof Class<?> clazz) {
            return clazz;
        }

        if (type instanceof TypeVariable<?>) {
            //It is not possible to know the type that will be used to create instances automatically
            throw new RuntimeException("Type variables are not supported");
        }

        if (type instanceof GenericArrayType genericArrayType) {
            return genericArrayType.getGenericComponentType().getClass();
        }

        if (type instanceof ParameterizedType parameterizedType) {
            var typeArgs = parameterizedType.getActualTypeArguments();
            if (typeArgs.length == 1) {
                var nestedType = typeArgs[0];
                return findContainedType(nestedType);
            }
            //It is not possible to know the type that will be used to create instances automatically
            throw new RuntimeException("Parameterized types with more than one type argument are not supported");
        }

        return null;

    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static String unCapitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    private static Method findGetter(Class<?> type, String propertyName) {
        try {

            return type.getMethod("get" + capitalize(propertyName));

        } catch (NoSuchMethodException ignore) {
        }

        try {
            Method result = type.getMethod("is" + capitalize(propertyName));

            var returnType = result.getReturnType();

            if (returnType == boolean.class || returnType == Boolean.class) {
                return result;
            }

        } catch (NoSuchMethodException ignore) {
        }

        return null;

    }

    private static Method findSetter(Class<?> type, String propertyName) {

        Method result = Arrays.stream(type.getMethods())
                .filter(m -> m.getName().equals("set" + capitalize(propertyName)))
                .filter(m -> m.getParameterCount() == 1)
                .filter(m -> m.getReturnType() == void.class)
                .findFirst()
                .orElse(null);

        return result;

    }

    public static class PropertyDescriptor {

        private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];

        private final Class<?> declaringType;
        private final String propertyName;
        public final Method getter;
        public final Method setter;
        private final Field field;
        private final Class<?> fieldType;
        //If the type is a "container" type, the type of the value held in the container, in other case it is the same as the type of the field
        private final Class<?> containedType;
        // Cache to store annotations of specific types for a property, combining annotations from the field, getter, and setter.
        private ConcurrentHashMap<Class<? extends Annotation>, Annotation[]> annotationsByType;


        private PropertyDescriptor() {
            declaringType = null;
            propertyName = null;
            getter = null;
            setter = null;
            field = null;
            fieldType = null;
            containedType = null;
            annotationsByType = null;
        }

        private PropertyDescriptor(Class<?> declaringType, String propertyName) {

            this.declaringType = declaringType;
            this.propertyName = propertyName;
            this.getter = findGetter(declaringType, propertyName);
            this.setter = findSetter(declaringType, propertyName);

            Field field = null;

            try {
                field = declaringType.getField(propertyName);
            } catch (NoSuchFieldException ignore) {
            }

            if (field == null) {
                this.field = null;
                this.fieldType = null;
                this.containedType = null;
            } else {
                this.field = field;
                this.fieldType = field.getType();
                try {
                    this.containedType = findContainedType(field.getGenericType());
                } catch (RuntimeException e) {
                    throw new RuntimeException("Could not determine the contained type for field " + field.getName() + " of type " + declaringType.getName(), e);
                }
            }

        }

        public Field getField() {
            return field;
        }

        public Class<?> getFieldType() {
            return fieldType;
        }

        public Class<?> getContainedType() {
            return containedType;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public boolean canRead() {
            return getter != null || field != null;
        }

        public boolean canWrite() {
            return setter != null || field != null;
        }

        public Object getValue(Object bean) {

            if (!canRead()) {
                throw new RuntimeException("Property " + propertyName + " in object of type" + bean.getClass() + " is not readable");
            }

            try {

                if (getter != null) {
                    return getter.invoke(bean);
                }

                return field.get(bean);

            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        }

        public <T extends Annotation> T getAnnotation(Class<T> annotationType) {

            if (this.annotationsByType == null) {
                this.annotationsByType = new ConcurrentHashMap<>();
            }

            //Use the cache if possible
            if (this.annotationsByType.containsKey(annotationType)) {

                var annotationsForType = this.annotationsByType.get(annotationType);

                if (annotationsForType.length == 0) {
                    return null;
                }

                //noinspection unchecked
                return (T) annotationsForType[0];

            }

            //Find the annotations on the field, the getter and the setter
            Annotation[] fieldAnnotationsForType = null;
            if (field != null) {
                fieldAnnotationsForType = field.getAnnotationsByType(annotationType);
            }

            Annotation[] getterAnnotationsForType = null;
            if (getter != null) {
                getterAnnotationsForType = getter.getAnnotationsByType(annotationType);
            }

            Annotation[] setterAnnotationsForType = null;
            if (setter != null) {
                setterAnnotationsForType = setter.getAnnotationsByType(annotationType);
            }

            //Calculate annotations count
            int annotationsCount = 0;
            annotationsCount += fieldAnnotationsForType != null ? fieldAnnotationsForType.length : 0;
            annotationsCount += getterAnnotationsForType != null ? getterAnnotationsForType.length : 0;
            annotationsCount += setterAnnotationsForType != null ? setterAnnotationsForType.length : 0;

            //If there are no annotations, add a null result to the cache and return it
            if (annotationsCount == 0) {
                this.annotationsByType.put(annotationType, EMPTY_ANNOTATION_ARRAY);
                return null;
            }

            //Build the annotations array and populate it
            var result = new Annotation[annotationsCount];
            int index = 0;

            if (fieldAnnotationsForType != null) {
                System.arraycopy(fieldAnnotationsForType, 0, result, index, fieldAnnotationsForType.length);
                index += fieldAnnotationsForType.length;
            }

            if (getterAnnotationsForType != null) {
                System.arraycopy(getterAnnotationsForType, 0, result, index, getterAnnotationsForType.length);
                index += getterAnnotationsForType.length;
            }

            if (setterAnnotationsForType != null) {
                System.arraycopy(setterAnnotationsForType, 0, result, index, setterAnnotationsForType.length);
            }

            //Add the array to the cache and return the annotation
            this.annotationsByType.put(annotationType, result);

            return getAnnotation(annotationType);

        }

        public void setValue(Object bean, Object value) {

            if (!canWrite()) {
                throw new RuntimeException("Property " + propertyName + " in object of type " + bean.getClass() + " is not writable");
            }

            try {

                if (setter != null) {
                    setter.invoke(bean, value);
                    return;
                }

                field.set(bean, value);

            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        }

    }
}