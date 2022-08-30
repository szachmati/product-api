package pl.wit.shop.product.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static pl.wit.shop.product.domain.ProductBuilder.aFirstHomeProduct;
import static pl.wit.shop.product.domain.ProductRepository.ProductNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest implements ProductTestDataIdentifiers {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;


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

}