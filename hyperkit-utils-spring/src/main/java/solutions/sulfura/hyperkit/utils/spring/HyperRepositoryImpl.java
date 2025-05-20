package solutions.sulfura.hyperkit.utils.spring;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class HyperRepositoryImpl<C> implements HyperRepository<C> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public <T, ID extends Serializable> Optional<T> findById(@NonNull Class<T> entityClass, @NonNull ID id, C contextInfo) {
        T entity = entityManager.find(entityClass, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public <T> T save(@NonNull T entity, C contextInfo) {
        if (entityManager.contains(entity)) {
            return entityManager.merge(entity);
        } else {
            entityManager.persist(entity);
            return entity;
        }
    }

    @Override
    public <T> void delete(@NonNull T entity, C contextInfo) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    @Override
    public <T, ID extends Serializable> void deleteById(@NonNull Class<T> entityClass, @NonNull ID id, C contextInfo) {

        T entity = entityManager.find(entityClass, id);

        if (entity == null) {
            throw new IllegalArgumentException("Entity not found");
        }

        this.delete(entity, contextInfo);

    }

    @Override
    public <T> Long count(@NonNull Class<T> entityClass, Specification<T> spec, C contextInfo) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(entityClass);
        countQuery.select(cb.count(countRoot));

        if (spec != null) {
            Predicate predicate = spec.toPredicate(countRoot, countQuery, cb);
            if (predicate != null) {
                countQuery.where(predicate);
            }
        }

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private <T> CriteriaQuery<T> buildQueryWithSpecificationAndSort(@NonNull Class<T> entityClass,
                                                                            Specification<T> spec,
                                                                            Sort sort) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root);

        if (spec != null) {

            Predicate predicate = spec.toPredicate(root, query, cb);

            if (predicate != null) {
                query.where(predicate);
            }

        }

        if (sort != null && sort.isSorted()) {
            query.orderBy(QueryUtils.toOrders(sort, root, cb));
        }

        return query;

    }

    @Override
    public <T> List<T> findAll(@NonNull Class<T> entityClass, @NonNull Sort sort, C contextInfo) {
        CriteriaQuery<T>  query = buildQueryWithSpecificationAndSort(entityClass, null, sort);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public <T> Page<T> findAll(@NonNull Class<T> entityClass, @NonNull Pageable pageable, C contextInfo) {
        // Query for counting total elements
        Long totalElements = count(entityClass, null, null);

        // Query for fetching data
        CriteriaQuery<T> query = buildQueryWithSpecificationAndSort(entityClass, null, pageable.getSort());

        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(typedQuery.getResultList(), pageable, totalElements);
    }

    @Override
    public <T> List<T> findAll(@NonNull Class<T> entityClass, @NonNull Specification<T> spec, C contextInfo) {
        CriteriaQuery<T> query = buildQueryWithSpecificationAndSort(entityClass, spec, null);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public <T> List<T> findAll(@NonNull Class<T> entityClass, @NonNull Specification<T> spec, @NonNull Sort sort, C contextInfo) {
        CriteriaQuery<T> query = buildQueryWithSpecificationAndSort(entityClass, spec, sort);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public <T> Page<T> findAll(@NonNull Class<T> entityClass, @NonNull Specification<T> spec, @NonNull Pageable pageable, C contextInfo) {

        Long totalElements = count(entityClass, spec, null);

        CriteriaQuery<T> query = buildQueryWithSpecificationAndSort(entityClass, spec, pageable.getSort());

        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(typedQuery.getResultList(), pageable, totalElements);
    }
}
