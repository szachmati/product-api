package pl.wit.shop.product.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.wit.shop.product.domain.ProductService;
import pl.wit.shop.product.test.data.ProductTestDataIdentifiers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.wit.shop.product.api.ProductInputBuilder.aProductInput;
import static pl.wit.shop.product.api.ProductApi.ProductInput;
import static pl.wit.shop.product.domain.ProductRepository.ProductNotFoundException;
import static pl.wit.shop.product.domain.ProductCategoryRepository.ProductCategoryNotFoundException;
import static pl.wit.shop.product.domain.ProductService.ProductAlreadyExistsException;

@WebMvcTest(controllers = ProductApi.class)
class ProductApiTest implements ProductTestDataIdentifiers {

    private static final String PRODUCT_API = "/api/products";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void create_shouldReturn201() throws Exception {
        final ProductInput productInput = aProductInput().build();

        ResultActions resultActions = mockMvc.perform(post(PRODUCT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(productInput))
        );

        resultActions.andExpect(status().isCreated());
        then(productService).should().create(productInput.toDto());
    }

    @Test
    void create_shouldReturn404_whenProductCategoryNotExist() throws Exception {
        willThrow(ProductCategoryNotFoundException.class)
                .given(productService).create(any());

        mockMvc.perform(post(PRODUCT_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(aProductInput().build()))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturn409_whenProductWithGivenNameAlreadyExistsInCategory() throws Exception {
        willThrow(ProductAlreadyExistsException.class)
                .given(productService).create(any());

        mockMvc.perform(post(PRODUCT_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(aProductInput().build()))
                )
                .andExpect(status().isConflict());
    }

    @Test
    void delete_shouldReturn200() throws Exception {
        mockMvc.perform(delete(PRODUCT_API + "/" + PRODUCT_1_UUID))
                .andExpect(status().isOk());

        then(productService).should().delete(PRODUCT_1_UUID);
    }

    @Test
    void delete_shouldReturn404_whenProductNotExist() throws Exception {
        willThrow(ProductNotFoundException.class)
                .given(productService).delete(any());

        mockMvc.perform(delete(PRODUCT_API + "/" + PRODUCT_1_UUID))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllProductsInCategory_shouldReturn200() throws Exception {
        given(productService.findAllProductsInCategory(anyString(), any()))
                .willReturn(Page.empty());

        mockMvc.perform(get(PRODUCT_API)
                        .param("category", "HOME")
                        .param("sort", "HOME,desc")
                        .param("page", "2")
                        .param("size", "50")
                )
                .andExpect(status().isOk());

        then(productService).should()
                .findAllProductsInCategory("HOME", PageRequest.of(2, 50, Sort.by("HOME").descending()));
    }

    @Test
    void update_shouldReturn204() throws Exception {
        final ProductInput productInput = aProductInput().build();

        ResultActions resultActions = mockMvc.perform(put(PRODUCT_API + "/" + PRODUCT_1_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(productInput))
        );

        resultActions.andExpect(status().isNoContent());
        then(productService).should().update(PRODUCT_1_UUID, productInput.toDto());
    }

    @Test
    void update_shouldReturn404_whenProductNotExist() throws Exception {
        willThrow(ProductNotFoundException.class)
                .given(productService).update(any(), any());

        mockMvc.perform(put(PRODUCT_API + "/" + NOT_EXISTING_PRODUCT_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(aProductInput().build()))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void update_shouldReturn404_whenProductCategoryNotExist() throws Exception {
        willThrow(ProductCategoryNotFoundException.class)
                .given(productService).update(any(), any());

        mockMvc.perform(put(PRODUCT_API + "/" + NOT_EXISTING_PRODUCT_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(aProductInput().build()))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void update_shouldReturn409_whenProductWithGivenNameAlreadyExistsInCategory() throws Exception {
        willThrow(ProductAlreadyExistsException.class)
                .given(productService).update(any(), any());

        mockMvc.perform(put(PRODUCT_API + "/" + NOT_EXISTING_PRODUCT_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(aProductInput().build()))
                )
                .andExpect(status().isConflict());
    }

    @SneakyThrows
    private String asJson(Object o) {
        return objectMapper.writeValueAsString(o);
    }


}
