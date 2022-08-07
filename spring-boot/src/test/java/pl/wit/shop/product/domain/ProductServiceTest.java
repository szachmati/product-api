package pl.wit.shop.product.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;


import java.math.BigDecimal;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static pl.wit.shop.product.domain.ProductBuilder.aHomeProduct;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryMatcher.isProductCategory;
import static pl.wit.shop.product.domain.ProductMatcher.isProduct;
import static pl.wit.shop.product.domain.ProductSaveDtoBuilder.aProductSaveDto;
import static pl.wit.shop.product.domain.ProductCategoryRepository.ProductCategoryNotFoundException;
import static pl.wit.shop.product.domain.ProductService.ProductAlreadyExistsException;
import static pl.wit.shop.product.domain.ProductRepository.ProductNotFoundException;

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
        willThrow(ProductCategoryNotFoundException.class)
                .given(productCategoryRepository).getByName(anyString());

        assertThrows(ProductCategoryNotFoundException.class,
                () -> productService.create(aProductSaveDto().build())
        );
    }

    @Test
    void create_shouldThrowProductAlreadyExistsException_whenProductWithGivenNameAlreadyExistsInCategory() {
        willThrow(ProductAlreadyExistsException.class)
                .given(productRepository).existsByNameAndCategoryName(anyString(), anyString());

        assertThrows(ProductAlreadyExistsException.class,
                () -> productService.create(aProductSaveDto().build())
        );
    }

    @Test
    void delete_shouldPassParams() {
        final Product product = aHomeProduct().build();
        given(productRepository.getByUuid(any())).willReturn(product);

        productService.delete(PRODUCT_1_UUID);

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
    void update_shouldPassParams() {
        given(productRepository.getByUuid(any()))
                .willReturn(aHomeProduct().build());
        given(productCategoryRepository.getByName(anyString()))
                .willReturn(aHomeProductCategory().build());

        productService.update(PRODUCT_1_UUID,
                new ProductSaveDto("Product no 1", "HOME", BigDecimal.TEN)
        );

        then(productCategoryRepository).should().getByName("HOME");
        then(productRepository).should().getByUuid(PRODUCT_1_UUID);
    }

    @Test
    void update_shouldThrowProductNotFoundException_whenProductNotExist() {
        willThrow(ProductNotFoundException.class)
                .given(productRepository).getByUuid(any());

        assertThrows(ProductNotFoundException.class,
                () -> productService.update(NOT_EXISTING_PRODUCT_UUID, aProductSaveDto().build())
        );
    }

    @Test
    void update_shouldThrowProductCategoryNotFoundException_whenProductCategoryNotExist() {
        given(productRepository.getByUuid(any()))
                .willReturn(aHomeProduct().build());
        willThrow(ProductCategoryNotFoundException.class)
                .given(productCategoryRepository).getByName(any());

        assertThrows(ProductCategoryNotFoundException.class,
                () -> productService.update(PRODUCT_1_UUID, aProductSaveDto().build())
        );
    }

    @Test
    void update_shouldThrowProductAlreadyExistsException_whenProductWithGivenNameAlreadyExistsInCategory() {
        given(productRepository.getByUuid(any()))
                .willReturn(aHomeProduct().build());
        given(productCategoryRepository.getByName(anyString()))
                .willReturn(aHomeProductCategory().build());
        willThrow(ProductAlreadyExistsException.class)
                .given(productRepository).existsByNameAndCategoryName(anyString(), anyString());

        assertThrows(ProductAlreadyExistsException.class,
                () -> productService.update(PRODUCT_1_UUID, aProductSaveDto().build())
        );
    }
}
