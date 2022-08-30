package pl.wit.shop.product.api;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import pl.wit.shop.product.domain.ProductService;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;

import static pl.wit.shop.product.domain.ProductRepository.ProductNotFoundException;

@MicronautTest
class ProductApiTest implements ProductTestDataIdentifiers {

    private static final String PRODUCT_API = "/api/products";

    @Inject
    ProductService productService;

    @Test
    void delete_shouldReturn200(RequestSpecification spec) {
        doNothing().when(productService)
                .delete(any());
        spec
                .when()
                .delete(PRODUCT_API + "/{uuid}", PRODUCT_1_UUID)
                .then()
                .statusCode(200);
    }

    @Test
    void delete_shouldReturn404_whenProductNotExist(RequestSpecification spec) {
        willThrow(new ProductNotFoundException(NOT_EXISTING_PRODUCT_UUID))
                .given(productService).delete(NOT_EXISTING_PRODUCT_UUID);
        spec
                .when()
                .delete(PRODUCT_API + "/{uuid}", NOT_EXISTING_PRODUCT_UUID)
                .then()
                .statusCode(404);
    }

    @Test
    void findAllByCategoryName_shouldReturn200(RequestSpecification spec) {
        given(productService.findAllProductsInCategory(any(), any()))
                .willReturn(Page.empty());
       Page result =
                spec
                .when()
                .queryParams(Map.of(
                        "category", "HOME",
                        "sort", "name,desc",
                        "page", "2",
                        "size", "5"
                ))
                .get(PRODUCT_API)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response()
                .as(Page.class);

        assertThat(result.isEmpty(), Matchers.is(true));
        then(productService).should()
                .findAllProductsInCategory("HOME", Pageable.from(2, 5, Sort.of(Sort.Order.desc("name"))));
    }

    @MockBean(ProductService.class)
    ProductService productService() {
        return mock(ProductService.class);
    }
}
