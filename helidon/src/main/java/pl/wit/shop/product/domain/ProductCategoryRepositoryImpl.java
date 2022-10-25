package pl.wit.shop.product.domain;

import lombok.Getter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ProductCategoryRepositoryImpl implements ProductCategoryRepository {

    @Getter
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ProductCategory getByName(String name) {
        return null;
    }
}
