package solutions.sulfura.hyperkit.utils.spring;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface for basic CRUD operations, RSQL queries, pagination and sorting
 * Methods are parameterized to allow using the same repository for all entities
 *
 * @param <C> the type of the context information used for repository operations
 */
@SuppressWarnings("unused")
public interface HyperRepository<C> {

    /**
     * Find an entity by its ID
     *
     * @param entityClass the class of the entity
     * @param id the ID of the entity
     * @param contextInfo additional information required for the operation
     * @return optional containing the entity if found
     */
    <T, ID extends Serializable> Optional<T> findById(@NonNull Class<T> entityClass, @NonNull ID id, C contextInfo);

    /**
     * Save an entity
     *
     * @param entity the entity to save
     * @param contextInfo additional information required for the operation
     * @return the saved entity
     */
    <T> T save(@NonNull T entity, C contextInfo);

    /**
     * Delete an entity
     *
     * @param entity the entity to delete
     * @param contextInfo additional information required for the operation
     */
    <T> void delete(@NonNull T entity, C contextInfo);

    /**
     * Delete an entity by its ID
     *
     * @param entityClass the class of the entity
     * @param id the ID of the entity
     * @param contextInfo additional information required for the operation
     */
    <T, ID extends Serializable> void deleteById(@NonNull Class<T> entityClass, @NonNull ID id, C contextInfo);

    <T> Long count(@NonNull Class<T> entityClass, Specification<T> spec, C contextInfo);

    /**
     * Find all entities of the given type
     *
     * @param entityClass the class of the entity
     * @param sort the sort specification to be used
     * @param contextInfo additional information required for the operation
     * @return list of all entities sorted according to the given sort specification
     */
    <T> List<T> findAll(@NonNull Class<T> entityClass, @NonNull Sort sort, C contextInfo);

    /**
     * Find all entities of the given type with pagination
     *
     * @param entityClass the class of the entity
     * @param pageable the pagination information
     * @param contextInfo additional information required for the operation
     * @return page of entities
     */
    <T> Page<T> findAll(@NonNull Class<T> entityClass, @NonNull Pageable pageable, C contextInfo);

    /**
     * Find all entities of the given type matching the specification
     *
     * @param entityClass the class of the entity
     * @param spec the specification to filter entities
     * @param contextInfo additional information required for the operation
     * @return list of entities matching the specification
     */
    <T> List<T> findAll(@NonNull Class<T> entityClass, @NonNull Specification<T> spec, C contextInfo);

    /**
     * Find all entities of the given type matching the specification with sorting
     *
     * @param entityClass the class of the entity
     * @param spec the specification to filter entities
     * @param sort the sort specification to be used
     * @param contextInfo additional information required for the operation
     * @return list of entities matching the specification sorted according to the given sort specification
     */
    <T> List<T> findAll(@NonNull Class<T> entityClass, @NonNull Specification<T> spec, @NonNull Sort sort, C contextInfo);

    /**
     * Find all entities of the given type matching the specification with pagination
     *
     * @param entityClass the class of the entity
     * @param spec the specification to filter entities
     * @param pageable the pagination information
     * @param contextInfo additional information required for the operation
     * @return page of entities matching the specification
     */
    <T> Page<T> findAll(@NonNull Class<T> entityClass, @NonNull Specification<T> spec, @NonNull Pageable pageable, C contextInfo);
}
