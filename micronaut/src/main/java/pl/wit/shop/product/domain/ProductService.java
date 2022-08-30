package pl.wit.shop.product.domain;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.UUID;

@Singleton
public class ProductService {

    @Inject
    private ProductRepository productRepository;

    @Transactional
    public void delete(UUID uuid) {
        Product product = productRepository.getByUuid(uuid);
        productRepository.delete(product);
    }

    public Page<Product> findAllProductsInCategory(String category, Pageable pageable) {
        return productRepository.findAllProductsInCategory(category, pageable);
    }
}
