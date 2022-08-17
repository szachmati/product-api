package pl.wit.shop.product.api;

import io.quarkus.panache.common.Sort;
import lombok.Value;
import pl.wit.shop.product.domain.Product;
import pl.wit.shop.product.domain.ProductSaveDto;
import pl.wit.shop.product.domain.ProductService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Path("/api/products")
public class ProductApi {

    @Inject
    ProductService productService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid ProductInput input) {
        productService.create(input.toDto());
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductOutput> findAllByCategoryName(
            @QueryParam("category") String category,
            @QueryParam("sort") String sort,
            @QueryParam("sortDir") String sortDirection,
            @QueryParam("page") int page,
            @QueryParam("size") int size

    ) {
        Sort.Direction direction = Sort.Direction.Ascending;
        if ("desc".equalsIgnoreCase(sortDirection)) {
            direction = Sort.Direction.Descending;
        }
        return productService
                .findAllByCategoryName(category, Sort.by(sort).direction(direction), page, size)
                .stream()
                .map(ProductOutput::from)
                .toList();
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
