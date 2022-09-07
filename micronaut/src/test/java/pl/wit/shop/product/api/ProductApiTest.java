package pl.wit.shop.product.api;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.micronaut.http.MediaType;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import pl.wit.shop.product.domain.ProductCategoryRepository;
import pl.wit.shop.product.domain.ProductRepository;
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

import static pl.wit.shop.product.api.ProductInputBuilder.aProductInput;
import static pl.wit.shop.product.domain.ProductRepository.ProductNotFoundException;

@MicronautTest
class ProductApiTest implements ProductTestDataIdentifiers {

    private static final String PRODUCT_API = "/api/products";

    @Inject
    ProductService productService;

    @Test
    void create_shouldReturn201(RequestSpecification spec) {
        doNothing().when(productService).create(any());

        spec
                .contentType(MediaType.APPLICATION_JSON)
                .body(aProductInput().build())
                .when()
                .post(PRODUCT_API)
                .then()
                .statusCode(201);
    }

    @Test
    void create_shouldReturn404_whenProductCategoryNotExist(RequestSpecification spec) {
        willThrow(new ProductCategoryRepository.ProductCategoryNotFoundException("msg"))
                .given(productService).create(any());

        spec
                .contentType(MediaType.APPLICATION_JSON)
                .body(aProductInput().build())
                .when()
                .post(PRODUCT_API)
                .then()
                .statusCode(404);
    }

    @Test
    void create_shouldReturn409_whenProductAlreadyExistsInCategory(RequestSpecification spec) {
        willThrow(new ProductService.ProductAlreadyExistsException("product", "HOME"))
                .given(productService).create(any());

        spec
                .contentType(MediaType.APPLICATION_JSON)
                .body(aProductInput().build())
                .when()
                .post(PRODUCT_API)
                .then()
                .statusCode(409);
    }

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
                        "sort", "name",
                        "direction", "DESC",
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

    @Test
    void update_shouldReturn200(RequestSpecification spec) {
        doNothing().when(productService).update(any(), any());

        spec
                .contentType(MediaType.APPLICATION_JSON)
                .body(aProductInput().build())
                .when()
                .put(PRODUCT_API + "/{uuid}", PRODUCT_1_UUID)
                .then()
                .statusCode(200);
    }

    @Test
    void update_shouldReturn404_whenProductNotExist(RequestSpecification spec) {
        willThrow(new ProductRepository.ProductNotFoundException(PRODUCT_1_UUID))
                .given(productService).update(any(), any());

       spec
                .contentType(MediaType.APPLICATION_JSON)
                .body(aProductInput().build())
                .when()
                .put(PRODUCT_API + "/{uuid}", PRODUCT_1_UUID)
                .then()
                .statusCode(404);
    }

    @Test
    void update_shouldReturn404_whenProductCategoryNotExist(RequestSpecification spec) {
        willThrow(new ProductCategoryRepository.ProductCategoryNotFoundException("HOME"))
                .given(productService).update(any(), any());

        spec
                .contentType(MediaType.APPLICATION_JSON)
                .body(aProductInput().build())
                .when()
                .put(PRODUCT_API + "/{uuid}", PRODUCT_1_UUID)
                .then()
                .statusCode(404);
    }

    @Test
    void update_shouldReturn409_whenProductNameAlreadyExistsInCategory(RequestSpecification spec) {
        willThrow(new ProductService.ProductAlreadyExistsException("prod1", "HOME"))
                .given(productService).update(any(), any());

        spec
                .contentType(MediaType.APPLICATION_JSON)
                .body(aProductInput().build())
                .when()
                .put(PRODUCT_API + "/{uuid}", PRODUCT_1_UUID)
                .then()
                .statusCode(409);
    }

    @MockBean(ProductService.class)
    ProductService productService() {
        return mock(ProductService.class);
    }
}
