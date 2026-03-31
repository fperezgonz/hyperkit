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

    public static RelationshipData getRelationshipData(Object entity1,
                                                       PropertyDescriptor entity1PropertyDescriptor,
                                                       Object entity2) {

        Object owningEntity = null;
        PropertyDescriptor owningPropertyDescriptor = null;

        Object nonOwningEntity = null;
        PropertyDescriptor nonOwningPropertyDescriptor = null;


        // Determine whether entity1 is the owning side of the relationship by looking at the annotations on the property.
        // If one of them is a mapping annotation (e.g., have a mappedBy attribute set), entity1 is the non-owning side.
        Annotation nonOwnerAnnotation = getNonOwnerAnnotation(entity1PropertyDescriptor);

        // Case: entity1 is NOT the owning side of the relationship.
        if (nonOwnerAnnotation != null) {

            nonOwningEntity = entity1;
            nonOwningPropertyDescriptor = entity1PropertyDescriptor;

            String mappedBy = findMappedBy(nonOwnerAnnotation);

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
                final String owningPropertyName = owningPropertyDescriptor.getPropertyName();

                //Look for a mapping annotation on entity2 whose mappedBy field matches this property descriptor
                nonOwningPropertyDescriptor = HyperMapperPropertyUtils.getProperties(entity2.getClass()).stream()
                        .filter(propDesc -> {

                            Annotation auxMappingAnnotation = propDesc.getAnnotation(OneToMany.class);

                            if (auxMappingAnnotation == null) {
                                auxMappingAnnotation = propDesc.getAnnotation(OneToOne.class);
                            }

                            if (auxMappingAnnotation == null) {
                                auxMappingAnnotation = propDesc.getAnnotation(ManyToMany.class);
                            }

                            if (auxMappingAnnotation == null) {
                                return false;
                            }

                            String mappedBy = findMappedBy(auxMappingAnnotation);

                            return Objects.equals(mappedBy, owningPropertyName);

                        }).findFirst()
                        .orElse(null);

                //Entity2 is part of a relationship but doesn't know it yet
                if (nonOwningPropertyDescriptor != null) {
                    nonOwningEntity = entity2;
                }

            }

        }

        return new RelationshipData(owningEntity, owningPropertyDescriptor, nonOwningEntity, nonOwningPropertyDescriptor);
    }

    /**
     * Breaks the relationship between two objects by identifying and nullifying references on both sides of the relationship (also removing it from the collection if it is a ManyToOne relationship).
     * This method considers whether the objects are on the owning or non-owning side of the relationship.
     *
     * @param obj1                   The first object involved in the relationship.
     * @param obj1PropertyDescriptor The property descriptor for the relationship field on the first object.
     * @param obj2                   The second object involved in the relationship.
     * @throws InvocationTargetException If methods invoked via reflection throw exceptions.
     * @throws IllegalAccessException    If the property cannot be accessed.
     * @throws NoSuchMethodException     If required methods are not found.
     */
    public static void removeRelationship(Object obj1, PropertyDescriptor obj1PropertyDescriptor, Object obj2) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RelationshipData relationshipData = getRelationshipData(obj1, obj1PropertyDescriptor, obj2);

        // Nullify or remove references between the owning and non-owning objects from both sides of the relationship
        if (relationshipData.owningPropertyDescriptor != null) {

            // If owning side property is a collection (e.g., ManyToMany owner), remove the specific element
            if (Collection.class.isAssignableFrom(relationshipData.owningPropertyDescriptor.getPropertyType())) {
                removeFromCollectionProperty(relationshipData.owningPropertyHolder, relationshipData.owningPropertyDescriptor.getPropertyName(), obj2);
            } else {
                HyperMapperPropertyUtils.setProperty(relationshipData.owningPropertyHolder, relationshipData.owningPropertyDescriptor.getPropertyName(), null);
            }

        }

        if (relationshipData.nonOwningPropertyDescriptor != null) {

            //The non-owner property might be a collection, in that case remove the owner from the collection
            if (Collection.class.isAssignableFrom(relationshipData.nonOwningPropertyDescriptor.getPropertyType())) {
                removeFromCollectionProperty(relationshipData.nonOwningPropertyHolder, relationshipData.nonOwningPropertyDescriptor.getPropertyName(), relationshipData.owningPropertyHolder);
            } else {
                HyperMapperPropertyUtils.setProperty(relationshipData.nonOwningPropertyHolder, relationshipData.nonOwningPropertyDescriptor.getPropertyName(), null);
            }

        }


    }


    public static class RelationshipData {
        public Object owningPropertyHolder;
        public Object nonOwningPropertyHolder;
        public PropertyDescriptor owningPropertyDescriptor;
        public PropertyDescriptor nonOwningPropertyDescriptor;

        public RelationshipData() {
        }

        public RelationshipData(Object owningPropertyHolder, PropertyDescriptor owningPropertyDescriptor, Object nonOwningPropertyHolder, PropertyDescriptor nonOwningPropertyDescriptor) {
            this.owningPropertyHolder = owningPropertyHolder;
            this.nonOwningPropertyHolder = nonOwningPropertyHolder;
            this.owningPropertyDescriptor = owningPropertyDescriptor;
            this.nonOwningPropertyDescriptor = nonOwningPropertyDescriptor;
        }

    }

}
