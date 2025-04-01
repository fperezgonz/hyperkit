package solutions.sulfura.hyperkit.utils.spring.hypermapper;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.ValueWrapperAdapter;
import solutions.sulfura.hyperkit.utils.spring.HyperRepository;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.HyperMapperPropertyUtils.PropertyDescriptor;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static solutions.sulfura.hyperkit.utils.spring.hypermapper.HyperMapperPropertyUtils.getIdPropertyDescriptor;
import static solutions.sulfura.hyperkit.utils.spring.hypermapper.RelationshipManager.*;

@SuppressWarnings({"unchecked", "rawtypes"})
@Service
public class HyperMapper<C> {

    private final HyperRepository<C> hyperRepository;
    private final ValueWrapperAdapter valueWrapperAdapter;


    public HyperMapper(final HyperRepository<C> hyperRepository, ValueWrapperAdapter valueWrapperAdapter) {
        this.hyperRepository = hyperRepository;
        this.valueWrapperAdapter = valueWrapperAdapter;
    }

    /**
     * Retrieves an entity from the repository based on its class and identifier.
     *
     * @param entityClass the class type of the entity to retrieve
     * @param entityId    the identifier of the entity to retrieve
     * @param contextInfo additional context information required for the repository operation
     * @param <T>         the type of the entity
     * @param <I>         the type of the identifier
     * @return the entity retrieved from the repository
     * @throws HyperMapperException if the entity is not found in the repository
     */
    @NonNull
    private <T, I extends Serializable> T findEntityInRepository(@NonNull Class<T> entityClass, @NonNull I entityId, C contextInfo) {
        return hyperRepository.findById(entityClass, entityId, contextInfo)
                .orElseThrow(() -> new HyperMapperException("Entity of type " + entityClass + " with id " + entityId + " not found in repository"));
    }

    private static boolean isEntity(Object object) {

        if (object == null) {
            return false;
        }

        return object.getClass().getAnnotation(Entity.class) != null;

    }

    @NonNull
    public <T> T persistDtoToEntity(@NonNull Dto<T> dto, C contextInfo) {

        ToEntityResult<T> toEntityResult = mapDtoToEntity(dto, contextInfo);

        for (int i = 0; i < toEntityResult.persistenceQueue.size(); i++) {
            hyperRepository.save(toEntityResult.persistenceQueue.get(i), contextInfo);
        }

        return toEntityResult.entity;

    }

    /**
     * Maps a property of an entity that holds a collection of {@link ListOperation}, including adding, removing, and updating entities,
     * while maintaining relationships
     *
     * @param entity             The parent entity whose collection property is being modified
     * @param propertyDescriptor The property descriptor for the property that is being mapped
     * @param collection         The collection with the {@link ListOperation} elements
     * @param contextInfo        Extra contextual information for repository access
     * @param visitedEntities    A hash set for tracking entities already processed
     * @return A list of ToEntityResult<?> objects representing updated or created entities
     */
    @NonNull
    private List<ToEntityResult<?>> mapListOperations(@NonNull Object entity,
                                                      @NonNull PropertyDescriptor propertyDescriptor,
                                                      @NonNull Collection<?> collection,
                                                      C contextInfo,
                                                      @NonNull HashSet<Object> visitedEntities) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        List<ToEntityResult<?>> result = new ArrayList<>();

        // If the collection is empty, return an empty result list as no processing is needed.
        if (collection.isEmpty()) {
            return result;
        }

