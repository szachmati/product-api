package pl.wit.shop.product.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.wit.shop.product.domain.Product;
import pl.wit.shop.product.domain.ProductSaveDto;
import pl.wit.shop.product.domain.ProductService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductApi {

    private final ProductService productService;

    @Operation(summary = "Create product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product was created successfully"),
            @ApiResponse(responseCode = "404", description = "Product category not found"),
            @ApiResponse(responseCode = "409", description = "Product with given name already exists" +
                    "in category")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(
            @Parameter(description = "Product input data", required = true)
            @RequestBody @Valid ProductInput productInput
    ) {
        productService.create(productInput.toDto());
    }

    @Operation(summary = "Delete product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product was deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product with given id was not found")
    })
    @DeleteMapping("{uuid}")
    public void delete(@Parameter(description = "Product id", required = true) @PathVariable UUID uuid) {
        productService.delete(uuid);
    }

    @Operation(summary = "Find all products in category")
    @ApiResponse(
            responseCode = "200",
            description = "Products in given category",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductOutput.class)))
    )
    @GetMapping
    public Page<ProductOutput> findAllProductsInCategory(
            @Parameter(description = "Product category", required = true,
                    content = @Content(schema = @Schema(allowableValues = {
                            "HOME", "ELECTRONICS", "CARS", "FOOD", "FURNITURE",
                            "MOBILE PHONES", "FASHION", "MUSIC", "SPORT", "CHILD", "HEALTH"
                    })))
            @RequestParam
            String category,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC)
            @ParameterObject
            Pageable pageable
    ) {
        return productService.findAllProductsInCategory(category, pageable)
                .map(ProductOutput::from);
    }

    @Operation(summary = "Get product by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product"),
            @ApiResponse(responseCode = "404", description = "Product with given id was not found")
    })
    @GetMapping("{uuid}")
    public ProductOutput getProduct(
            @Parameter(description = "Product id", required = true)
            @PathVariable("uuid") UUID uuid
    ) {
        return ProductOutput.from(productService.getProduct(uuid));
    }


    @Operation(summary = "Update product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product was updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product category was not found or product with given id was not found"),
            @ApiResponse(responseCode = "409", description = "Product with given name already exists" +
                    "in category")
    })
    @PutMapping("/{uuid}")
    public void update(
            @Parameter(description = "Product id", required = true)
            @PathVariable UUID uuid,
            @Parameter(description = "Product input data", required = true)
            @RequestBody @Valid ProductInput productInput
    ) {
        productService.update(uuid, productInput.toDto());
    }

    @Value
    @Schema(description = "Product data")
    public static class ProductOutput {
        @Schema(description = "Product id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID uuid;
        @Schema(description = "Product category", example = "ELECTRONICS")
        String category;
        @Schema(description = "Product name", example = "Playstation 4")
        String name;
        @Schema(description = "Product price", example = "1200.87")
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

    @Schema(description = "Product input data")
    public record ProductInput(
            @Schema(description = "Product name", required = true, example = "LG 123")
            @NotBlank String name,
            @Schema(description = "Product category", required = true, example = "MUSIC", allowableValues = {
                    "HOME", "ELECTRONICS", "CARS", "FOOD", "FURNITURE",
                    "MOBILE PHONES", "FASHION", "MUSIC", "SPORT", "CHILD", "HEALTH"
            })
            @NotNull String category,
            @Schema(description = "Product price", required = true, example = "33.99")
            @NotNull BigDecimal price
    ) {
        ProductSaveDto toDto() {
            return new ProductSaveDto(name, category, price);
        }
    }
}
