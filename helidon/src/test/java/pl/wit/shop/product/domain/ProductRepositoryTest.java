package pl.wit.shop.product.domain;

import io.helidon.microprofile.tests.junit5.Configuration;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import pl.wit.shop.common.repository.Pageable;
import pl.wit.shop.common.repository.Sort;
import pl.wit.shop.product.test.base.BaseDatabaseTest;
import pl.wit.shop.product.test.transaction.TransactionOperations;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static pl.wit.shop.product.domain.ProductBuilder.aFirstHomeProduct;
import static pl.wit.shop.product.domain.ProductBuilder.aSecondHomeProduct;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryMatcher.isProductCategory;
import static pl.wit.shop.product.domain.ProductMatcher.isProduct;
import static pl.wit.shop.product.test.data.ProductTestDataIdentifiers.PRODUCT_1_UUID;
import static pl.wit.shop.product.test.data.ProductTestDataIdentifiers.PRODUCT_2_UUID;

@Slf4j
@HelidonTest
@Configuration(useExisting = true)
class ProductRepositoryTest extends BaseDatabaseTest implements TransactionOperations {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProductCategoryRepository productCategoryRepository;

    @Test
    void existsByNameAndCategoryName_shouldReturnBooleanValue() {
        inTransactionWithoutResult(() -> {
            final ProductCategory homeProductCategory = productCategoryRepository.save(aHomeProductCategory().build());
            productRepository.save(
                    aFirstHomeProduct()
                            .withName("Product 1")
                            .withCategory(homeProductCategory)
                            .build()
            );
        });

        boolean result = productRepository.existsByNameAndCategoryName("Product 1", "HOME");
        log.info("result: {}", result);

        assertThat(result, Matchers.is(true));
    }

    @Test
    void findAllProductsInCategory_shouldReturnAllProductsInCategory() {
        inTransactionWithoutResult(() -> {
            final ProductCategory homeProductCategory = productCategoryRepository.save(aHomeProductCategory().build());
            productRepository.save(
                    aFirstHomeProduct()
                            .withName("Product 1")
                            .withCategory(homeProductCategory)
                            .withPrice(new BigDecimal("16.77"))
                            .build()
            );

            productRepository.save(
                    aSecondHomeProduct()
                            .withName("Product 2")
                            .withCategory(homeProductCategory)
                            .withPrice(new BigDecimal("30.21"))
                            .build()
            );
        });

       List<Product> result =  productRepository.findAllProductsInCategory(
               "HOME",
             new Pageable(
                       Sort.DESC,
                       "price",
                       0,
                       5
               )
       );

       assertThat(result, contains(
               isProduct()
                    .withUuid(PRODUCT_2_UUID)
                    .withCategory(isProductCategory()
                            .withName("HOME")
                    )
                    .withName("Product 2")
                    .withPrice(new BigDecimal("30.21")),
               isProduct()
                       .withUuid(PRODUCT_1_UUID)
                       .withCategory(isProductCategory()
                               .withName("HOME")
                       )
                       .withName("Product 1")
                       .withPrice(new BigDecimal("16.77"))
       ));
    }
}
