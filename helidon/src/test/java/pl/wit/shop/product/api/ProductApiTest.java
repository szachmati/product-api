package pl.wit.shop.product.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.TransactionManager;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.wit.shop.common.repository.Sort;
import pl.wit.shop.product.domain.ProductCategory;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;
import pl.wit.shop.product.test.transaction.TransactionOperations;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static pl.wit.shop.product.api.ProductApiMatcher.ProductOutputMatcher.isProductOutput;
import static pl.wit.shop.product.api.ProductInputBuilder.aProductInput;
import static pl.wit.shop.product.domain.ProductBuilder.aFirstHomeProduct;
import static pl.wit.shop.product.domain.ProductBuilder.aMonitorProduct;
import static pl.wit.shop.product.domain.ProductBuilder.aSecondHomeProduct;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;
import static pl.wit.shop.product.api.ProductApi.ProductOutput;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.anElectronicsProductCategory;

@HelidonTest
public class ProductApiTest implements ProductTestDataIdentifiers, TransactionOperations {

    private static final String PRODUCT_API = "/api/products";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private WebTarget webTarget;

    @Getter
    @Inject
    private TransactionManager transactionManager;

    @BeforeEach
    void setUp() {
        inTransactionWithoutResult(() -> {
            entityManager
                    .createNativeQuery("TRUNCATE TABLE product, product_category RESTART IDENTITY")
                    .executeUpdate();
        });
    }

    @Test
    void create_shouldCreateProduct() {
        inTransactionWithoutResult(() -> {
            final ProductCategory homeProductCategory = aHomeProductCategory().build();
            entityManager.persist(homeProductCategory);
        });

        Response response = webTarget
                .path(PRODUCT_API)
                .request()
                .post(Entity.entity(aProductInput().build(), MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(HttpResponseStatus.CREATED.code()));
    }

    @Test
    void create_shouldReturn404_whenProductCategoryNotExist() {
        Response response = webTarget
                .path(PRODUCT_API)
                .request()
                .post(Entity.entity(aProductInput().build(), MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(HttpResponseStatus.NOT_FOUND.code()));
    }

    @Test
    void create_shouldReturn409_whenProductWithSameNameAlreadyExistInCategory() {
        inTransactionWithoutResult(() -> {
            final ProductCategory homeProductCategory = aHomeProductCategory().build();
            entityManager.persist(homeProductCategory);
            entityManager.persist(aFirstHomeProduct().withCategory(homeProductCategory).build());
        });

        Response response = webTarget
                .path(PRODUCT_API)
                .request()
                .post(Entity.entity(aProductInput().build(), MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(HttpResponseStatus.CONFLICT.code()));
    }

    @Test
    void delete_shouldDeleteProduct() {
        inTransactionWithoutResult(() -> {
            final ProductCategory homeProductCategory = aHomeProductCategory().build();
            entityManager.persist(homeProductCategory);
            entityManager.persist(aFirstHomeProduct().withCategory(homeProductCategory).build());
            entityManager.flush();
        });

        Response response = webTarget
                .path(PRODUCT_API + "/" + PRODUCT_1_UUID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildDelete()
                .invoke();

        assertThat(response.getStatus(), is(HttpResponseStatus.OK.code()));
    }

    @Test
    void delete_shouldReturn404_whenProductNotExist() {
        Response response = webTarget
                .path(PRODUCT_API + "/" + NOT_EXISTING_PRODUCT_UUID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildDelete()
                .invoke();

        assertThat(response.getStatus(), is(HttpResponseStatus.NOT_FOUND.code()));
    }

    @Test
    void findAllProductsInCategory_shouldReturnAllProductsInCategory() throws JsonProcessingException {
        inTransactionWithoutResult(() -> {
            final ProductCategory homeProductCategory = aHomeProductCategory().build();
            entityManager.persist(homeProductCategory);
            entityManager.persist(aFirstHomeProduct().withCategory(homeProductCategory).build());
            entityManager.persist(aSecondHomeProduct().withCategory(homeProductCategory).build());
        });

        Response response = webTarget
                .path(PRODUCT_API)
                .queryParam("category", "HOME")
                .queryParam("sort", Sort.DESC)
                .queryParam("sortField", "name")
                .queryParam("page", "0")
                .queryParam("size", "30")
                .request()
                .get();

        String jsonArray = response.readEntity(String.class);
        List<ProductOutput> products =  objectMapper.readValue(jsonArray, new TypeReference<>() {});
        assertThat(response.getStatus(), is(HttpResponseStatus.OK.code()));
        assertThat(products, contains(
                isProductOutput()
                        .withUuid(PRODUCT_2_UUID)
                        .withName("Second home product")
                        .withCategory("HOME"),
                isProductOutput()
                        .withUuid(PRODUCT_1_UUID)
                        .withName("Home product")
                        .withCategory("HOME")
        ));
    }

    @Test
    void getByUuid_shouldReturnProduct() throws Exception {
        inTransactionWithoutResult(() -> {
            final ProductCategory homeProductCategory = aHomeProductCategory().build();
            entityManager.persist(homeProductCategory);
            entityManager.persist(aFirstHomeProduct().withCategory(homeProductCategory).build());
        });

        Response response = webTarget
                .path(PRODUCT_API + "/" + PRODUCT_1_UUID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildGet()
                .invoke();

        String jsonResponse = response.readEntity(String.class);
        ProductOutput result = objectMapper.readValue(jsonResponse, ProductOutput.class);
        assertThat(response.getStatus(), is(HttpResponseStatus.OK.code()));
        assertThat(result, isProductOutput()
               .withUuid(PRODUCT_1_UUID)
               .withName("Home product")
               .withCategory("HOME")
        );
    }

    @Test
    void update_shouldUpdateProduct() {
        inTransactionWithoutResult(() -> {
            final ProductCategory homeProductCategory = aHomeProductCategory().build();
            entityManager.persist(homeProductCategory);
            entityManager.persist(aFirstHomeProduct().withCategory(homeProductCategory).build());
        });

        Response response = webTarget
                .path(PRODUCT_API + "/" + PRODUCT_1_UUID)
                .request()
                .put(Entity.entity(aProductInput().build(), MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(HttpResponseStatus.NO_CONTENT.code()));
    }

    @Test
    void update_shouldReturn404_whenProductCategoryNotExist() {
        Response response = webTarget
                .path(PRODUCT_API + "/" + PRODUCT_1_UUID)
                .request()
                .put(Entity.entity(aProductInput().build(), MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(HttpResponseStatus.NOT_FOUND.code()));
    }

    @Test
    void update_shouldReturn409_whenProductWithSameNameAlreadyExistInCategory() {
        inTransactionWithoutResult(() -> {
            final ProductCategory homeProductCategory = aHomeProductCategory().build();
            entityManager.persist(homeProductCategory);
            final ProductCategory electronicsProductCategory = anElectronicsProductCategory().build();
            entityManager.persist(electronicsProductCategory);
            entityManager.persist(aFirstHomeProduct().withCategory(homeProductCategory).build());
            entityManager.persist(aMonitorProduct().withCategory(electronicsProductCategory).withName("LG123").build());
        });

        Response response = webTarget
                .path(PRODUCT_API + "/" + PRODUCT_1_UUID)
                .request()
                .put(Entity.entity(aProductInput().withName("LG123").withCategory("ELECTRONICS").build(), MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(HttpResponseStatus.CONFLICT.code()));
    }
}
