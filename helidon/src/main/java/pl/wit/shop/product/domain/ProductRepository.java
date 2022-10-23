package pl.wit.shop.product.domain;

import pl.wit.shop.common.repository.BaseRepository;

import java.util.List;
import java.util.function.Supplier;

public interface ProductRepository extends BaseRepository<Product> {

    List<Product> findAllProductsInCategory(String category);

    @Override
    default Supplier<NotFoundException> notFoundException(String cause) {
        return () -> new ProductNotFoundException(cause);
    }

    class ProductNotFoundException extends NotFoundException {
        public ProductNotFoundException(String cause) {
            super(cause);
        }
    }
}
