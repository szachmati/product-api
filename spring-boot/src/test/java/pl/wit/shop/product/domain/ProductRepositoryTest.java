package pl.wit.shop.product.domain;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.wit.shop.product.test.base.BaseIntegrationTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static pl.wit.shop.product.domain.ProductBuilder.aHomeProduct;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @BeforeEach
    void tearDown() {
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
    }

    //TODO delete later
    @Test
    void shouldSaveHomeProduct() {
        final ProductCategory homeCategory = productCategoryRepository.save(aHomeProductCategory().build());
        final Product homeProduct = aHomeProduct().withCategory(homeCategory).build();

        Product savedProduct = productRepository.save(homeProduct);

        assertThat(savedProduct, CoreMatchers.equalTo(homeProduct));
    }
}
