package pl.wit.shop.product.domain;

import lombok.Getter;
import pl.wit.shop.shared.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.function.Supplier;

public class ProductCategoryRepositoryImpl implements ProductCategoryRepository {

    @Getter
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Supplier<NotFoundException> notFoundException(String cause) {
        return () -> new NotFoundException("");
    }

    @Override
    public ProductCategory getByName(String name) {
        return null;
    }
}
