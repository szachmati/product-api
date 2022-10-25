package pl.wit.shop.product.domain;

import pl.wit.shop.common.repository.BaseRepository;
import pl.wit.shop.shared.exception.NotFoundException;

import java.util.function.Supplier;

public interface ProductCategoryRepository extends BaseRepository<ProductCategory> {

    ProductCategory getByName(String name);

    @Override
    default Supplier<NotFoundException> notFoundException(String cause) {
        return () -> new ProductCategoryNotFoundException(cause);
    }

    class ProductCategoryNotFoundException extends NotFoundException {
       public ProductCategoryNotFoundException(String cause) {
            super(cause);
        }
    }
}
