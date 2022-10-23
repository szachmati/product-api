package pl.wit.shop.product.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static pl.wit.shop.product.domain.ProductBuilder.aFirstHomeProduct;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest implements ProductTestDataIdentifiers {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

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