        // Iterate over each item in the collection and perform corresponding operations.
        for (Object item : collection) {

            var itemEntityResult = new ToEntityResult<>();

            if (visitedEntities.contains(item)) {
                continue;
            }

            if (!(item instanceof ListOperation<?> listOperation)) {
                throw new HyperMapperException("Dto collections must not contain elements that are not of type ListOperation");
            }

            if (!(listOperation.getValue() instanceof Dto<?> value)) {
                throw new HyperMapperException("Only Dtos are supported collection elements on properties of Dtos of entities");
            }

            PropertyDescriptor listValueIdPropertyDescriptor = getIdPropertyDescriptor(value.getSourceClass());
            if (listValueIdPropertyDescriptor == null) {
                throw new HyperMapperException("Entity types without an @Id property are not supported. Type: " + value.getSourceClass().getName());
            }
            var itemIdWrapper = HyperMapperPropertyUtils.getProperty(value, listValueIdPropertyDescriptor.getPropertyName());
            Serializable itemId = (Serializable) valueWrapperAdapter.unwrap(itemIdWrapper);

            // Case INSERT; ensure the item does not have an ID.
            if (listOperation.getItemOperationType() == ListOperation.ItemOperationType.INSERT && itemId != null) {
                throw new HyperMapperException("Unable to insert with an INSERT list operation, the collection item is already on the repository");
            }

            // Case UPDATE; ensure the item has an ID and is currently present in the collection
            if (listOperation.getItemOperationType() == ListOperation.ItemOperationType.UPDATE) {

                if (itemId == null) {
                    throw new HyperMapperException("Unable to perform an UPDATE list operation, could not find the collection item on the repository");
                }

                boolean inCollection = oneToManyContains(entity, propertyDescriptor.getPropertyName(), itemId);

                if (!inCollection) {
                    throw new HyperMapperException("Unable to perform an UPDATE using the ListOperation value, could not find the value on the collection of the repository entity");
                }
            }

            //Case REMOVE: remove from the relationship on both sides
            if (listOperation.getOperationType() == ListOperation.ListOperationType.REMOVE) {

                if (itemId == null) {
                    throw new HyperMapperException("Unable to perform a REMOVE list operation, could not find the item on the repository");
                }

                boolean inCollection = oneToManyContains(entity, propertyDescriptor.getPropertyName(), itemId);

                if (!inCollection) {
                    throw new HyperMapperException("Unable to perform a REMOVE ListOperation, could not find the value on the collection of the repository entity");
                }

                // If not found in the repository throws an exception.
                Object repositoryEntity = findEntityInRepository(value.getSourceClass(), itemId, contextInfo);
                removeRelationship(entity, HyperMapperPropertyUtils.getPropertyDescriptor(entity, propertyDescriptor.getPropertyName()), repositoryEntity);
                continue;
            }

            if (listOperation.getItemOperationType() == ListOperation.ItemOperationType.NONE) {
                continue;
            }

            //Retrieve or build the entity
            ToEntityResult<?> toEntityResult = mapDtoToEntity((Dto<?>) value, contextInfo, visitedEntities);
            Object childEntity = toEntityResult.getEntity();
            itemEntityResult.entity = childEntity;
            itemEntityResult.persistenceQueue.addAll(0, toEntityResult.getPersistenceQueue());
            //Set the parent on the new entity
            OneToMany oneToManyAnnotation = HyperMapperPropertyUtils.getPropertyDescriptor(entity, propertyDescriptor.getPropertyName()).getAnnotation(OneToMany.class);
            String mappedBy = oneToManyAnnotation == null ? null : oneToManyAnnotation.mappedBy();
            if (mappedBy != null) {
                HyperMapperPropertyUtils.setProperty(childEntity, mappedBy, entity);
            }

            // Case ADD: Add the new or updated entity to the collection property of the parent entity.
            if (listOperation.getOperationType() == ListOperation.ListOperationType.ADD) {
                addElementToOneToManyProperty(entity, propertyDescriptor.getPropertyName(), childEntity);
            }

        }

