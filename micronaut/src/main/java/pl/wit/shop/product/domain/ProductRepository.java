package pl.wit.shop.product.domain;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.CrudRepository;
import pl.wit.shop.shared.exception.NotFoundException;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByUuid(UUID uuid);

    @Query(value = "SELECT product_ FROM Product product_ " +
            "INNER JOIN product_.category product_category_ " +
            "WHERE product_category_.name = :category",
            countQuery = "SELECT count(product_) FROM Product product_ " +
                    "WHERE product_.category.name = :category"
    )
    Page<Product> findAllProductsInCategory(String category, Pageable pageable);

    default Product getByUuid(UUID uuid) {
        return findByUuid(uuid)
                .orElseThrow(() -> new ProductNotFoundException(uuid));
    }

    class ProductNotFoundException extends NotFoundException {
        public ProductNotFoundException(UUID uuid) {
            super(String.format("Product with uuid: %s not found", uuid));
        }
    }
}
