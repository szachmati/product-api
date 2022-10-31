package pl.wit.shop.product.api;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import pl.wit.shop.product.domain.Product;
import pl.wit.shop.product.domain.ProductSaveDto;
import pl.wit.shop.product.domain.ProductService;

import java.math.BigDecimal;
import java.util.UUID;

@Path("/api/products")
@RequestScoped
public class ProductApi {

    private final ProductService productService;

    @Inject
    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    @GET
    @Path("/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductOutput getProduct(@PathParam("uuid") UUID uuid) {
        return ProductOutput.from(productService.getProduct(uuid));
    }


    public record ProductInput(@NotBlank String name, @NotNull String category, @NotNull BigDecimal price) {
        ProductSaveDto toDto() {
            return new ProductSaveDto(name, category, price);
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ProductOutput {
        private UUID uuid;
        private String name;
        private String category;
        public static ProductOutput from(Product product) {
            return new ProductOutput(product.getUuid(), product.getName(), product.getCategory().getName());
        }
    }
}