        return result;

    }

    /**
     * Converts a given Data Transfer Object (DTO) into an entity
     * This method is used to map the fields of the provided DTO to a new or existing entity
     * If the entity already exists in the repository, it is retrieved; otherwise, a new entity instance is created
     *
     * @param dto         the DTO to be converted into an entity
     * @param contextInfo additional context information used for repository operations
     * @param <T>         the entity type corresponding to the DTO
     * @return a ToEntityResult containing the mapped entity and a list of created/retrieved entities, in the order that they should be serialized
     */
    @NonNull
    public <T> ToEntityResult<T> mapDtoToEntity(@NonNull Dto<T> dto, C contextInfo) {
        return mapDtoToEntity(dto, contextInfo, new HashSet<>());
    }

    /**
     * Converts the given Data Transfer Object (DTO) into its corresponding entity, handling the mapping
     * of all DTO fields, relationships, and collections recursively. This method also ensures that
     * existing entities are fetched from the repository, avoiding duplication.
     * Please note that new entities are not persisted automatically.
     * <p>
     * The method uses a `visitedEntities` set to track already processed DTOs, preventing circular
     * references from causing infinite loops.
     *
     * @param dto             the DTO to be converted into an entity
     * @param contextInfo     additional contextual data for repository operations
     * @param visitedEntities a set to track already processed DTOs and prevent cycles in the mapping process
     * @param <T>             the type of the entity corresponding to the given DTO
     * @return a ToEntityResult containing the mapped entity and the serialization queue in the order they should be serialized
     */
    @NonNull
    public <T> ToEntityResult<T> mapDtoToEntity(@NonNull Dto<T> dto, C contextInfo, @NonNull HashSet<Object> visitedEntities) {

        Class<Dto<T>> dtoClass = (Class<Dto<T>>) dto.getClass();
        //A Dto's generic parameter MUST always match its DtoFor annotation value, an invalid cast will always be a programming error
        Class<T> entityClass = (Class<T>) dto.getSourceClass();
        //Entities MUST always have an @Id, so this MUST never be null
        PropertyDescriptor entityIdPropDesc = getIdPropertyDescriptor(entityClass);
        ToEntityResult<T> result = new ToEntityResult<>();
        List<Object> prioritySerializationQueue = new ArrayList<>();

        if (entityIdPropDesc == null) {
            throw new HyperMapperException("Entity types without an @Id property are not supported. Type: " + entityClass.getName());
        }

        visitedEntities.add(dto);

        T entity = null;

        try {

            Object dtoIdWrapper = HyperMapperPropertyUtils.getProperty(dto, entityIdPropDesc.getPropertyName());
            Serializable dtoId = valueWrapperAdapter.isSupportedWrapperInstance(dtoIdWrapper)
                    ? (Serializable) valueWrapperAdapter.unwrap(dtoIdWrapper)
                    : (Serializable) dtoIdWrapper;

            //Retrieves an instance from the repository or creates a new instance, depending on whether it has an id or not
            if (dtoId != null) {
                //Retrieve entity from Repository
                entity = findEntityInRepository(entityClass, dtoId, contextInfo);
            }

            if (dtoId == null) {
                //Create new entity instance
                entity = entityClass.getDeclaredConstructor().newInstance();
            }

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            throw new HyperMapperException("Reflection exception while processing entity of type " + entityClass.getName(), e);
        }

        Collection<PropertyDescriptor> dtoPropDescriptors = HyperMapperPropertyUtils.getProperties(dtoClass);

        //Map properties
        for (PropertyDescriptor propertyDescriptor : dtoPropDescriptors) {

            if (Objects.equals(propertyDescriptor.getPropertyName(), "sourceClass")) {
                continue;
            }

            try {

                Object propertyValue = HyperMapperPropertyUtils.getProperty(dto, propertyDescriptor.getPropertyName());

                if (propertyValue == null) {
                    throw new HyperMapperException("Property " + propertyDescriptor.getPropertyName() + " is null. " +
                            "Dto fields MUST NEVER be null. Use a non-empty wrapper that contains a null value or an empty wrapper for absent values");
                }

                if (!(valueWrapperAdapter.isSupportedWrapperInstance(propertyValue))) {
                    throw new HyperMapperException("Property " + propertyDescriptor.getPropertyName() + " in type " + dtoClass.getCanonicalName() + " is of type " + propertyValue.getClass().getSimpleName() + ". " +
                            "All Dto Fields must be of the wrapper type");
                }

                //If the DTO property is absent/empty it is ignored
                if (valueWrapperAdapter.isEmpty(propertyValue)) {
                    continue;
                }

                Object unwrappedValue = valueWrapperAdapter.unwrap(propertyValue);

                //TODO It should not be ignored, the mapped result should be used and set on the property before continuing to the next entity
                //If the entity has already been processed, ignore it
                if (visitedEntities.contains(unwrappedValue)) {
                    continue;
                }

                //Collections
                if (unwrappedValue instanceof Collection<?> collectionValue) {

                    List<ToEntityResult<?>> listOperationsResult =
                            mapListOperations(entity, propertyDescriptor, collectionValue, contextInfo
                                    , visitedEntities);

                    for (ToEntityResult<?> listOperationsResultItem : listOperationsResult) {
                        result.persistenceQueue.addAll(0, listOperationsResultItem.getPersistenceQueue());
                    }

                    //Non-collections
                } else {

                    //If the value is a Dto, map it to an entity and handle the relationships
                    if (unwrappedValue instanceof Dto<?> dtoAux) {

                        ToEntityResult<?> toEntityResult = mapDtoToEntity(dtoAux, contextInfo, visitedEntities);
                        unwrappedValue = toEntityResult.entity;

                        //Non-owners have to be serialized first or there will be serialization errors because of null ids on the owner columns
                        if (!RelationshipManager.isRelationshipOwner(entity, propertyDescriptor.getPropertyName())) {
                            result.persistenceQueue.addAll(0, toEntityResult.persistenceQueue);
                        } else {
                            prioritySerializationQueue.addAll(toEntityResult.persistenceQueue);
                        }

                    }

                    //If the property is an Entity and the value has changed, remove the old relationship
                    Object oldPropValue = HyperMapperPropertyUtils.getProperty(entity, propertyDescriptor.getPropertyName());

                    if (isEntity(oldPropValue) && oldPropValue != unwrappedValue) {
                        var entityPropDescriptor = HyperMapperPropertyUtils.getPropertyDescriptor(entity, propertyDescriptor.getPropertyName());
                        removeRelationship(entity, entityPropDescriptor, oldPropValue);
                    }

                    //Set the value of the entity property
                    HyperMapperPropertyUtils.setProperty(entity, propertyDescriptor.getPropertyName(), unwrappedValue);

                }

            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new HyperMapperException("Reflection exception while processing property " + propertyDescriptor.getPropertyName() + " of entity type " + entityClass.getCanonicalName(), e);
            }

        }

        result.entity = entity;
        result.persistenceQueue.addFirst(entity);
        result.persistenceQueue.addAll(0, prioritySerializationQueue);

        return result;

    }

    private <E> Object mapEntityPropertyToDtoProperty(E entity, DtoProjection<? extends Dto<E>> dtoProjection, PropertyDescriptor propertyDescriptor) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        //Get value from entity and map it to the dto
        var entityPropertyValue = HyperMapperPropertyUtils.getProperty(entity, propertyDescriptor.getPropertyName());
        FieldConf fieldConf = ((FieldConf) HyperMapperPropertyUtils.getProperty(dtoProjection, propertyDescriptor.getPropertyName()));
        Object wrappedValue;

        //Property ignored by projection
        if (fieldConf == null || fieldConf.getPresence() == FieldConf.Presence.IGNORED) {

            wrappedValue = valueWrapperAdapter.empty();

            //Null property
        } else if (entityPropertyValue == null) {

            wrappedValue = valueWrapperAdapter.wrap(null);

            //Collection property
        } else if (entityPropertyValue instanceof Collection<?> collectionProperty) {

            //Create an instance of the adequate collection type
            Collection<ListOperation<Dto<?>>> values =
                    (collectionProperty instanceof List<?>) ? new ArrayList<>() : new HashSet<>();

            //Cast to the right FieldConf type
            DtoFieldConf nestedDtoConf = (DtoFieldConf<?>) fieldConf;

            //Fill the collection
            for (var element : collectionProperty) {
                Dto<?> dtoFromEntityElement = mapEntityToDto(element, (Class<Dto>) propertyDescriptor.getContainedType(), nestedDtoConf.dtoProjection);
                values.add(ListOperation.valueOf(dtoFromEntityElement));
            }

            wrappedValue = valueWrapperAdapter.wrap(values);

            //Dto property
        } else if (Dto.class.isAssignableFrom(propertyDescriptor.getContainedType())) {

            //Cast to the right FieldConf type
            DtoFieldConf nestedDtoConf = (DtoFieldConf<?>) fieldConf;
            var value = mapEntityToDto(entityPropertyValue, (Class<Dto>) propertyDescriptor.getContainedType(), nestedDtoConf.dtoProjection);
            wrappedValue = valueWrapperAdapter.wrap(value);

            //Plain property
        } else {

            wrappedValue = valueWrapperAdapter.wrap(entityPropertyValue);

        }

        return wrappedValue;

    }

    /**
     * Converts an entity into its corresponding Data Transfer Object (DTO).
     * This method takes an entity, the class type of the target DTO, and a projection configuration,
     * then maps each entity property to the appropriate DTO field.
     *
     * @param entity        the source entity to be converted
     * @param dtoType       the target DTO class type
     * @param dtoProjection the projection containing configuration for the DTO fields
     * @param <R>           the DTO type
     * @param <E>           the entity type
     * @return the mapped DTO instance containing the entity's data
     */
    @NonNull
    public <R extends Dto<E>, E> R mapEntityToDto(@NonNull E entity, @NonNull Class<R> dtoType, @NonNull DtoProjection<R> dtoProjection) {

        // Retrieve all properties of the DTO type for mapping
        Collection<PropertyDescriptor> dtoProperties = HyperMapperPropertyUtils.getProperties(dtoType);

        try {

            // Create an instance of the target DTO
            R result = dtoType.getDeclaredConstructor().newInstance();

            //Map properties
            for (PropertyDescriptor propertyDescriptor : dtoProperties) {

                // Skip the "sourceClass" property as it doesn't need mapping
                if (propertyDescriptor.getPropertyName().equals("sourceClass")) {
                    continue;
                }

                // Extract and map the value from the entity to the DTO, based on the projection config
                Object wrappedValue;
                wrappedValue = mapEntityPropertyToDtoProperty(entity, dtoProjection, propertyDescriptor);

                // Set the mapped value into the new DTO instance
                HyperMapperPropertyUtils.setProperty(result, propertyDescriptor.getPropertyName(), wrappedValue);

            }

            // Return the fully populated DTO
            return result;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            // Rethrow any reflection-related exception as a HyperMapperException
            throw new HyperMapperException("Reflection exception while processing entity of type " + entity.getClass().getCanonicalName(), e);
        }

    }

    public static class ToEntityResult<T> {

        T entity;
        List<Object> persistenceQueue = new ArrayList<>();

        public T getEntity() {
            return entity;
        }

        /**
         * A list that holds entities created or retrieved during the mapping operation, in the order they should be persisted.
         * This queue ensures proper serialization of objects, avoiding NOT NULL constraint problems caused by the
         * persistence of owners of relationships before the owned entity has been serialized
         */
        public List<Object> getPersistenceQueue() {
            return persistenceQueue;
        }

    }

    public static class HyperMapperException extends RuntimeException {

        public HyperMapperException(String message) {
            super(message);
        }

        public HyperMapperException(String message, Throwable cause) {
            super(message, cause);
        }

        @SuppressWarnings("unused")
        public HyperMapperException(Throwable cause) {
            super(cause);
        }
    }

}
