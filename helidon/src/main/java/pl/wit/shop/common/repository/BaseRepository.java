package pl.wit.shop.common.repository;

import pl.wit.shop.shared.exception.NotFoundException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public interface BaseRepository<E> {

    EntityManager getEntityManager();

    Supplier<NotFoundException> notFoundException(String cause);

    Class<E> clazz();

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
        CriteriaBuilder criteria = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> query = (CriteriaQuery<E>) criteria.createQuery();
        Root<E> root = query.from(clazz());
        query.select(root).where(criteria.equal(root.get("uuid"), uuid));
        try {
            return Optional.ofNullable(getEntityManager().createQuery(query).getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    default E getByUuid(UUID uuid) {
        return findByUuid(uuid).orElseThrow(notFoundException(uuid.toString()));
    }

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
