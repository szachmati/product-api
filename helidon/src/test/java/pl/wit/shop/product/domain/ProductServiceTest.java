package pl.wit.shop.product.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import java.math.BigDecimal;
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
    }


    @Test
    void delete_shouldPassParams() {
        given(productRepository.getByUuid(any()))
                .willReturn(aFirstHomeProduct().build());

        productService.delete(PRODUCT_1_UUID);

        assertThat(productRepository.findByUuid(PRODUCT_1_UUID), is(Optional.empty()));
    }

    @Test
    void delete_shouldThrowProductNotFoundException_whenProductNotExist() {
        given(productRepository.getByUuid(any()))
                .willThrow(ProductRepository.ProductNotFoundException.class);

        assertThrows(ProductRepository.ProductNotFoundException.class,
                () -> productService.delete(PRODUCT_1_UUID));
    }
}
