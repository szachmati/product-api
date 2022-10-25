package pl.wit.shop.product.domain;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class ProductRepositoryFacadeImpl implements ProductRepository {

    @Getter
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean existsByNameAndCategoryName(String productName, String categoryName) {
        return false;
    }

    @Override
    public List<Product> findAllProductsInCategory(String category) {
        return List.of();
    }
}
