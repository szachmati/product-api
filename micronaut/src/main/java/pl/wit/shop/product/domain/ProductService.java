package pl.wit.shop.product.domain;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import pl.wit.shop.shared.exception.ConflictException;

import javax.transaction.Transactional;
import java.util.UUID;

@Singleton
public class ProductService {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProductCategoryRepository productCategoryRepository;

    @Transactional
    public void create(ProductSaveDto dto) {
        ProductCategory category = productCategoryRepository.getByName(dto.getCategory());
        checkIfProductWithGivenNameExistsInCategory(dto);
        productRepository.save(new Product(
                category,
                dto.getName(),
                dto.getPrice()
        ));
    }

    @Transactional
    public void delete(UUID uuid) {
        Product product = productRepository.getByUuid(uuid);
        productRepository.delete(product);
    }

    public Page<Product> findAllProductsInCategory(String category, Pageable pageable) {
        return productRepository.findAllProductsInCategory(category, pageable);
    }

    private void checkIfProductWithGivenNameExistsInCategory(ProductSaveDto dto) {
        if (productRepository.existsByNameAndCategoryName(dto.getName(), dto.getCategory())) {
            throw new ProductAlreadyExistsException(dto.getName(), dto.getCategory());
        }
    }

    public static class ProductAlreadyExistsException extends ConflictException {
        public ProductAlreadyExistsException(String name, String category) {
            super(String.format("Product with name: %s already exists in category: %s", name, category));
        }
    }
}
