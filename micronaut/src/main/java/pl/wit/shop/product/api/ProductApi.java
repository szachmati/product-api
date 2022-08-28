package pl.wit.shop.product.api;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Status;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import pl.wit.shop.product.domain.ProductService;

import java.util.UUID;

@Controller("/api/products")
@RequiredArgsConstructor
public class ProductApi {

    @Inject
    private ProductService productService;

    @Delete("{uuid}")
    @Status(HttpStatus.OK)
    public void delete(@PathVariable UUID uuid) {
        productService.delete(uuid);
    }

}
