package pl.wit.shop.product.domain;

import pl.wit.shop.common.repository.BaseRepository;
import pl.wit.shop.shared.exception.NotFoundException;

import java.util.List;
import java.util.function.Supplier;

public interface ProductRepository extends BaseRepository<Product> {

    boolean existsByNameAndCategoryName(String productName, String categoryName);

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
