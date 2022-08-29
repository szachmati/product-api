package pl.wit.shop.product.domain;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static pl.wit.shop.product.domain.ProductBuilder.aFirstHomeProduct;
import static pl.wit.shop.product.domain.ProductBuilder.aMonitorProduct;
import static pl.wit.shop.product.domain.ProductBuilder.aSecondHomeProduct;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.anElectronicsProductCategory;

@MicronautTest
class ProductRepositoryTest implements ProductTestDataIdentifiers {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProductCategoryRepository productCategoryRepository;

    @Test
    void findAllProductsInCategory_shouldReturnPageWithHomeProductsSortedByPriceDesc() {
        ProductCategory homeCategory = productCategoryRepository.save(aHomeProductCategory().build());
        ProductCategory electronicsCategory = productCategoryRepository.save(anElectronicsProductCategory().build());
        productRepository.saveAll(List.of(
                aFirstHomeProduct().withCategory(homeCategory).build(),
                aSecondHomeProduct().withCategory(homeCategory).build(),
                aMonitorProduct().withCategory(electronicsCategory).build()
        ));


        Page<Product> products = productRepository.findAllProductsInCategory("HOME",
                Pageable.from(0, 5, Sort.of(Sort.Order.desc("price")))
        );

        assertThat(products.getTotalSize(), is(2L));
        //TODO
//        assertThat(products.getContent(), contains(
//               ProductMatcher.isProduct()
//                        .withUuid(PRODUCT_2_UUID)
//                        .withCategory(isProductCategory().withName("HOME"))
//                        .withPrice(new BigDecimal("30.87")),
//                isProduct()
//                        .withUuid(PRODUCT_1_UUID)
//                        .withCategory(isProductCategory().withName("HOME"))
//                        .withPrice(BigDecimal.ONE)
//        ));
    }
}
