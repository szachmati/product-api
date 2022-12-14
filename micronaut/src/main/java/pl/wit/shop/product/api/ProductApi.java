package pl.wit.shop.product.api;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.wit.shop.product.domain.Product;
import pl.wit.shop.product.domain.ProductSaveDto;
import pl.wit.shop.product.domain.ProductService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Tag(name = "Product API")
@Controller("/api/products")
@RequiredArgsConstructor
public class ProductApi {

    private final ProductService productService;

    @Operation(summary = "Create product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product was created successfully"),
            @ApiResponse(responseCode = "404", description = "Product category not found"),
            @ApiResponse(responseCode = "409", description = "Product with given name already exists" +
                    "in category")
    })
    @Post(consumes = MediaType.APPLICATION_JSON)
    @Status(HttpStatus.CREATED)
    public void create(
            @Parameter(description = "Product input data", required = true)
            @Body ProductInput productInput
    ) {
        productService.create(productInput.toDto());
    }

    @Operation(summary = "Delete product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product was deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product with given id was not found")
    })
    @Delete("{uuid}")
    @Status(HttpStatus.OK)
    public void delete(
            @Parameter(description = "Product id", required = true)
            @PathVariable UUID uuid
    ) {
        productService.delete(uuid);
    }

    @Operation(summary = "Find all products in category")
    @ApiResponse(
            responseCode = "200",
            description = "Products in given category",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductOutput.class)))
    )
    @Get(produces = MediaType.APPLICATION_JSON)
    @Status(HttpStatus.OK)
    public Page<ProductOutput> findAllByCategoryName(
            @Parameter(description = "Product category", required = true,
                    content = @Content(schema = @Schema(allowableValues = {
                            "HOME", "ELECTRONICS", "CARS", "FOOD", "FURNITURE",
                            "MOBILE PHONES", "FASHION", "MUSIC", "SPORT", "CHILD", "HEALTH"
                    })))
            @QueryValue(value = "category", defaultValue = "HOME")
            String category,
            @QueryValue(value = "page")
            @Parameter(description = "Page number", required = true)
            int page,
            @QueryValue(value = "size")
            @Parameter(description = "Page size", required = true)
            int size,
            @QueryValue(value = "sort")
            @Parameter(description = "Sort field", required = true)
            ProductSort sort,
            @QueryValue(value = "direction")
            @Parameter(description = "Sort direction", required = true)
            Sort.Order.Direction direction
    ) {
        Sort.Order order = direction == Sort.Order.Direction.ASC
                ? Sort.Order.asc(sort.getValue()) : Sort.Order.desc(sort.getValue());
        Pageable pageable = Pageable.from(page, size, Sort.of(order));
        return productService.findAllProductsInCategory(category, pageable)
                .map(ProductOutput::from);
    }

    @Operation(summary = "Get product by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product data"),
            @ApiResponse(responseCode = "404", description = "Product with given id was not found")
    })
    @Get("{uuid}")
    public ProductOutput getProduct(@PathVariable UUID uuid) {
        return ProductOutput.from(productService.getProduct(uuid));
    }

    @Operation(summary = "Update product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product was updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product with given id was not found"),
            @ApiResponse(responseCode = "409", description = "Product with given name already exists" +
                    "in category")
    })
    @Put(value = "{uuid}", consumes = MediaType.APPLICATION_JSON)
    public void update(
            @Parameter(description = "Product id", required = true)
            @PathVariable UUID uuid,
            @Parameter(description = "Product input data", required = true)
            @Body ProductInput productInput
    ) {
        productService.update(uuid, productInput.toDto());
    }

    @Schema
    public record ProductInput(
            @Schema(description = "Product name", required = true, example = "LG 123")
            @NotBlank String name,
            @Schema(description = "Product category", required = true, example = "MUSIC", allowableValues = {
                    "HOME", "ELECTRONICS", "CARS", "FOOD", "FURNITURE",
                    "MOBILE PHONES", "FASHION", "MUSIC", "SPORT", "CHILD", "HEALTH"
            })
            @NotBlank String category,
            @Schema(description = "Product price", required = true, example = "33.99")
            @NotNull BigDecimal price
    ) {
        ProductSaveDto toDto() {
            return new ProductSaveDto(name, category, price);
        }
    }

    @Schema
    public record ProductOutput(
            @Schema(description = "Product id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            UUID uuid,
            @Schema(description = "Product category", example = "ELECTRONICS")
            String category,
            @Schema(description = "Product name", example = "Playstation 4")
            String name,
            @Schema(description = "Product price", example = "1200.87")
            BigDecimal price
    ) {
        static ProductOutput from(Product product) {
            return new ProductOutput(
                    product.getUuid(),
                    product.getCategory().getName(),
                    product.getName(),
                    product.getPrice()
            );
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum ProductSort {

        PRODUCT_NAME("name"),
        PRODUCT_PRICE("price"),
        PRODUCT_CATEGORY("name");

        private final String value;
    }
}
