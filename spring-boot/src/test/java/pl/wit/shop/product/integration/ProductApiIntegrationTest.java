package pl.wit.shop.product.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import pl.wit.shop.product.domain.ProductCategoryRepository;
import pl.wit.shop.product.domain.ProductRepository;
import pl.wit.shop.product.test.base.BaseIntegrationTest;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;
import pl.wit.shop.product.test.utils.RestPageResponse;

import java.math.BigDecimal;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static pl.wit.shop.product.api.ProductApi.ProductInput;
import static pl.wit.shop.product.api.ProductApi.ProductOutput;
import static pl.wit.shop.product.api.ProductApiMatcher.ProductOutputMatcher.isProductOutput;
import static pl.wit.shop.product.api.ProductInputBuilder.aProductInput;
import static pl.wit.shop.product.domain.ProductBuilder.aFirstHomeProduct;
import static pl.wit.shop.product.domain.ProductBuilder.aSecondHomeProduct;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.anElectronicsProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryMatcher.isProductCategory;
import static pl.wit.shop.product.domain.ProductMatcher.isProduct;

class ProductApiIntegrationTest extends BaseIntegrationTest implements ProductTestDataIdentifiers {

    private static final String PRODUCT_API = "/api/products";

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void create_shouldCreateProduct() {
        productCategoryRepository.save(aHomeProductCategory().build());

        ResponseEntity<String> responseEntity =
                post(PRODUCT_API, aProductInput().build(), String.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.CREATED));
        transactionOperations.executeWithoutResult(transactionStatus ->
                assertThat(productRepository.findAll(), contains(
                        isProduct()
                                .withUuid(notNullValue())
                                .withCategory(isProductCategory().withName("HOME"))
                                .withName("Home product")
                                .withPrice(new BigDecimal("1.00"))
                ))
        );
    }

    @Test
    void delete_shouldDeleteProduct() {
        productCategoryRepository.save(aHomeProductCategory().build());
        productRepository.save(aFirstHomeProduct().build());

        ResponseEntity<String> responseEntity = delete(PRODUCT_API + "/" + PRODUCT_1_UUID);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(productRepository.findByUuid(PRODUCT_1_UUID), is(Optional.empty()));
    }

    @Test
    void findAllProductsInCategory_shouldReturnHomeProductsPage_forDefaultPageParams() {
        productCategoryRepository.save(aHomeProductCategory().build());
        productRepository.save(aFirstHomeProduct().build());
        productRepository.save(aSecondHomeProduct().build());

        ParameterizedTypeReference<RestPageResponse<ProductOutput>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<RestPageResponse<ProductOutput>> responseEntity =
                get(PRODUCT_API + "?category={category}", responseType, "HOME");

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        RestPageResponse<ProductOutput> resultPage = requireNonNull(responseEntity.getBody());
        assertThat(resultPage.getNumber(), is(0));
        assertThat(resultPage.getTotalPages(), is(1));
        assertThat(resultPage.getSize(), is(20));
        assertThat(resultPage.getTotalElements(), is(2L));
        assertThat(responseEntity.getBody().getContent(), contains(
                isProductOutput()
                        .withUuid(PRODUCT_1_UUID)
                        .withName("Home product")
                        .withCategory("HOME")
                        .withPrice(new BigDecimal("1.00")),
                isProductOutput()
                        .withUuid(PRODUCT_2_UUID)
                        .withName("Second home product")
                        .withCategory("HOME")
                        .withPrice(new BigDecimal("30.87"))
        ));
    }

    @Test
    void findAllProductsInCategory_shouldReturnHomeProductsPage_forCustomPageParams() {
        productCategoryRepository.save(aHomeProductCategory().build());
        productRepository.save(aFirstHomeProduct().build());
        productRepository.save(aSecondHomeProduct().build());

        ParameterizedTypeReference<RestPageResponse<ProductOutput>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<RestPageResponse<ProductOutput>> responseEntity = get(
                UriComponentsBuilder
                        .fromPath(PRODUCT_API)
                        .queryParam("category", "HOME")
                        .queryParam("page", "0")
                        .queryParam("size", "5")
                        .queryParam("sort", "price,DESC")
                        .build()
                        .toUriString(),
                responseType
        );

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        RestPageResponse<ProductOutput> resultPage = requireNonNull(responseEntity.getBody());
        assertThat(resultPage.getNumber(), is(0));
        assertThat(resultPage.getTotalPages(), is(1));
        assertThat(resultPage.getSize(), is(5));
        assertThat(resultPage.getTotalElements(), is(2L));
        assertThat(responseEntity.getBody(), contains(
                isProductOutput()
                        .withUuid(PRODUCT_2_UUID)
                        .withName("Second home product")
                        .withCategory("HOME")
                        .withPrice(new BigDecimal("30.87")),
                isProductOutput()
                        .withUuid(PRODUCT_1_UUID)
                        .withName("Home product")
                        .withCategory("HOME")
                        .withPrice(new BigDecimal("1.00"))
        ));

    }

    @Test
    void update_shouldUpdateProduct() {
        productCategoryRepository.save(aHomeProductCategory().build());
        productCategoryRepository.save(anElectronicsProductCategory().build());
        productRepository.save(aFirstHomeProduct().build());
        final ProductInput requestBody = aProductInput()
                .withName("Samsung s10")
                .withCategory("ELECTRONICS")
                .withPrice(new BigDecimal("300.99"))
                .build();

        ResponseEntity<String> responseEntity =
                put(PRODUCT_API + "/" + PRODUCT_1_UUID, requestBody, String.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.NO_CONTENT));
        transactionOperations.executeWithoutResult(transactionStatus -> {
            assertThat(productRepository.getByUuid(PRODUCT_1_UUID), isProduct()
                    .withUuid(PRODUCT_1_UUID)
                    .withCategory(isProductCategory().withName("ELECTRONICS"))
                    .withName("Samsung s10")
                    .withPrice(new BigDecimal("300.99"))
            );
        });
    }
}
