package pl.wit.shop.product.api;

import io.quarkus.panache.common.Sort;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import pl.wit.shop.product.domain.ProductRepository;
import pl.wit.shop.product.domain.ProductService;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import javax.ws.rs.core.MediaType;

import java.util.Collections;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static pl.wit.shop.product.api.ProductInputBuilder.aProductInput;
import static pl.wit.shop.product.domain.ProductCategoryRepository.ProductCategoryNotFoundException;
import static pl.wit.shop.product.domain.ProductService.ProductAlreadyExistsException;

@QuarkusTest
public class ProductApiTest implements ProductTestDataIdentifiers {

    private final String PRODUCT_API = "/api/products";

    @InjectMock
    ProductService productService;

    @Test
    void create_shouldReturn201() {
        doNothing().when(productService).create(any());
        final ProductApi.ProductInput input = aProductInput().build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(input)
        .when()
                .post(PRODUCT_API)
        .then()
                .statusCode(201);
        then(productService).should().create(input.toDto());
    }

    @Test
    void create_shouldReturn404_whenProductCategoryNotExist() {
        willThrow(new ProductCategoryNotFoundException("msg"))
                .given(productService).create(any());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(aProductInput().build())
        .when()
                .post(PRODUCT_API)
        .then()
                .statusCode(404);
    }

    @Test
    void create_shouldReturn409_whenProductAlreadyExistsInCategory() {
        willThrow(new ProductAlreadyExistsException("product", "HOME"))
                .given(productService).create(any());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(aProductInput().build())
        .when()
                .post(PRODUCT_API)
        .then()
                .statusCode(409);
    }

    @Test
    void delete_shouldReturn200() {
        doNothing().when(productService).delete(any());

        given()
        .when()
                .delete(PRODUCT_API + "/{uuid}", PRODUCT_1_UUID)
        .then()
                .statusCode(200);
        then(productService).should().delete(PRODUCT_1_UUID);
    }

    @Test
    void delete_shouldReturn404_whenProductNotExist() {
        willThrow(new ProductRepository.ProductNotFoundException(PRODUCT_1_UUID))
                .given(productService).delete(any());

        given()
        .when()
                .delete(PRODUCT_API + "/{uuid}", PRODUCT_1_UUID)
        .then()
                .statusCode(404);
    }

    @Test
    void findAllByCategoryName_shouldReturn200() {
        willReturn(Collections.emptyList())
                .given(productService).findAllByCategoryName(anyString(), any(), anyInt(), anyInt());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .queryParams(Map.of(
                        "category", "HOME",
                        "sort", ProductApi.ProductSort.PRODUCT_PRICE,
                        "sortDir", Sort.Direction.Descending.name(),
                        "page", "2",
                        "size", "5"
                ))
        .when()
                .get(PRODUCT_API)
        .then()
                .statusCode(200)
                .body("$", empty());
    }

    @Test
    void update_shouldReturn200() {
        doNothing().when(productService).update(any(), any());
        ProductApi.ProductInput input = aProductInput().build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(input)
        .when()
                .put(PRODUCT_API + "/{uuid}", PRODUCT_1_UUID)
        .then()
                .statusCode(200);
        then(productService).should().update(PRODUCT_1_UUID, input.toDto());
    }

    @Test
    void update_shouldReturn404_whenProductNotExist() {
        willThrow(new ProductRepository.ProductNotFoundException(PRODUCT_1_UUID))
                .given(productService).update(any(), any());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(aProductInput().build())
        .when()
                .put(PRODUCT_API + "/{uuid}", PRODUCT_1_UUID)
        .then()
                .statusCode(404);
    }

    @Test
    void update_shouldReturn404_whenProductCategoryNotExist() {
        willThrow(new ProductCategoryNotFoundException("HOME"))
                .given(productService).update(any(), any());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(aProductInput().build())
        .when()
                .put(PRODUCT_API + "/{uuid}", PRODUCT_1_UUID)
        .then()
                .statusCode(404);
    }

    @Test
    void update_shouldReturn409_whenProductNameAlreadyExistsInCategory() {
        willThrow(new ProductAlreadyExistsException("prod1", "HOME"))
                .given(productService).update(any(), any());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(aProductInput().build())
        .when()
                .put(PRODUCT_API + "/{uuid}", PRODUCT_1_UUID)
        .then()
                .statusCode(409);
    }

}
