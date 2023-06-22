package pl.wit.shop.product.domain;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wit.shop.common.repository.Pageable;
import pl.wit.shop.common.repository.Sort;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static pl.wit.shop.product.domain.ProductBuilder.aFirstHomeProduct;
import static org.hamcrest.Matchers.is;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryMatcher.isProductCategory;
import static pl.wit.shop.product.domain.ProductMatcher.isProduct;
import static pl.wit.shop.product.domain.ProductSaveDtoBuilder.aProductSaveDto;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest implements ProductTestDataIdentifiers {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void create_shouldPassParams() {
        given(productCategoryRepository.getByName(anyString()))
                .willReturn(aHomeProductCategory().build());

        productService.create(aProductSaveDto().build());

        then(productCategoryRepository).should().getByName("HOME");
        then(productRepository).should().save(argThat(
                isProduct()
                        .withUuid(notNullValue())
                        .withCategory(
                                isProductCategory()
                                        .withName("HOME")
                        )
                        .withName("Home product")
                        .withPrice(BigDecimal.ONE)
        ));
    }

    @Test
    void create_shouldThrowProductCategoryNotFoundException_whenProductCategoryNotExist() {
        willThrow(ProductCategoryRepository.ProductCategoryNotFoundException.class)
                .given(productCategoryRepository).getByName(anyString());

        assertThrows(ProductCategoryRepository.ProductCategoryNotFoundException.class,
                () -> productService.create(aProductSaveDto().build())
        );
        then(productCategoryRepository).should().getByName("HOME");
    }

    @Test
    void create_shouldThrowProductAlreadyExistsException_whenProductWithGivenNameAlreadyExistsInCategory() {
        given(productCategoryRepository.getByName(anyString()))
                .willReturn(aHomeProductCategory().build());
        willThrow(ProductService.ProductAlreadyExistsException.class)
                .given(productRepository).existsByNameAndCategoryName(anyString(), anyString());

        assertThrows(ProductService.ProductAlreadyExistsException.class,
                () -> productService.create(aProductSaveDto().build())
        );
        then(productCategoryRepository).should().getByName("HOME");
        then(productRepository).should().existsByNameAndCategoryName("Home product", "HOME");
    }


    @Test
    void delete_shouldPassParams() {
        final Product product = aFirstHomeProduct().build();
        given(productRepository.getByUuid(any()))
                .willReturn(product);

        productService.delete(PRODUCT_1_UUID);

        assertThat(productRepository.findByUuid(PRODUCT_1_UUID), is(Optional.empty()));
        then(productRepository).should().delete(product);
    }

    @Test
    void delete_shouldThrowProductNotFoundException_whenProductNotExist() {
        given(productRepository.getByUuid(any()))
                .willThrow(ProductRepository.ProductNotFoundException.class);

        assertThrows(ProductRepository.ProductNotFoundException.class,
                () -> productService.delete(PRODUCT_1_UUID));
        then(productRepository).should().getByUuid(PRODUCT_1_UUID);
    }

    @Test
    void findAllProductsInCategory_shouldPassParams() {
        given(productRepository.findAllProductsInCategory(anyString(), any())).willReturn(List.of());
        final Pageable pageable = new Pageable(Sort.DESC, "name", 5, 20);

        List<Product> result = productService.findAllProductsInCategory("HOME", pageable);

        assertThat(result, Matchers.empty());
        then(productRepository).should().findAllProductsInCategory("HOME", pageable);
    }

    @Test
    void getProduct_shouldPassParams() {
        given(productRepository.getByUuid(any()))
                .willReturn(aFirstHomeProduct().build());

        Product product = productService.getProduct(PRODUCT_1_UUID);

        assertThat(product.getUuid(), is(PRODUCT_1_UUID));
        then(productRepository).should().getByUuid(PRODUCT_1_UUID);
    }

    @Test
    void getProduct_shouldThrowProductNotFoundException_whenProductNotExist() {
        willThrow(ProductRepository.ProductNotFoundException.class)
                .given(productRepository).getByUuid(any());

        assertThrows(ProductRepository.ProductNotFoundException.class,
                () -> productService.getProduct(PRODUCT_1_UUID)
        );
        then(productRepository).should().getByUuid(PRODUCT_1_UUID);
    }

    @Test
    void update_shouldPassParams() {
        given(productRepository.getByUuid(any()))
                .willReturn(aFirstHomeProduct().build());
        given(productCategoryRepository.getByName(anyString()))
                .willReturn(aHomeProductCategory().build());
        given(productRepository.existsByNameAndCategoryName(anyString(), anyString()))
                .willReturn(false);

        productService.update(PRODUCT_1_UUID, aProductSaveDto().withName("product1").build());

        then(productRepository).should().getByUuid(PRODUCT_1_UUID);
        then(productCategoryRepository).should().getByName("HOME");
        then(productRepository).should().existsByNameAndCategoryName("product1", "HOME");
    }

    @Test
    void update_shouldThrowProductNotFoundException_whenProductNotExist() {
        willThrow(ProductRepository.ProductNotFoundException.class)
                .given(productRepository).getByUuid(any());

        assertThrows(ProductRepository.ProductNotFoundException.class,
                () -> productService.update(NOT_EXISTING_PRODUCT_UUID, aProductSaveDto().build())
        );
        then(productRepository).should().getByUuid(NOT_EXISTING_PRODUCT_UUID);
    }

    @Test
    void update_shouldThrowProductNotFoundException_whenProductCategoryNotExist() {
        given(productRepository.getByUuid(any()))
                .willReturn(aFirstHomeProduct().build());
        willThrow(ProductCategoryRepository.ProductCategoryNotFoundException.class)
                .given(productCategoryRepository).getByName(anyString());

        assertThrows(ProductCategoryRepository.ProductCategoryNotFoundException.class,
                () -> productService.update(PRODUCT_1_UUID, aProductSaveDto().build())
        );
        then(productRepository).should().getByUuid(PRODUCT_1_UUID);
        then(productCategoryRepository).should().getByName("HOME");
    }

    @Test
    void update_shouldThrowProductAlreadyExistsException_whenProductWithGivenNameAlreadyExistInCategory() {
        given(productRepository.getByUuid(any()))
                .willReturn(aFirstHomeProduct().build());
        given(productCategoryRepository.getByName(anyString()))
                .willReturn(aHomeProductCategory().build());
        given(productRepository.existsByNameAndCategoryName(anyString(), anyString()))
                .willReturn(true);

       assertThrows(ProductService.ProductAlreadyExistsException.class,
               () ->  productService.update(PRODUCT_1_UUID, aProductSaveDto().withName("product1").build())
       );
       then(productRepository).should().getByUuid(PRODUCT_1_UUID);
       then(productCategoryRepository).should().getByName("HOME");
       then(productRepository).should().existsByNameAndCategoryName("product1", "HOME");
    }
}
