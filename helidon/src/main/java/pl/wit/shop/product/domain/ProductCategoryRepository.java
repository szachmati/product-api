package pl.wit.shop.product.domain;

import pl.wit.shop.common.repository.BaseRepository;
import pl.wit.shop.shared.exception.NotFoundException;

import java.util.Optional;
import java.util.function.Supplier;

public interface ProductCategoryRepository extends BaseRepository<ProductCategory> {

    Optional<ProductCategory> findByName(String name);
    ProductCategory getByName(String name);

    @Override
    default Supplier<NotFoundException> notFoundException(String name) {
        return () -> new ProductCategoryNotFoundException(name);
    }

    @Override
    default Class<ProductCategory> clazz() {
        return ProductCategory.class;
    }

    class ProductCategoryNotFoundException extends NotFoundException {
       public ProductCategoryNotFoundException(String name) {
            super(String.format("Product category with name: %s not found", name));
        }
    }
}
