package pl.wit.shop.product.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import pl.wit.shop.product.domain.ProductCategory;
import pl.wit.shop.product.domain.ProductCategoryRepository;
import pl.wit.shop.product.domain.ProductRepository;
import pl.wit.shop.product.test.base.BaseIntegrationTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static pl.wit.shop.product.api.ProductInputBuilder.aProductInput;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;

public class ProductApiIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper testObjectMapper;

    @Test
    void create_shouldCreateProduct() {
        final ProductCategory homeProductCategory =
                productCategoryRepository.save(aHomeProductCategory().build());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(asJson(aProductInput().build()), headers);

        ResponseEntity<String> responseEntity =
                testRestTemplate.postForEntity("/api/products", httpEntity, String.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.CREATED));
        //TODO need to be fixed
//        assertThat(productRepository.findAll(), contains(
//                isProduct()
//                        .withUuid(notNullValue())
//                        .withCategory(homeProductCategory)
//                        .withName("Home product")
//                        .withPrice(BigDecimal.ONE)
//        ));
    }

    @SneakyThrows
    private String asJson(Object o) {
        return testObjectMapper.writeValueAsString(o);
    }

}
