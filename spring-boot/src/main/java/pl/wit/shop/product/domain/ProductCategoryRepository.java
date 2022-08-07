package pl.wit.shop.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wit.shop.shared.exception.NotFoundException;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    Optional<ProductCategory> findByName(String name);

    default ProductCategory getByName(String name) {
        return findByName(name)
                .orElseThrow(() -> new ProductCategoryNotFoundException(name));
    }

    class ProductCategoryNotFoundException extends NotFoundException {
        ProductCategoryNotFoundException(String name) {
            super(String.format("Product category: %s not found", name));
        }
    }
}
