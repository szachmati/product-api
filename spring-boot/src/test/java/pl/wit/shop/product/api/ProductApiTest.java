package pl.wit.shop.product.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.wit.shop.product.domain.ProductCategoryRepository;
import pl.wit.shop.product.domain.ProductService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.wit.shop.product.api.ProductInputBuilder.aProductInput;


@WebMvcTest(controllers = ProductController.class)
class ProductApiTest {

    private static final String PRODUCT_API = "/api/products";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void create_shouldReturn201() throws Exception {
        mockMvc.perform(post(PRODUCT_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(aProductInput().build()))
                )
                .andExpect(status().isCreated());
    }

    @Test
    void create_shouldReturn404_whenProductCategoryNotExist() throws Exception {
        willThrow(ProductCategoryRepository.ProductCategoryNotFoundException.class)
                .given(productService).create(any());

        mockMvc.perform(post(PRODUCT_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(aProductInput().build()))
                )
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    private String asJson(Object o) {
        return objectMapper.writeValueAsString(o);
    }


}
