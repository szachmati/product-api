package pl.wit.shop.product.api;

import io.quarkus.panache.common.Sort;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.ResponseStatus;
import pl.wit.shop.product.domain.Product;
import pl.wit.shop.product.domain.ProductSaveDto;
import pl.wit.shop.product.domain.ProductService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Tag(name = "Product API")
@Path("/api/products")
@RequiredArgsConstructor
public class ProductApi {

    private final ProductService productService;

    @Operation(summary = "Create product")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Product was created successfully"),
            @APIResponse(responseCode = "409", description = "Product with given name already exists" +
                    "in category")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseStatus(201)
    public void create(
            @Parameter(description = "Product input data", required = true)
            @Valid ProductInput input
    ) {
        productService.create(input.toDto());
    }

    @Operation(summary = "Delete product")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Product was deleted successfully"),
            @APIResponse(responseCode = "404", description = "Product with given id was not found")
    })
    @DELETE
    @Path("{uuid}")
    @ResponseStatus(200)
    public void delete(
            @Parameter(description = "Product id", required = true)
            @PathParam("uuid") UUID uuid
    ) {
        productService.delete(uuid);
    }

    @Operation(summary = "Find all products in category")
    @APIResponse(
            responseCode = "200",
            description = "Products in given category",
            content = @Content(schema = @Schema(implementation = ProductOutput.class))
    )
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductOutput> findAllByCategoryName(
            @Parameter(description = "Product category", required = true)
            @QueryParam("category") String category,
            @Parameter(description = "Sort field", required = true)
            @QueryParam("sort") String sort,
            @Parameter(description = "Sort direction", required = true)
            @QueryParam("sortDir") String sortDirection,
            @Parameter(description = "Page number", required = true)
            @QueryParam("page") int page,
            @Parameter(description = "Page size", required = true)
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

    @Operation(summary = "Update product")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Product was updated successfully"),
            @APIResponse(responseCode = "404", description = "Product with given id was not found"),
            @APIResponse(responseCode = "409", description = "Product with given name already exists" +
                    "in category")
    })
    @PUT
    @Path("{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @ResponseStatus(200)
    public void update(
            @Parameter(description = "Product id", required = true)
            @PathParam("uuid") UUID uuid,
            @Valid ProductInput input
    ) {
        productService.update(uuid, input.toDto());
    }

    @Schema
    public record ProductInput(
            @Parameter(description = "Product name")
            @NotBlank String name,
            @Parameter(description = "Product category")
            @NotNull String category,
            @Parameter(description = "Product price")
            @NotNull BigDecimal price
    ) {
        ProductSaveDto toDto() {
            return new ProductSaveDto(name, category, price);
        }
    }

    @Value
    @Schema
    public static class ProductOutput {
        @Parameter(description = "Product id")
        UUID uuid;
        @Parameter(description = "Product category")
        String category;
        @Parameter(description = "Product name")
        String name;
        @Parameter(description = "Product price")
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
