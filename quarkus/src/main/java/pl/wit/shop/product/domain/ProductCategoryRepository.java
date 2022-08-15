package pl.wit.shop.product.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductCategoryRepository implements PanacheRepository<ProductCategory> {

    public ProductCategory getByName(String name) {
        return find("name = ?1", name)
                .firstResultOptional()
                .orElseThrow(() -> new ProductCategoryNotFoundException(name));
    }

    public static class ProductCategoryNotFoundException extends RuntimeException {
        ProductCategoryNotFoundException(String name) {
            super(String.format("Product category: %s not found", name));
        }
    }
}
