package pl.wit.shop.product.domain;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import pl.wit.shop.product.test.base.PostgresContainer;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import java.math.BigDecimal;
import java.util.List;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
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
        final ProductCategory homeCategory = productCategoryRepository.save(aHomeProductCategory().build());
        final ProductCategory electronicsCategory = productCategoryRepository.save(anElectronicsProductCategory().build());
        final Product firstHomeProduct = aFirstHomeProduct()
                .withPrice(new BigDecimal("11.99"))
                .withCategory(homeCategory)
                .build();
        final Product secondHomeProduct = aSecondHomeProduct()
                .withCategory(homeCategory)
                .withPrice(new BigDecimal("20.30"))
                .build();
        productRepository.saveAll(List.of(
                firstHomeProduct,
                secondHomeProduct,
                aMonitorProduct().withCategory(electronicsCategory).build()
        ));

        Page<Product> result = productRepository.findAllProductsInCategory("HOME",
                Pageable.from(0, 5, Sort.of(Sort.Order.desc("price")))
        );

        assertThat(result.getContent(), contains(
                pojo(Product.class)
                        .withProperty("name", is("Second home product"))
                        .withProperty("price", is(new BigDecimal("20.30")))
                        .where("getCategory",
                                pojo(ProductCategory.class)
                                        .withProperty("name", is("HOME"))
                        ),
                pojo(Product.class)
                        .withProperty("name", is("Home product"))
                        .withProperty("price", is(new BigDecimal("11.99")))
                        .where("getCategory",
                                pojo(ProductCategory.class)
                                        .withProperty("name", is("HOME"))
                        )
        ));
    }
}
