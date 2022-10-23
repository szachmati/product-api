package pl.wit.shop.product.domain;

import jakarta.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class ProductRepositoryFacadeImpl implements ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public EntityManager entityManager() {
        return entityManager;
    }

    @Override
    public List<Product> findAllProductsInCategory(String category) {
        return List.of();
    }
}
