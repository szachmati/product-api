package pl.wit.shop.product.domain;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import pl.wit.shop.shared.exception.NotFoundException;

import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends CrudRepository<ProductCategory, Long> {

    Optional<ProductCategory> findByName(String categoryName);

    default ProductCategory getByName(String category) {
        return findByName(category)
                .orElseThrow(() -> new ProductCategoryNotFoundException(category));
    }

    class ProductCategoryNotFoundException extends NotFoundException {
        public ProductCategoryNotFoundException(String name) {
            super(String.format("Product category: %s not found", name));
        }
    }
}
