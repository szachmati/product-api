package pl.wit.shop.product.domain;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
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
                cb.equal(root.get("category.name"), categoryName))
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
        query.select(root).where(cb.equal(root.get("category.name"), category));
        return entityManager.createQuery(query).getResultList();
    }
}
