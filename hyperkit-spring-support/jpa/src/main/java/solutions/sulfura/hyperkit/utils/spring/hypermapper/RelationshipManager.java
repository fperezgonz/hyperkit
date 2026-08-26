package solutions.sulfura.hyperkit.utils.spring.hypermapper;

import jakarta.persistence.*;
import org.hibernate.Hibernate;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.HyperMapperPropertyUtils.PropertyDescriptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static solutions.sulfura.hyperkit.utils.spring.hypermapper.HyperMapperPropertyUtils.getIdPropertyDescriptor;

public class RelationshipManager {

    public static Collection<Object> collectionInstanceForType(Class<?> collectionType) {

        if (collectionType == List.class) {

            return new ArrayList<>();

        } else if (collectionType == Set.class) {

            return new HashSet<>();

        } else {

            try {

                //noinspection unchecked
                return (Collection<Object>) collectionType.getConstructor().newInstance();

            } catch (InstantiationException | NoSuchMethodException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new RuntimeException("Automatic instantiation of collection type " + collectionType.getCanonicalName() +
                        " not supported. Only collections with public no-args constructors are supported", e);
            }

        }

    }

    public static void addToCollectionProperty(Object parentEntity, String propertyName, Object childEntity) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        var propertyDescriptor = HyperMapperPropertyUtils.getPropertyDescriptor(parentEntity, propertyName);

        @SuppressWarnings("unchecked")
        Collection<Object> collection = ((Collection<Object>) propertyDescriptor.getValue(parentEntity));

        //Initialize the collection property if necessary
        if (collection == null) {

            collection = collectionInstanceForType(propertyDescriptor.getPropertyType());

            HyperMapperPropertyUtils.setProperty(parentEntity, propertyName, collection);

        }

