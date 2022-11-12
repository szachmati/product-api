package pl.wit.shop.product.api;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import pl.wit.shop.common.repository.Pageable;
import pl.wit.shop.common.repository.Sort;
import pl.wit.shop.product.domain.Product;
import pl.wit.shop.product.domain.ProductSaveDto;
import pl.wit.shop.product.domain.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Tag(name = "Product API")
@Path("/api/products")
@RequestScoped
public class ProductApi {

    private final ProductService productService;

    @Inject
    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create product")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Product was created successfully"),
            @APIResponse(responseCode = "404", description = "Product category was not found"),
            @APIResponse(responseCode = "409", description = "Product with given name already exists" +
                    "in category")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(
            @RequestBody(
                    description = "Product input data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductInput.class))
            )
            @Valid ProductInput input
    ) {
        productService.create(input.toDto());
        return Response.status(Response.Status.CREATED).build();
    }

    @Operation(summary = "Delete product")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Product was deleted successfully"),
            @APIResponse(responseCode = "404", description = "Product with given id was not found")
    })
    @DELETE
    @Path("{uuid}")
    public Response delete(
            @Parameter(description = "Product id", required = true)
            @PathParam("uuid") UUID uuid
    ) {
        productService.delete(uuid);
        return Response.ok().build();
    }

    @Operation(summary = "Find all products in category")
    @APIResponse(
            responseCode = "200",
            description = "Products in given category",
            content = @Content(schema = @Schema(implementation = ProductOutput.class))
    )
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductOutput> findAllProductsInCategory(
            @Parameter(description = "Product category", required = true,
                    content = @Content(schema = @Schema(enumeration = {
                            "HOME", "ELECTRONICS", "CARS", "FOOD", "FURNITURE",
                            "MOBILE PHONES", "FASHION", "MUSIC", "SPORT", "CHILD", "HEALTH"
                    })))
            @QueryParam("category") String category,
            @Parameter(description = "Sort field", required = true)
            @QueryParam("sort") Sort sort,
            @Parameter(description = "Sort field", required = true,
                    content = @Content(schema = @Schema(enumeration = {
                            "name", "category", "price"
                    })))
            @QueryParam("sortField") String sortField,
            @Parameter(description = "Page number", required = true, example = "5")
            @QueryParam("page") String page,
            @Parameter(description = "Page size", required = true, example = "20")
            @QueryParam("size") String size
    ) {
        Pageable p = new Pageable(sort, sortField, Integer.parseInt(page), Integer.parseInt(size));
        return productService.findAllProductsInCategory(category, p).stream()
                .map(ProductOutput::from)
                .toList();
    }

    @Operation(summary = "Get product by id")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Product"),
            @APIResponse(responseCode = "404", description = "Product with given id was not found")
    })
    @GET
    @Path("{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductOutput getProduct(
            @Parameter(description = "Product id", required = true)
            @PathParam("uuid") UUID uuid
    ) {
        return ProductOutput.from(productService.getProduct(uuid));
    }

    @Operation(summary = "Update product")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Product was updated successfully"),
            @APIResponse(responseCode = "404", description = "Product category was not found or product with given id was not found"),
            @APIResponse(responseCode = "409", description = "Product with given name already exists" +
                    "in category")
    })
    @PUT
    @Path("{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
            @Parameter(description = "Product id", required = true)
            @PathParam("uuid") UUID uuid,
            @RequestBody(
                    description = "Product input data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductInput.class))
            )
            @Valid ProductInput input
    ) {
        productService.update(uuid, input.toDto());
        return Response.noContent() .build();
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


    @Schema
    @NoArgsConstructor(force = true)
    @AllArgsConstructor
    @Getter
    @FieldDefaults(makeFinal = true)
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
