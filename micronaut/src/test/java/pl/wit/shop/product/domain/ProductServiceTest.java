package pl.wit.shop.product.domain;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import java.math.BigDecimal;

import static com.spotify.hamcrest.pojo.IsPojo.pojo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static pl.wit.shop.product.domain.ProductBuilder.aFirstHomeProduct;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;
import static pl.wit.shop.product.domain.ProductRepository.ProductNotFoundException;
import static pl.wit.shop.product.domain.ProductSaveDtoBuilder.aProductSaveDto;

@MicronautTest
class ProductServiceTest implements ProductTestDataIdentifiers {

    @MockBean(ProductCategoryRepository.class)
    final ProductCategoryRepository productCategoryRepository = mock(ProductCategoryRepository.class);
    @MockBean(ProductRepository.class)
    final ProductRepository productRepository = mock(ProductRepository.class);
    @Inject
    private ProductService productService;

    @Test
    void create_shouldPassParams() {
        given(productCategoryRepository.getByName(any()))
                .willReturn(aHomeProductCategory().build());
        given(productRepository.existsByNameAndCategoryName(any(), any()))
                .willReturn(false);
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

        productService.create(aProductSaveDto().build());

        then(productCategoryRepository).should().getByName("HOME");
        then(productRepository).should().existsByNameAndCategoryName("Home product", "HOME");
        then(productRepository).should().save(productArgumentCaptor.capture());
        assertThat(productArgumentCaptor.getValue(), pojo(Product.class)
                .withProperty("name", is("Home product"))
                .withProperty("price", is(BigDecimal.ONE))
                .withProperty("uuid", notNullValue())
                .where("getCategory", pojo(ProductCategory.class)
                        .withProperty("name", is("HOME"))
                )
        );
    }

    @Test
    void create_shouldThrowProductCategoryNotFoundException_whenProductCategoryNotExist() {
        willThrow(ProductCategoryRepository.ProductCategoryNotFoundException.class)
                .given(productCategoryRepository).getByName(anyString());

        assertThrows(ProductCategoryRepository.ProductCategoryNotFoundException.class,
                () -> productService.create(aProductSaveDto().build())
        );
    }

    @Test
    void create_shouldThrowProductAlreadyExistsException_whenProductWithGivenNameAlreadyExistsInCategory() {
        willThrow(ProductService.ProductAlreadyExistsException.class)
                .given(productRepository).existsByNameAndCategoryName(anyString(), anyString());

        assertThrows(ProductService.ProductAlreadyExistsException.class,
                () -> productService.create(aProductSaveDto().build())
        );
    }


    @Test
    void delete_shouldPassParams() {
        final Product product = aFirstHomeProduct().build();
        given(productRepository.getByUuid(any())).willReturn(product);

        productService.delete(PRODUCT_1_UUID);

        then(productRepository).should().getByUuid(PRODUCT_1_UUID);
        then(productRepository).should().delete(product);
    }

    @Test
    void delete_shouldThrowProductNotFoundException_whenProductNotExist() {
        willThrow(ProductNotFoundException.class)
                .given(productRepository).getByUuid(any());

        assertThrows(ProductNotFoundException.class,
                () -> productService.delete(NOT_EXISTING_PRODUCT_UUID)
        );
    }

    @Test
    void findAllProductsInCategory_shouldPassParams() {
        given(productRepository.findAllProductsInCategory(any(), any()))
                .willReturn(Page.empty());

        Page<Product> result = productService.findAllProductsInCategory("HOME", Pageable.from(0, 5));

        assertThat(result.getContent(), Matchers.empty());
        then(productRepository).should().findAllProductsInCategory("HOME", Pageable.from(0, 5));
    }

    @Test
    void getProduct_shouldPassParams() {
        given(productRepository.getByUuid(any()))
                .willReturn(null);

        Product product = productService.getProduct(PRODUCT_1_UUID);

        assertThat(product, nullValue());
        then(productRepository).should().getByUuid(PRODUCT_1_UUID);
    }

    @Test
    void getProduct_shouldThrowProductNotFoundException_whenProductNotExist() {
        willThrow(ProductNotFoundException.class)
                .given(productRepository).getByUuid(any());

        assertThrows(ProductNotFoundException.class,
                () -> productService.getProduct(PRODUCT_1_UUID)
        );
    }

    @Test
    void update_shouldPassParams() {
        given(productRepository.getByUuid(any()))
                .willReturn(aFirstHomeProduct().build());
        given(productCategoryRepository.getByName(anyString()))
                .willReturn(aHomeProductCategory().build());
        given(productRepository.existsByNameAndCategoryName(anyString(), anyString()))
                .willReturn(false);

        productService.update(PRODUCT_1_UUID, aProductSaveDto()
                .withCategory("HOME")
                .withName("New name")
                .withPrice(new BigDecimal("32.5"))
                .build()
        );

        then(productRepository).should().getByUuid(PRODUCT_1_UUID);
        then(productCategoryRepository).should().getByName("HOME");
        then(productRepository).should().existsByNameAndCategoryName("New name", "HOME");
    }

    @Test
    void update_shouldThrowProductNotFoundException_whenProductNotExist() {
        willThrow(ProductNotFoundException.class)
                .given(productRepository).getByUuid(any());

        assertThrows(ProductNotFoundException.class,
                () -> productService.update(PRODUCT_1_UUID, aProductSaveDto().build())
        );
    }

    @Test
    void update_shouldThrowProductCategoryNotFoundException_whenProductCategoryNotExist() {
        given(productRepository.getByUuid(any()))
                .willReturn(aFirstHomeProduct().build());
        willThrow(ProductCategoryRepository.ProductCategoryNotFoundException.class)
                .given(productCategoryRepository).getByName(anyString());

        assertThrows(ProductCategoryRepository.ProductCategoryNotFoundException.class,
                () -> productService.update(PRODUCT_1_UUID, aProductSaveDto().build())
        );
    }

    @Test
    void update_shouldThrowProductAlreadyExistsException_whenProductWithGivenNameAlreadyExistsInCategory() {
        given(productRepository.getByUuid(any()))
                .willReturn(aFirstHomeProduct().build());
        given(productCategoryRepository.getByName(anyString()))
                .willReturn(aHomeProductCategory().build());
        given(productRepository.existsByNameAndCategoryName(anyString(), anyString()))
                .willReturn(true);

        assertThrows(ProductService.ProductAlreadyExistsException.class,
                () -> productService.update(PRODUCT_1_UUID, aProductSaveDto()
                        .withCategory("HOME")
                        .withName("New name")
                        .withPrice(new BigDecimal("32.5"))
                        .build()
                )
        );
    }
}
