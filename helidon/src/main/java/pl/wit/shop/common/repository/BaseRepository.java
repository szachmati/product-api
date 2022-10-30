package pl.wit.shop.common.repository;

import jakarta.transaction.Transactional;
import pl.wit.shop.shared.exception.NotFoundException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public interface BaseRepository<E> {

    EntityManager getEntityManager();

    Supplier<NotFoundException> notFoundException(String cause);

    default Class clazz() {
        return (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Transactional
    default void delete(E entity) {
        getEntityManager().remove(entity);
    }

    default List<E> findAll() {
        CriteriaBuilder criteria = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> cq = (CriteriaQuery<E>) criteria.createQuery();
        Root<E> root = cq.from(clazz());
        CriteriaQuery<E> query = cq.select(root);
        return getEntityManager().createQuery(query).getResultList();
    }

    default Optional<E> findByUuid(UUID uuid) {
        try {
            return Optional.ofNullable((E) getEntityManager().find(clazz(), uuid));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    default E getByUuid(UUID uuid) {
        return findByUuid(uuid).orElseThrow(notFoundException(uuid.toString()));
    }

    @Transactional
    default E save(E entity) {
        final EntityManager em = getEntityManager();
        if (em.contains(entity)) {
            return em.merge(entity);
        }
        em.persist(entity);
        em.flush();
        return entity;
    }
}