        //Add the element to the collection
        if (Hibernate.isInitialized(collection)) {
            collection.add(childEntity);
        }

    }

    public static void removeFromCollectionProperty(Object parentEntity, String propertyName, Object childEntity) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        @SuppressWarnings("unchecked")
        Collection<Object> collection = ((Collection<Object>) HyperMapperPropertyUtils.getProperty(parentEntity, propertyName));
        if (Hibernate.isInitialized(collection)) {
            collection.remove(childEntity);
        }

    }

    /**
     * Checks if a collection property of an entity contains a specific item based on the item's ID.
     *
     * @param entity                 The parent entity whose collection property will be checked.
     * @param collectionPropertyName The name of the property representing a collection of related entities.
     * @param itemId                 The ID of the item to search for within the collection.
     * @return True if an element in the collection matches the itemId; False otherwise.
     * @throws InvocationTargetException If there is an error during reflective method invocation.
     * @throws IllegalAccessException    If the property is not accessible.
     * @throws NoSuchMethodException     If the getter method for the property is missing.
     */
    public static boolean toManyContains(Object entity, String collectionPropertyName, Object itemId) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        @SuppressWarnings("unchecked")
        Collection<Object> collection = ((Collection<Object>) HyperMapperPropertyUtils.getProperty(entity, collectionPropertyName));
        PropertyDescriptor entityIdPropDesc = null;

        if (collection == null) {
            return false;
        }

        for (Object collectionElement : collection) {

            entityIdPropDesc = entityIdPropDesc != null ? entityIdPropDesc : getIdPropertyDescriptor(collectionElement.getClass());

            if (entityIdPropDesc == null) {
                throw new RuntimeException("Entity types without an @Id property are not supported. Type: " + collectionElement.getClass().getName());
            }

            if (Objects.equals(HyperMapperPropertyUtils.getProperty(collectionElement, entityIdPropDesc.getPropertyName()), itemId)) {
                return true;
            }

        }

        // Return false if no matching element was found in the collection.
        return false;

    }

    public static Annotation getNonOwnerAnnotation(PropertyDescriptor entityPropertyDescriptor) {

        var oneToManyAnnotation = entityPropertyDescriptor.getAnnotation(OneToMany.class);

        if (oneToManyAnnotation != null) {
            return oneToManyAnnotation;
        }

        var oneToOneAnnotation = entityPropertyDescriptor.getAnnotation(OneToOne.class);
        if (oneToOneAnnotation != null) {

            var mappedBy = oneToOneAnnotation.mappedBy();

            if (mappedBy != null && !mappedBy.isEmpty()) {
                return oneToOneAnnotation;
            }

        }

        // ManyToMany non-owning side (mappedBy set)
        var manyToManyAnnotation = entityPropertyDescriptor.getAnnotation(ManyToMany.class);
        if (manyToManyAnnotation != null) {
            if (manyToManyAnnotation.mappedBy() != null && !manyToManyAnnotation.mappedBy().isEmpty()) {
                return manyToManyAnnotation;
            }
        }

        return null;

    }

    public static Annotation getOwnerAnnotation(PropertyDescriptor entity1PropertyDescriptor) {

        var manyToOneAnnotation = entity1PropertyDescriptor.getAnnotation(ManyToOne.class);

        if (manyToOneAnnotation != null) {
            return manyToOneAnnotation;
        }

        var oneToOneAnnotation = entity1PropertyDescriptor.getAnnotation(OneToOne.class);

        if (oneToOneAnnotation != null) {

            var mappedBy = oneToOneAnnotation.mappedBy();

            if (mappedBy == null || mappedBy.isEmpty()) {
                return oneToOneAnnotation;
            }

        }

        // ManyToMany owning side (JoinTable present on field)
        var manyToManyAnnotation = entity1PropertyDescriptor.getAnnotation(ManyToMany.class);
        if (manyToManyAnnotation != null) {
            // If mappedBy is empty, this side owns the relationship
            if (manyToManyAnnotation.mappedBy() == null || manyToManyAnnotation.mappedBy().isEmpty()) {
                return manyToManyAnnotation;
            }
        }

        return null;

    }

    public static boolean isRelationshipOwner(Object entity, String fieldName) {

        PropertyDescriptor propertyDescriptor = HyperMapperPropertyUtils.getPropertyDescriptor(entity, fieldName);

        // Owning side if it has a Column/JoinColumn/JoinTable annotation
        return propertyDescriptor.getAnnotation(Column.class) != null
                || propertyDescriptor.getAnnotation(JoinColumn.class) != null
                || propertyDescriptor.getAnnotation(JoinTable.class) != null;

    }

    private static String findMappedBy(Annotation auxMappingAnnotation) {
        String mappedBy;

        if (auxMappingAnnotation instanceof OneToMany) {
            mappedBy = ((OneToMany) auxMappingAnnotation).mappedBy();
        } else if (auxMappingAnnotation instanceof OneToOne) {
            mappedBy = ((OneToOne) auxMappingAnnotation).mappedBy();
        } else {
            mappedBy = ((ManyToMany) auxMappingAnnotation).mappedBy();
        }
        return mappedBy;
    }

    private static String findMappedBy(PropertyDescriptor entity1PropertyDescriptor) {
        Annotation nonOwnerAnnotation = getNonOwnerAnnotation(entity1PropertyDescriptor);
        if (nonOwnerAnnotation == null) {
            return null;
        }
        return findMappedBy(nonOwnerAnnotation);
    }

    private static String findNonOwningPropertyPathByOwningPropertyPath(String owningPropertyPath, Class<?> nonOwningEntityClass) {

        for (PropertyDescriptor propertyDescriptor : HyperMapperPropertyUtils.getProperties(nonOwningEntityClass)) {

            if (propertyDescriptor.getAnnotation(Embedded.class) != null) {

                String relativePropertyPath = findNonOwningPropertyPathByOwningPropertyPath(owningPropertyPath, propertyDescriptor.getPropertyType());

                if (relativePropertyPath != null) {
                    return propertyDescriptor.getPropertyName() + "." + relativePropertyPath;
                }

            } else {

                String mappedBy = findMappedBy(propertyDescriptor);

                if (mappedBy != null && mappedBy.equals(owningPropertyPath)) {
                    return propertyDescriptor.getPropertyName();
                }

            }
        }

        return null;

    }


    /**
     * Analyzes and determines the relationship structure between two relationship holders by examining JPA mapping annotations.
     * This method identifies which holder is the owner and which is the non-owner of the relationship,
     * along with their respective property descriptors.
     *
     * @param entityClass                  The class of the entity that propertyPath1 is calculated from
     * @param propertyPath1                The relationship property path from the {@code entityClass} to the {@code relHolder1PropertyDescriptor}
     * @param helHolder1PropertyDescriptor The property descriptor for the relationship property on relHolder1.
     * @param relHolder2                   The entity related to entityClass through the property in relHolder1.
     * @return A RelationshipData object containing the owning entity, owning property descriptor,
     * non-owning entity, and non-owning property descriptor. Some fields may be null if
     * the relationship is unidirectional or if one side doesn't maintain a reference.
     */
    public static RelationshipData<Class<?>, Class<?>> getRelationshipData(Class<?> entityClass,
                                                                           String propertyPath1,
                                                                           PropertyDescriptor helHolder1PropertyDescriptor,
                                                                           Class<?> relHolder2) {

        Class<?> owningEntityClass = null;
        Class<?> nonOwningEntityClass = null;

        // Determine whether relHolder1 is the owning side of the relationship by looking at the annotations on the property.
        // If one of them is a mapping annotation (e.g., have a mappedBy attribute set), relHolder1 is the non-owning side.
        Annotation nonOwnerAnnotation = getNonOwnerAnnotation(helHolder1PropertyDescriptor);

        // Case 1: relHolder1 IS NOT the owning side of the relationship.
        if (nonOwnerAnnotation != null) {

            nonOwningEntityClass = entityClass;
            String mappedBy = findMappedBy(nonOwnerAnnotation);
            owningEntityClass = relHolder2;

            return new RelationshipData<>(relHolder2, mappedBy, nonOwningEntityClass, propertyPath1);

        }

        // Case 2: relHolder1 IS the owning side of the relationship.
        // Search for a relationship owner annotation on relHolder1
        Annotation ownerAnnotation = getOwnerAnnotation(helHolder1PropertyDescriptor);

        if (ownerAnnotation == null) {
            throw new IllegalArgumentException("No relationship annotation found for property descriptor: " + helHolder1PropertyDescriptor);
        }

        // RelHolder1 IS the owning side of the relationship, look for a OneToMany or a OneToOne annotation on the properties of relHolder2 whose mappedBy field matches this property descriptor
        owningEntityClass = entityClass;
        nonOwningEntityClass = relHolder2;
        String nonOwningPropertyPath = findNonOwningPropertyPathByOwningPropertyPath(propertyPath1, relHolder2);

        return new RelationshipData<>(owningEntityClass, propertyPath1, nonOwningEntityClass, nonOwningPropertyPath);
    }

    /**
     * Breaks the relationship between two entities by identifying and nullifying references on both sides of the relationship (also removing it from the collection if it is a ManyToOne relationship).
     * This method considers whether the entities are on the owning or non-owning side of the relationship.
     *
     * @param entity1            The first entity involved in the relationship.
     * @param propertyDescriptor The property descriptor for the field that holds entity2. This is a field on entity1 or an embedded value object on entity1.
     * @param entity2            The second entity involved in the relationship.
     * @throws InvocationTargetException If methods invoked via reflection throw exceptions.
     * @throws IllegalAccessException    If the property cannot be accessed.
     * @throws NoSuchMethodException     If required methods are not found.
     */
    public static void removeRelationship(@NonNull Object entity1, @NonNull String propertyPath1, @NonNull PropertyDescriptor propertyDescriptor, @NonNull Object entity2) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RelationshipData<?, ?> relationshipData = getRelationshipData(entity1.getClass(), propertyPath1, propertyDescriptor, entity2.getClass());

        Object owner;
        PropertyDescriptor ownerPropertyDescriptor;
        Object nonOwner;
        PropertyDescriptor nonOwnerPropertyDescriptor;

        if (relationshipData.isRelationshipOwner(entity1, propertyPath1)) {
            owner = entity1;
            ownerPropertyDescriptor = propertyDescriptor;
            nonOwner = entity2;
            nonOwnerPropertyDescriptor = HyperMapperPropertyUtils.getPropertyDescriptorAtPropertyPath(relationshipData.nonOwningEntityClass, relationshipData.nonOwningPropertyPath);
        } else {
            owner = entity2;
            ownerPropertyDescriptor = HyperMapperPropertyUtils.getPropertyDescriptorAtPropertyPath(relationshipData.owningEntityClass, relationshipData.owningPropertyPath);
            nonOwner = entity1;
            nonOwnerPropertyDescriptor = propertyDescriptor;
        }

        removeRelationship(owner, ownerPropertyDescriptor, nonOwner, nonOwnerPropertyDescriptor);

    }

    public static void removeRelationship(@NonNull Object owner, @NonNull PropertyDescriptor ownerPropertyDescriptor, @NonNull Object nonOwner, @Nullable PropertyDescriptor nonOwnerPropertyDescriptor) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        // Nullify or remove references between the owning and non-owning entities from both sides of the relationship

        // If owning side property is a collection (e.g., ManyToMany owner), remove the specific element
        if (Collection.class.isAssignableFrom(ownerPropertyDescriptor.getPropertyType())) {
            removeFromCollectionProperty(owner, ownerPropertyDescriptor.getPropertyName(), nonOwner);
        } else {
            HyperMapperPropertyUtils.setProperty(owner, ownerPropertyDescriptor.getPropertyName(), null);
        }

        if (nonOwnerPropertyDescriptor != null) {

            //The non-owner property might be a collection, in that case remove the owner from the collection
            if (Collection.class.isAssignableFrom(nonOwnerPropertyDescriptor.getPropertyType())) {
                removeFromCollectionProperty(nonOwner, nonOwnerPropertyDescriptor.getPropertyName(), owner);
            } else {
                HyperMapperPropertyUtils.setProperty(nonOwner, nonOwnerPropertyDescriptor.getPropertyName(), null);
            }

        }


    }


    /**
     * @param owningEntityClass     The class of the entity that owns the relationship.
     * @param owningPropertyPath    The property path for the property that owns the relationship, starting from the owning entity.
     * @param nonOwningEntityClass  The class of the entity that does not own the relationship.
     * @param nonOwningPropertyPath The property path for the property on the non-owning side of the relationship, starting from the non-owning entity.
     */
    public record RelationshipData<O extends Class<?>, NO extends Class<?>>(O owningEntityClass,
                                                                            String owningPropertyPath,
                                                                            NO nonOwningEntityClass,
                                                                            String nonOwningPropertyPath) {

        public boolean isRelationshipOwner(@NonNull Object entityInstance, @NonNull String propertyPath) {
            return owningEntityClass.isAssignableFrom(entityInstance.getClass()) && propertyPath.equals(owningPropertyPath);
        }

    }

}
