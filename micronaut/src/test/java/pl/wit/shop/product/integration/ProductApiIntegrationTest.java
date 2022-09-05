package pl.wit.shop.product.integration;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Sort;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.TransactionOperations;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.wit.shop.product.api.ProductApi;
import pl.wit.shop.product.domain.Product;
import pl.wit.shop.product.domain.ProductCategory;
import pl.wit.shop.product.domain.ProductCategoryRepository;
import pl.wit.shop.product.domain.ProductRepository;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static pl.wit.shop.product.api.ProductInputBuilder.aProductInput;
import static pl.wit.shop.product.domain.ProductBuilder.aFirstHomeProduct;
import static pl.wit.shop.product.domain.ProductBuilder.aSecondHomeProduct;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.anElectronicsProductCategory;
import static pl.wit.shop.product.api.ProductApi.ProductInput;
import static pl.wit.shop.product.api.ProductApi.ProductOutput;

@MicronautTest(transactional = false)
class ProductApiIntegrationTest implements ProductTestDataIdentifiers {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProductCategoryRepository productCategoryRepository;

    @Inject
    private TransactionOperations<?> transactionOperations;

    @Inject
    @Client("/api/products")
    private HttpClient httpClient;

    @BeforeEach
    void init() {
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();
    }

    @Test
    void create_shouldCreateProduct() {
        productCategoryRepository.save(aHomeProductCategory().build());
        HttpRequest<ProductApi.ProductInput> request = HttpRequest.POST("", aProductInput().build());

        HttpResponse<?> response = httpClient.toBlocking().exchange(request);

        assertThat(response.status(), is(HttpStatus.CREATED));
        transactionOperations.executeRead(status -> {
            assertThat(productRepository.findAll(), contains(
                    pojo(Product.class)
                            .withProperty("name", is("Home product"))
                            .withProperty("price", is(new BigDecimal("1.00")))
                            .where("getCategory",
                                    pojo(ProductCategory.class)
                                            .withProperty("name", is("HOME"))
                            )
            ));
            return status;
        });

    }

    @Test
    void delete_shouldDeleteProduct() {
        final ProductCategory homeProductCategory = productCategoryRepository.save(aHomeProductCategory().build());
        productRepository.save(aFirstHomeProduct().withCategory(homeProductCategory).build());
        HttpRequest<String> request = HttpRequest.DELETE("/" + PRODUCT_1_UUID);

        HttpResponse<?> response = httpClient.toBlocking().exchange(request);

        assertThat(response.status(), is(HttpStatus.OK));
        assertThat(productRepository.findByUuid(PRODUCT_1_UUID), is(Optional.empty()));
    }

    @Test
    void findAllProductsInCategory_shouldReturnHomeProductsPage() {
        final ProductCategory homeProductCategory = productCategoryRepository.save(aHomeProductCategory().build());
        productRepository.save(aFirstHomeProduct().withCategory(homeProductCategory).build());
        productRepository.save(aSecondHomeProduct().withCategory(homeProductCategory).build());
        HttpRequest<String> request = HttpRequest.GET(
                UriBuilder.of("")
                        .queryParam("category", "HOME")
                        .queryParam("sort", "price,desc")
                        .queryParam("page", "0")
                        .queryParam("size", "8")
                        .build()
        );

        HttpResponse<Page> response = httpClient.toBlocking().exchange(request, Page.class);

        assertThat(response.status(), is(HttpStatus.OK));
        Page result = Objects.requireNonNull(response.body());
        assertThat(result.getPageNumber(), is(0));
        assertThat(result.getTotalSize(), is(2L));
        assertThat(result.getSize(), is(8));
        assertThat(result.getSort().getOrderBy().get(0), is(Sort.Order.desc("price")));
        List<ProductOutput> content = result.getContent().stream().map(this::mapToProductOutput).toList();
        assertThat(content, contains(
                pojo(ProductOutput.class)
                        .withProperty("uuid", is(PRODUCT_2_UUID))
                        .withProperty("name", is("Second home product"))
                        .withProperty("price", is(new BigDecimal("30.87")))
                        .withProperty("category", is("HOME")),
                pojo(ProductOutput.class)
                        .withProperty("uuid", is(PRODUCT_1_UUID))
                        .withProperty("name", is("Home product"))
                        .withProperty("price", is(new BigDecimal("1.0"))) //TODO
                        .withProperty("category", is("HOME"))
        ));
    }

    @Test
    void update_shouldUpdateProduct() {
        final ProductCategory homeProductCategory = productCategoryRepository.save(aHomeProductCategory().build());
        productCategoryRepository.save(anElectronicsProductCategory().build());
        productRepository.save(aFirstHomeProduct().withCategory(homeProductCategory).build());
        HttpRequest<ProductInput> request = HttpRequest.PUT(
                "/" + PRODUCT_1_UUID,
                aProductInput()
                        .withCategory("ELECTRONICS")
                        .withName("Samsung s21")
                        .withPrice(new BigDecimal("1210.22"))
                        .build()
        );


        HttpResponse<?> response = httpClient.toBlocking().exchange(request);

        assertThat(response.status(), is(HttpStatus.OK));
        transactionOperations.executeRead(status -> {
            assertThat(productRepository.getByUuid(PRODUCT_1_UUID),
                    pojo(Product.class)
                            .withProperty("name", is("Samsung s21"))
                            .withProperty("price", is(new BigDecimal("1210.22")))
                            .where("getCategory",
                                    pojo(ProductCategory.class)
                                            .withProperty("name", is("ELECTRONICS"))
                            )
            );
            return status;
        });
    }

    private ProductOutput mapToProductOutput(Object o) {
        UUID uuid = UUID.fromString(((LinkedHashMap) o).get("uuid").toString());
        String category = ((LinkedHashMap) o).get("category").toString();
        String name = ((LinkedHashMap) o).get("name").toString();
        BigDecimal price = new BigDecimal(((LinkedHashMap) o).get("price").toString());
        return new ProductApi.ProductOutput(uuid, category, name, price);
    }
}
