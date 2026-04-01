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

    public static RelationshipData getRelationshipData(Object relHolder1,
                                                       PropertyDescriptor helHolder1PropertyDescriptor,
                                                       Object relHolder2) {

        Object owningEntity = null;
        PropertyDescriptor owningPropertyDescriptor = null;

        Object nonOwningEntity = null;
        PropertyDescriptor nonOwningPropertyDescriptor = null;


        // Determine whether relHolder1 is the owning side of the relationship by looking at the annotations on the property.
        // If one of them is a mapping annotation (e.g., have a mappedBy attribute set), relHolder1 is the non-owning side.
        Annotation nonOwnerAnnotation = getNonOwnerAnnotation(helHolder1PropertyDescriptor);

        // Case: relHolder1 is NOT the owning side of the relationship.
        if (nonOwnerAnnotation != null) {

            nonOwningEntity = relHolder1;
            nonOwningPropertyDescriptor = helHolder1PropertyDescriptor;

            String mappedBy = findMappedBy(nonOwnerAnnotation);

            owningEntity = relHolder2;
            owningPropertyDescriptor = HyperMapperPropertyUtils.getPropertyDescriptorAtPropertyPath(relHolder2, mappedBy);

        }

        // Case: relHolder1 IS the owning side of the relationship.
        if (nonOwnerAnnotation == null) {

            // Search for a relationship owner annotation on relHolder1
            Annotation ownerAnnotation = getOwnerAnnotation(helHolder1PropertyDescriptor);

            // RelHolder1 IS the owning side of the relationship, look for a OneToMany or a OneToOne annotation on the properties of relHolder2 whose mappedBy field matches this property descriptor
            if (ownerAnnotation != null) {

                owningPropertyDescriptor = helHolder1PropertyDescriptor;
                owningEntity = relHolder1;
                final String owningPropertyName = owningPropertyDescriptor.getPropertyName();

                // Look for a mapping annotation on relHolder2 whose mappedBy field matches this property descriptor
                final PropertyDescriptor finalOwningPropertyDescriptor = owningPropertyDescriptor;
                nonOwningPropertyDescriptor = HyperMapperPropertyUtils.getProperties(relHolder2.getClass()).stream()
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

                            if (Objects.equals(mappedBy, owningPropertyName)) {
                                return true;
                            }

                            if (!mappedBy.contains(".") || !mappedBy.endsWith(owningPropertyName)) {
                                return false;
                            }

                            return Objects.equals(finalOwningPropertyDescriptor, HyperMapperPropertyUtils.getPropertyDescriptorAtPropertyPath(relHolder1, mappedBy));

                        }).findFirst()
                        .orElse(null);

                // RelHolder2 is part of a relationship but doesn't know it yet
                if (nonOwningPropertyDescriptor != null) {
                    nonOwningEntity = relHolder2;
                }

            }

        }

        return new RelationshipData(owningEntity, owningPropertyDescriptor, nonOwningEntity, nonOwningPropertyDescriptor);
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
    public static void removeRelationship(Object entity1, PropertyDescriptor propertyDescriptor, Object entity2) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        RelationshipData relationshipData = getRelationshipData(entity1, propertyDescriptor, entity2);

        // Nullify or remove references between the owning and non-owning entities from both sides of the relationship
        if (relationshipData.owningPropertyDescriptor != null) {

            // If owning side property is a collection (e.g., ManyToMany owner), remove the specific element
            if (Collection.class.isAssignableFrom(relationshipData.owningPropertyDescriptor.getPropertyType())) {
                removeFromCollectionProperty(relationshipData.owningEntity, relationshipData.owningPropertyDescriptor.getPropertyName(), entity2);
            } else {
                HyperMapperPropertyUtils.setProperty(relationshipData.owningEntity, relationshipData.owningPropertyDescriptor.getPropertyName(), null);
            }

        }

        if (relationshipData.nonOwningPropertyDescriptor != null) {

            //The non-owner property might be a collection, in that case remove the owner from the collection
            if (Collection.class.isAssignableFrom(relationshipData.nonOwningPropertyDescriptor.getPropertyType())) {
                removeFromCollectionProperty(relationshipData.nonOwningEntity, relationshipData.nonOwningPropertyDescriptor.getPropertyName(), relationshipData.owningEntity);
            } else {
                HyperMapperPropertyUtils.setProperty(relationshipData.nonOwningEntity, relationshipData.nonOwningPropertyDescriptor.getPropertyName(), null);
            }

        }


    }


    public static class RelationshipData {
        public Object owningEntity;
        public Object nonOwningEntity;
        public PropertyDescriptor owningPropertyDescriptor;
        public PropertyDescriptor nonOwningPropertyDescriptor;

        public RelationshipData() {
        }

        public RelationshipData(Object owningEntity, PropertyDescriptor owningPropertyDescriptor, Object nonOwningEntity, PropertyDescriptor nonOwningPropertyDescriptor) {
            this.owningEntity = owningEntity;
            this.nonOwningEntity = nonOwningEntity;
            this.owningPropertyDescriptor = owningPropertyDescriptor;
            this.nonOwningPropertyDescriptor = nonOwningPropertyDescriptor;
        }

    }

}
