package pl.wit.shop.product.domain;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import lombok.Getter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import pl.wit.shop.common.repository.Pageable;
import pl.wit.shop.common.repository.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ProductRepositoryFacadeImpl implements ProductRepository {

    @Getter
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean existsByNameAndCategoryName(String productName, String categoryName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        root.fetch("category", JoinType.INNER);
        query.select(root).where(cb.and(
                cb.equal(root.get("name"), productName),
                cb.equal(root.get("category").get("name"), categoryName))
        );
        try {
            entityManager.createQuery(query).getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<Product> findByUuid(UUID uuid) {
        CriteriaBuilder criteria = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Product> query = criteria.createQuery(Product.class);
        Root<Product> root = query.from(clazz());
        root.fetch("category", JoinType.INNER);
        query.select(root).where(criteria.equal(root.get("uuid"), uuid));
        try {
            return Optional.ofNullable(getEntityManager().createQuery(query).getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findAllProductsInCategory(Pageable pageable, String category) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        root.fetch("category", JoinType.INNER);
        query.select(root)
                .where(cb.equal(root.get("category").get("name"), category));
        if (pageable.sortField() != null) {
            Expression<Product> ex = root.get(pageable.sortField());
            Order order = pageable.sort() == Sort.ASC ? cb.asc(ex) : cb.desc(ex);
            query.orderBy(order);
        }
        return entityManager.createQuery(query)
                .setFirstResult(pageable.firstElement())
                .setMaxResults(pageable.pageSize())
                .getResultList();
    }
}
