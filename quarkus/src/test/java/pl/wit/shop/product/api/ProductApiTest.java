package pl.wit.shop.product.api;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import pl.wit.shop.product.domain.ProductService;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static pl.wit.shop.product.api.ProductInputBuilder.aProductInput;
import static pl.wit.shop.product.domain.ProductCategoryRepository.ProductCategoryNotFoundException;
import static pl.wit.shop.product.domain.ProductService.ProductAlreadyExistsException;

@QuarkusTest
public class ProductApiTest {

    @InjectMock
    ProductService productService;

    @Test
    void create_shouldReturn201() {
        doNothing().when(productService).create(any());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(aProductInput().build())
        .when()
                .post("/api/products")
        .then()
                .statusCode(201);

    }

    @Test
    void create_shouldReturn404_whenProductCategoryNotExist() {
        willThrow(new ProductCategoryNotFoundException("msg"))
                .given(productService).create(any());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(aProductInput().build())
        .when()
                .post("/api/products")
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
                .post("/api/products")
        .then()
                .statusCode(409);
    }

}
