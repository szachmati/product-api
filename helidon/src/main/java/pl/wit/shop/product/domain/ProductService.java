package pl.wit.shop.product.domain;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.UUID;

@Singleton
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Inject
    public ProductService(
            ProductRepository productRepository,
            ProductCategoryRepository productCategoryRepository
    ) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

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

    public void delete(UUID uuid) {
        Product product = productRepository.getByUuid(uuid);
        productRepository.delete(product);
    }

    private void checkIfProductWithGivenNameExistsInCategory(ProductSaveDto dto) {
        if (productRepository.existsByNameAndCategoryName(dto.getName(), dto.getCategory())) {
            throw new ProductAlreadyExistsException(dto.getName(), dto.getCategory());
        }
    }

    public static class ProductAlreadyExistsException extends RuntimeException {
        ProductAlreadyExistsException(String name, String category) {
            super(String.format("Product with name: %s already exists in category: %s", name, category));
        }
    }

}
