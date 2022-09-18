package pl.wit.shop.product.domain;

import io.quarkus.panache.common.Sort;
import lombok.RequiredArgsConstructor;
import pl.wit.shop.shared.exception.ConflictException;

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
        ProductCategory category = productCategoryRepository.getByName(dto.category());
        checkIfProductWithGivenNameExistsInCategory(dto);
        productRepository.persistAndFlush(new Product(
                category,
                dto.name(),
                dto.price()
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
        ProductCategory category = productCategoryRepository.getByName(dto.category());
        boolean isNameChanged = !product.getName().equals(dto.name());
        boolean isCategoryChanged = !product.getCategory().getName().equals(dto.category());
        if (isNameChanged || isCategoryChanged) {
            checkIfProductWithGivenNameExistsInCategory(dto);
        }
        product.update(category, dto.name(), dto.price());
    }

    private void checkIfProductWithGivenNameExistsInCategory(ProductSaveDto dto) {
        if (productRepository.existsByNameAndCategoryName(dto.name(), dto.category())) {
            throw new ProductAlreadyExistsException(dto.name(), dto.category());
        }
    }

    public static class ProductAlreadyExistsException extends ConflictException {
        public ProductAlreadyExistsException(String name, String category) {
            super(String.format("Product with name: %s already exists in category: %s", name, category));
        }
    }
}
