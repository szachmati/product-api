package pl.wit.shop.product.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wit.shop.shared.exception.ConflictException;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

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
