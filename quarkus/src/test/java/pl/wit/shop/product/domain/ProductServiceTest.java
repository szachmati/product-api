package pl.wit.shop.product.domain;

import io.quarkus.panache.common.Sort;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import javax.inject.Inject;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static pl.wit.shop.product.domain.ProductBuilder.aFirstHomeProduct;
import static pl.wit.shop.product.domain.ProductCategoryBuilder.aHomeProductCategory;
import static pl.wit.shop.product.domain.ProductCategoryMatcher.isProductCategory;
import static pl.wit.shop.product.domain.ProductMatcher.isProduct;
import static pl.wit.shop.product.domain.ProductSaveDtoBuilder.aProductSaveDto;
import static pl.wit.shop.product.domain.ProductCategoryRepository.ProductCategoryNotFoundException;
import static pl.wit.shop.product.domain.ProductService.ProductAlreadyExistsException;

@QuarkusTest
class ProductServiceTest implements ProductTestDataIdentifiers {

    @InjectMock
    ProductRepository productRepository;

    @InjectMock
    ProductCategoryRepository productCategoryRepository;

    @Inject
    ProductService productService;

    @Test
    void create_shouldPassParams() {
        given(productCategoryRepository.getByName(anyString()))
                .willReturn(aHomeProductCategory().build());
        given(productRepository.existsByNameAndCategoryName(anyString(), anyString()))
                .willReturn(false);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        productService.create(aProductSaveDto().build());

        then(productCategoryRepository).should().getByName("HOME");
        then(productRepository).should().existsByNameAndCategoryName("Home product", "HOME");
        then(productRepository).should().persistAndFlush(productCaptor.capture());
        assertThat(productCaptor.getValue(), isProduct()
                .withUuid(notNullValue())
                .withName("Home product")
                .withCategory(isProductCategory().withName("HOME"))
                .withPrice(BigDecimal.ONE)
        );
    }

    @Test
    void create_shouldThrowProductCategoryNotFoundException_whenProductCategoryNotExist() {
        given(productRepository.existsByNameAndCategoryName(anyString(), anyString()))
                .willReturn(false);
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
        final Product product = aFirstHomeProduct().build();
        given(productRepository.getByUuid(any())).willReturn(product);

        productService.delete(PRODUCT_1_UUID);

        then(productRepository).should().getByUuid(PRODUCT_1_UUID);
        then(productRepository).should().delete(product);
    }

    @Test
    void findAllByCategoryName_shouldPassParams() {
        final Sort sort = Sort.by("price").ascending();
        given(productRepository.findAllByCategoryName(any(), any(), anyInt(), anyInt()))
                .willReturn(List.of());

        List<Product> products =
                productService.findAllByCategoryName("HOME", sort, 1, 30);

        assertThat(products, empty());
        then(productRepository).should()
                .findAllByCategoryName("HOME", sort, 1, 30);
    }

}
