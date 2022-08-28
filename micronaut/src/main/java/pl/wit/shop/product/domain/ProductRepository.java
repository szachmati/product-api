package pl.wit.shop.product.domain;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import pl.wit.shop.shared.exception.NotFoundException;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByUuid(UUID uuid);

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
