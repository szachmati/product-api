package pl.wit.shop.product.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.wit.shop.product.test.base.BaseIntegrationTest;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static pl.wit.shop.product.domain.ProductBuilder.aFirstHomeProduct;
import static pl.wit.shop.product.domain.ProductBuilder.aMonitorProduct;
import static pl.wit.shop.product.domain.ProductBuilder.aSecondHomeProduct;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHealthProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.anElectronicsProductCategory;

import static org.hamcrest.MatcherAssert.assertThat;
import static pl.wit.shop.product.domain.ProductCategoryMatcher.isProductCategory;
import static pl.wit.shop.product.domain.ProductMatcher.isProduct;
import static pl.wit.shop.product.test.data.ProductTestDataIdentifiers.PRODUCT_1_UUID;
import static pl.wit.shop.product.test.data.ProductTestDataIdentifiers.PRODUCT_2_UUID;

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

    @Test
    void findAllProductsInCategory_shouldReturnPageWithHomeProductsSortedByPriceDesc() {
        productCategoryRepository.saveAll(List.of(
                aHomeProductCategory().build(),
                aHealthProductCategory().build(),
                anElectronicsProductCategory().build()
        ));
        productRepository.saveAll(List.of(
                aFirstHomeProduct().build(),
                aSecondHomeProduct().build(),
                aMonitorProduct().build()
        ));

        Page<Product> products = productRepository.findAllProductsInCategory("HOME",
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "price"))
        );

        assertThat(products.getContent(), contains(
                isProduct()
                        .withUuid(PRODUCT_2_UUID)
                        .withCategory(isProductCategory().withName("HOME"))
                        .withPrice(new BigDecimal("30.87")),
                isProduct()
                        .withUuid(PRODUCT_1_UUID)
                        .withCategory(isProductCategory().withName("HOME"))
                        .withPrice(BigDecimal.ONE)
        ));
    }
}
