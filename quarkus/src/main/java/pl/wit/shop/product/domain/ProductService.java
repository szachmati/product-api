package pl.wit.shop.product.domain;

import io.quarkus.panache.common.Sort;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;


@ApplicationScoped
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Transactional
    public void create(ProductSaveDto dto) {
        ProductCategory category = productCategoryRepository.getByName(dto.getCategory());
        checkIfProductWithGivenNameExistsInCategory(dto);
        productRepository.persistAndFlush(new Product(
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

    public List<Product> findAllByCategoryName(String category, Sort sort, int pageNumber, int pageSize) {
        return productRepository.findAllByCategoryName(category, sort, pageNumber, pageSize);
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

    public static class ProductAlreadyExistsException extends RuntimeException {
        ProductAlreadyExistsException(String name, String category) {
            super(String.format("Product with name: %s already exists in category: %s", name, category));
        }
    }
}
