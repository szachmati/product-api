package pl.wit.shop.product.domain;

import pl.wit.shop.common.repository.BaseRepository;
import pl.wit.shop.shared.exception.NotFoundException;

public interface ProductCategoryRepository extends BaseRepository<ProductCategory> {

    ProductCategory getByName(String name);

    class ProductCategoryNotFoundException extends NotFoundException {
       public ProductCategoryNotFoundException(String cause) {
            super(cause);
        }
    }
}
