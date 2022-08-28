package pl.wit.shop.product.api;

import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import pl.wit.shop.product.domain.ProductService;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doNothing;
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

    @MockBean(ProductService.class)
    ProductService productService() {
        return mock(ProductService.class);
    }
}
