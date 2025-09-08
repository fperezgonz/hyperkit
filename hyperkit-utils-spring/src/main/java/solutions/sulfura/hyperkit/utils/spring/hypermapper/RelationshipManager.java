package solutions.sulfura.hyperkit.utils.spring.hypermapper;

import jakarta.persistence.*;
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
        collection.add(childEntity);

    }

    public static void removeFromCollectionProperty(Object parentEntity, String propertyName, Object childEntity) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        @SuppressWarnings("unchecked")
        Collection<Object> collection = ((Collection<Object>) HyperMapperPropertyUtils.getProperty(parentEntity, propertyName));
        collection.remove(childEntity);

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
    public static boolean oneToManyContains(Object entity, String collectionPropertyName, Object itemId) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        @SuppressWarnings("unchecked")
        Collection<Object> collection = ((Collection<Object>) HyperMapperPropertyUtils.getProperty(entity, collectionPropertyName));
        PropertyDescriptor entityIdPropDesc = null;

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

        return null;

    }

    public static boolean isRelationshipOwner(Object entity, String fieldName) {

        PropertyDescriptor propertyDescriptor = HyperMapperPropertyUtils.getPropertyDescriptor(entity, fieldName);

        return propertyDescriptor.getAnnotation(Column.class) != null
                || propertyDescriptor.getAnnotation(JoinColumn.class) != null;

    }

    /**
     * Breaks the relationship between two entities by identifying and nullifying references on both sides of the relationship (also removing it from the collection if it is a ManyToOne relationship).
     * This method considers whether the entities are on the owning or non-owning side of the relationship.
     *
     * @param entity1                   The first entity involved in the relationship.
     * @param entity1PropertyDescriptor The property descriptor for the relationship field on the first entity.
     * @param entity2                   The second entity involved in the relationship.
     * @throws InvocationTargetException If methods invoked via reflection throw exceptions.
     * @throws IllegalAccessException    If the property cannot be accessed.
     * @throws NoSuchMethodException     If required methods are not found.
     */
    public static void removeRelationship(Object entity1, PropertyDescriptor entity1PropertyDescriptor, Object entity2) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Object owningEntity = null;
        PropertyDescriptor owningPropertyDescriptor = null;

        Object nonOwningEntity = null;
        PropertyDescriptor nonOwningPropertyDescriptor = null;


        // Determine whether entity1 is the owning side of the relationship by looking at the annotations on the property.
        // If one of them is a mapping annotations (e.g., have a mappedBy attribute set), entity1 is the non-owning side.
        Annotation nonOwnerAnnotation = getNonOwnerAnnotation(entity1PropertyDescriptor);

        // Case: entity1 is NOT the owning side of the relationship.
        if (nonOwnerAnnotation != null) {

            nonOwningEntity = entity1;
            nonOwningPropertyDescriptor = entity1PropertyDescriptor;

            String mappedBy = nonOwnerAnnotation instanceof OneToMany ?
                    ((OneToMany) nonOwnerAnnotation).mappedBy()
                    : ((OneToOne) nonOwnerAnnotation).mappedBy();

            owningEntity = entity2;
            owningPropertyDescriptor = HyperMapperPropertyUtils.getPropertyDescriptor(entity2, mappedBy);

        }

        // Case: entity1 IS the owning side of the relationship.
        if (nonOwnerAnnotation == null) {

            //Search for a relationship owner annotation on entity1
            Annotation ownerAnnotation = getOwnerAnnotation(entity1PropertyDescriptor);

            //Entity1 IS the owning side of the relationship, look for a OneToMany or a OneToOne annotation on the properties of entity2 whose mappedBy field matches this property descriptor
            if (ownerAnnotation != null) {

                owningPropertyDescriptor = entity1PropertyDescriptor;
                owningEntity = entity1;

                //Look for a OneToMany or a OneToOne annotation on the properties of entity2 whose mappedBy field matches this property descriptor
                Class<? extends Annotation> mappingAnnotationType = ownerAnnotation instanceof ManyToOne ? OneToMany.class : OneToOne.class;

                nonOwningPropertyDescriptor = HyperMapperPropertyUtils.getProperties(entity2.getClass()).stream()
                        .filter(propDesc -> {

                            Annotation auxMappingAnnotation = propDesc.getAnnotation(mappingAnnotationType);

                            if (auxMappingAnnotation == null) {
                                return false;
                            }

                            String mappedBy = auxMappingAnnotation instanceof OneToMany ?
                                    ((OneToMany) auxMappingAnnotation).mappedBy()
                                    : ((OneToOne) auxMappingAnnotation).mappedBy();

                            return Objects.equals(propDesc.getPropertyName(), mappedBy);

                        }).findFirst()
                        .orElse(null);

                //Entity2 is part of a relationship but doesn't know it yet
                if (nonOwningPropertyDescriptor != null) {
                    nonOwningEntity = entity2;
                }

            }

        }

        // Nullify or remove references between the owning and non-owning entities from both sides of the relationship.

        if (owningPropertyDescriptor != null) {

            HyperMapperPropertyUtils.setProperty(owningEntity, owningPropertyDescriptor.getPropertyName(), null);

        }

        if (nonOwningPropertyDescriptor != null) {

            //The non-owner property might be a collection, in that case remove the owner from the collection
            if (Collection.class.isAssignableFrom(nonOwningPropertyDescriptor.getPropertyType())) {
                removeFromCollectionProperty(nonOwningEntity, nonOwningPropertyDescriptor.getPropertyName(), owningEntity);
            } else {
                HyperMapperPropertyUtils.setProperty(nonOwningEntity, nonOwningPropertyDescriptor.getPropertyName(), null);
            }

        }


    }
}
