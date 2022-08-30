package pl.wit.shop.product.api;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import pl.wit.shop.product.domain.Product;
import pl.wit.shop.product.domain.ProductSaveDto;
import pl.wit.shop.product.domain.ProductService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Controller("/api/products")
@RequiredArgsConstructor
public class ProductApi {

    @Inject
    private ProductService productService;

    @Post(consumes = MediaType.APPLICATION_JSON)
    @Status(HttpStatus.CREATED)
    public void create(@Body ProductInput productInput) {
        productService.create(productInput.toDto());
    }

    @Delete("{uuid}")
    @Status(HttpStatus.OK)
    public void delete(@PathVariable UUID uuid) {
        productService.delete(uuid);
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    @Status(HttpStatus.OK)
    public Page<ProductOutput> findAllByCategoryName(@QueryValue(value = "category", defaultValue = "HOME") String category, Pageable pageable) {
        return productService.findAllProductsInCategory(category, pageable)
                .map(ProductOutput::from);
    }


    public record ProductInput(@NotBlank String name, @NotNull String category, @NotNull BigDecimal price) {
        ProductSaveDto toDto() {
            return new ProductSaveDto(name, category, price);
        }
    }

    @Value
    public static class ProductOutput {
        UUID uuid;
        String category;
        String name;
        BigDecimal price;

        static ProductOutput from(Product product) {
            return new ProductOutput(
                    product.getUuid(),
                    product.getCategory().getName(),
                    product.getName(),
                    product.getPrice()
            );
        }
    }
}
