package pl.wit.shop.product.domain;

import io.quarkus.panache.common.Sort;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import pl.wit.shop.product.test.base.BaseDatabaseTest;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import javax.inject.Inject;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static pl.wit.shop.product.domain.ProductBuilder.aFirstHomeProduct;
import static pl.wit.shop.product.domain.ProductBuilder.aSecondHomeProduct;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.anElectronicsProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryMatcher.isProductCategory;
import static pl.wit.shop.product.domain.ProductMatcher.isProduct;

@QuarkusTest
@TestTransaction
class ProductRepositoryTest extends BaseDatabaseTest implements ProductTestDataIdentifiers {

    @Inject
    ProductRepository productRepository;

    @Inject
    ProductCategoryRepository productCategoryRepository;

    @Test
    void existsByNameAndCategoryName_shouldReturnTrue() {
        productCategoryRepository.persistAndFlush(aHomeProductCategory().build());
        productRepository.persistAndFlush(aFirstHomeProduct().build());

        boolean result  = productRepository.existsByNameAndCategoryName("Home product", "HOME");

        assertThat(result, is(true));
    }

    @Test
    void findAllByCategoryName_shouldReturnAllProductsInCategorySortedByPriceDescending() {
        productCategoryRepository.persistAndFlush(aHomeProductCategory().build());
        productCategoryRepository.persistAndFlush(anElectronicsProductCategory().build());
        productRepository.persistAndFlush(aFirstHomeProduct().build());
        productRepository.persistAndFlush(aSecondHomeProduct().build());

        List<Product> result = productRepository.findAllByCategoryName(
                "HOME",
                Sort.by("price").descending(),
                0,
                20
        );

        assertThat(result, contains(
                isProduct()
                        .withUuid(PRODUCT_2_UUID)
                        .withCategory(isProductCategory().withName("HOME"))
                        .withName("Second home product")
                        .withPrice(new BigDecimal("30.87")),
                isProduct()
                        .withUuid(PRODUCT_1_UUID)
                        .withCategory(isProductCategory().withName("HOME"))
                        .withName("Home product")
                        .withPrice(BigDecimal.ONE)
        ));
    }
}
