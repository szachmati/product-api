package pl.wit.shop.product.integration;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import pl.wit.shop.product.api.ProductApi;
import pl.wit.shop.product.domain.ProductCategoryRepository;
import pl.wit.shop.product.domain.ProductRepository;
import pl.wit.shop.product.test.base.BaseDatabaseTest;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static pl.wit.shop.product.api.ProductApiMatcher.ProductOutputMatcher.isProductOutput;
import static pl.wit.shop.product.api.ProductInputBuilder.aProductInput;
import static pl.wit.shop.product.domain.ProductBuilder.aFirstHomeProduct;
import static pl.wit.shop.product.domain.ProductBuilder.aSecondHomeProduct;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.anElectronicsProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryMatcher.isProductCategory;
import static pl.wit.shop.product.domain.ProductMatcher.isProduct;
import static pl.wit.shop.product.api.ProductApi.ProductOutput;

@QuarkusTest
class ProductApiIntegrationTest extends BaseDatabaseTest implements ProductTestDataIdentifiers {

    private static final String PRODUCT_API = "/api/products";

    @Inject
    ProductCategoryRepository productCategoryRepository;

    @Inject
    ProductRepository productRepository;

    @Test
    void create_shouldCreateProduct() {
        QuarkusTransaction.run(() -> productCategoryRepository.persistAndFlush(aHomeProductCategory().build()));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(aProductInput().build())
        .when()
                .post(PRODUCT_API)
        .then()
                .statusCode(201);

        assertThat(productRepository.findAll().list(), contains(
                isProduct()
                        .withUuid(notNullValue())
                        .withName("Home product")
                        .withCategory(isProductCategory().withName("HOME"))
                        .withPrice(new BigDecimal("1.00"))
        ));
    }

    @Test
    void delete_shouldDeleteProduct() {
        QuarkusTransaction.run(() -> {
            productCategoryRepository.persistAndFlush(aHomeProductCategory().build());
            productRepository.persistAndFlush(aFirstHomeProduct().build());
        });

        given()
        .when()
                .delete(PRODUCT_API + "/{uuid}", PRODUCT_1_UUID)
        .then()
                .statusCode(200);

        assertThat(productRepository.find("uuid", PRODUCT_1_UUID).firstResultOptional(), is(Optional.empty()));
    }

    @Test
    void findAllByCategoryName_shouldReturnAllProductsInCategorySortedByPriceDescending() {
        QuarkusTransaction.run(() -> {
            productCategoryRepository.persistAndFlush(aHomeProductCategory().build());
            productCategoryRepository.persistAndFlush(anElectronicsProductCategory().build());
            productRepository.persistAndFlush(aFirstHomeProduct().build());
            productRepository.persistAndFlush(aSecondHomeProduct().build());
        });

        List<ProductOutput> result = Arrays.asList(
                given()
                  .contentType(MediaType.APPLICATION_JSON)
                    .queryParams(Map.of(
                            "category", "HOME",
                            "sort", ProductApi.ProductSort.PRODUCT_NAME,
                            "sortDir", Sort.Direction.Descending.name(),
                            "page", "0",
                            "size", "5"
                    ))
                .when()
                    .get(PRODUCT_API)
                .thenReturn()
                    .getBody()
                    .as(ProductApi.ProductOutput[].class)
        );

        assertThat(result, contains(
                isProductOutput()
                        .withUuid(PRODUCT_2_UUID)
                        .withCategory("HOME")
                        .withName("Second home product")
                        .withPrice(new BigDecimal("30.87")),
                isProductOutput()
                        .withUuid(PRODUCT_1_UUID)
                        .withCategory("HOME")
                        .withName("Home product")
                        .withPrice(new BigDecimal("1.00"))
        ));
    }

    @Test
    void update_shouldUpdateExistingProduct() {
        QuarkusTransaction.run(() -> {
            productCategoryRepository.persistAndFlush(aHomeProductCategory().build());
            productCategoryRepository.persistAndFlush(anElectronicsProductCategory().build());
            productRepository.persistAndFlush(aFirstHomeProduct().build());
        });

        given()
                .body(aProductInput()
                        .withName("LG 2000")
                        .withCategory("ELECTRONICS")
                        .withPrice(new BigDecimal("2100.75"))
                        .build()
                )
                .contentType(MediaType.APPLICATION_JSON)
        .when()
                .put(PRODUCT_API + "/{uuid}", PRODUCT_1_UUID)
        .then()
                .statusCode(200);

        assertThat(productRepository.getByUuid(PRODUCT_1_UUID), isProduct()
                .withUuid(PRODUCT_1_UUID)
                .withCategory(isProductCategory().withName("ELECTRONICS"))
                .withName("LG 2000")
                .withPrice(new BigDecimal("2100.75"))
        );
    }

}
