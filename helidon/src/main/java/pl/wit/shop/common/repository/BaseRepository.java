package pl.wit.shop.common.repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public interface BaseRepository<E> {

    EntityManager entityManager();

    Supplier<NotFoundException> notFoundException(String cause);

    default Class clazz() {
        return (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    default void delete(E entity) {
        entityManager().remove(entity);
    }

    default List<E> findAll() {
        CriteriaBuilder criteria = entityManager().getCriteriaBuilder();
        CriteriaQuery<E> cq = (CriteriaQuery<E>) criteria.createQuery();
        Root<E> root = cq.from(clazz());
        CriteriaQuery<E> query = cq.select(root);
        return entityManager().createQuery(query).getResultList();
    }

    default Optional<E> findByUuid(UUID uuid) {
        try {
            return Optional.ofNullable((E) entityManager().find(clazz(), uuid));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    default E getByUuid(UUID uuid) {
        return findByUuid(uuid).orElseThrow(notFoundException(uuid.toString()));
    }

    default E save(E entity) {
        if (entityManager().contains(entity)) {
            return entityManager().merge(entity);
        }
        entityManager().persist(entity);
        return entity;
    }

    class NotFoundException extends RuntimeException {
        public NotFoundException(String cause) {
            super(cause);
        }
    }
}
