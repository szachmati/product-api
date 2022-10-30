package pl.wit.shop.product.domain;

import io.helidon.microprofile.tests.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static pl.wit.shop.product.domain.ProductBuilder.aFirstHomeProduct;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;


@HelidonTest
class ProductRepositoryTest {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProductCategoryRepository productCategoryRepository;

    @Test
    @Transactional
    void existsByNameAndCategoryName_shouldReturnBooleanValue() {
        ProductCategory homeProductCategory = productCategoryRepository.save(aHomeProductCategory().build());
        productRepository.save(
                aFirstHomeProduct()
                        .withName("Product 1")
                        .withCategory(homeProductCategory)
                        .build()
        );

        boolean result = productRepository.existsByNameAndCategoryName("Product 1", "HOME");
        System.out.println(result);

        assertThat(result, Matchers.is(true));
    }

}
