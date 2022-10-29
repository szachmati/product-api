package pl.wit.shop.product.domain;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@ApplicationScoped
public class ProductCategoryRepositoryFacadeImpl implements ProductCategoryRepository {

    @Getter
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ProductCategory> findByName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductCategory> query = cb.createQuery(ProductCategory.class);
        Root<ProductCategory> root = query.from(ProductCategory.class);
        query.select(root).where(cb.equal(root.get("name"), name));
        try {
            return Optional.of(entityManager.createQuery(query).getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public ProductCategory getByName(String name) {
      return findByName(name).orElseThrow(notFoundException(name));
    }
}
