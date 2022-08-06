package pl.wit.shop.product.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Transactional
    public void create(ProductSaveDto dto) {
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
        product.update(productCategory, dto.getName(), dto.getPrice());
    }
}
