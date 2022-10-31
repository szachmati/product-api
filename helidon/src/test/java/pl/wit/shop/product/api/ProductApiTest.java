package pl.wit.shop.product.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.wit.shop.product.domain.ProductCategory;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static pl.wit.shop.product.api.ProductApiMatcher.ProductOutputMatcher.isProductOutput;
import static pl.wit.shop.product.domain.ProductBuilder.aFirstHomeProduct;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;

@HelidonTest
public class ProductApiTest implements ProductTestDataIdentifiers {

    private static final String PRODUCT_API = "/api/products";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private TransactionManager transactionManager;

    @Inject
    private WebTarget webTarget;

    @BeforeEach
    @Transactional
    void setUp() {
        entityManager
                .createNativeQuery("TRUNCATE TABLE product, product_category RESTART IDENTITY")
                .executeUpdate();
    }

    @Test
    void test() throws Exception {
        transactionManager.begin();
        final ProductCategory homeProductCategory = aHomeProductCategory().build();
        entityManager.persist(homeProductCategory);
        entityManager.persist(aFirstHomeProduct().withCategory(homeProductCategory).build());
        transactionManager.commit();

        Response response = webTarget
                .path(PRODUCT_API + "/" + PRODUCT_1_UUID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .buildGet()
                .invoke();

        String jsonResponse = response.readEntity(String.class);
        ProductApi.ProductOutput result = objectMapper.readValue(jsonResponse, ProductApi.ProductOutput.class);
        assertThat(response.getStatus(), is(HttpResponseStatus.OK.code()));
        assertThat(result, isProductOutput()
               .withUuid(PRODUCT_1_UUID)
               .withName("Home product")
               .withCategory("HOME")
        );
    }
}
