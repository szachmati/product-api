package pl.wit.shop.product.domain;

import lombok.RequiredArgsConstructor;
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
        checkIfProductWithGivenNameExistsInCategory(dto);
        productRepository.save(new Product(
                productCategoryRepository.getByName(dto.getCategory()),
                dto.getName(),
                dto.getPrice())
        );
    }

    @Transactional
    public void update(UUID uuid, ProductSaveDto dto) {
        Product product = productRepository.getByUuid(uuid);
        ProductCategory productCategory = productCategoryRepository.getByName(dto.getCategory());
        checkIfProductWithGivenNameExistsInCategory(dto);
        product.update(productCategory, dto.getName(), dto.getPrice());
    }

    @Transactional
    public void delete(UUID uuid) {
        Product product = productRepository.getByUuid(uuid);
        productRepository.delete(product);
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
