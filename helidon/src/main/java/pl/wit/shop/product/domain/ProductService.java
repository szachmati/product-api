package pl.wit.shop.product.domain;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import pl.wit.shop.common.repository.Pageable;
import pl.wit.shop.shared.exception.ConflictException;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
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

    @Transactional
    public void delete(UUID uuid) {
        Product product = productRepository.getByUuid(uuid);
        productRepository.delete(product);
    }

    public List<Product> findAllProductsInCategory(Pageable pageable, String category) {
        return productRepository.findAllProductsInCategory(pageable, category);
    }

    public Product getProduct(UUID uuid) {
        return productRepository.getByUuid(uuid);
    }

    @Transactional
    public void update(UUID uuid, ProductSaveDto dto) {
        Product product = productRepository.getByUuid(uuid);
        ProductCategory category = productCategoryRepository.getByName(dto.getCategory());
        boolean isNameChanged = !product.getName().equals(dto.getName());
        boolean isCategoryChanged = !product.getCategory().getName().equals(dto.getCategory());
        if (isNameChanged || isCategoryChanged) {
            checkIfProductWithGivenNameExistsInCategory(dto);
        }
        product.update(category, dto.getName(), dto.getPrice());
    }

    private void checkIfProductWithGivenNameExistsInCategory(ProductSaveDto dto) {
        if (productRepository.existsByNameAndCategoryName(dto.getName(), dto.getCategory())) {
            throw new ProductAlreadyExistsException(dto.getName(), dto.getCategory());
        }
    }

    public static class ProductAlreadyExistsException extends ConflictException {
        ProductAlreadyExistsException(String name, String category) {
            super(String.format("Product with name: %s already exists in category: %s", name, category));
        }
    }

}
