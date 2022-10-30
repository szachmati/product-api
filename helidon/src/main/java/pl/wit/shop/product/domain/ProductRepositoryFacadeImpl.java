package pl.wit.shop.product.domain;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.List;

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
    public List<Product> findAllProductsInCategory(String category) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        root.fetch("category", JoinType.INNER);
        query.select(root).where(cb.equal(root.get("category").get("name"), category));
        return entityManager.createQuery(query).getResultList();
    }
}
