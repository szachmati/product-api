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

@ExtendWith(MockitoExtension.class)
class ProductServiceTest implements ProductTestDataIdentifiers {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void create_shouldCreateProduct() {
        given(productCategoryRepository.getByName(anyString()))
                .willReturn(aHomeProductCategory().build());
        given(productRepository.save(any()))
                .willReturn(aHomeProduct().build());

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
    void create_shouldThrowProductCategoryNotFoundException_whenProductCategoryWasNotFound() {
        willThrow(ProductCategoryNotFoundException.class)
                .given(productCategoryRepository).getByName(anyString());

        assertThrows(ProductCategoryNotFoundException.class,
                () -> productService.create(aProductSaveDto().build())
        );
    }
}
