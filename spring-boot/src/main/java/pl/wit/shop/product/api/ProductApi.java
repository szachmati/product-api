package pl.wit.shop.product.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid ProductInput productInput) {
        productService.create(productInput.toDto());
    }

    @DeleteMapping("/{uuid}")
    public void delete(@PathVariable UUID uuid) {
        productService.delete(uuid);
    }

    @GetMapping()
    public Page<ProductOutput> findAllProductsInCategory(
            @RequestParam String category,
            @PageableDefault(size = 20)
            @SortDefault(sort = "name", caseSensitive = false, direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return productService.findAllProductsInCategory(category, pageable)
                .map(ProductOutput::from);
    }

    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable UUID uuid, @RequestBody @Valid ProductInput productInput) {
        productService.update(uuid, productInput.toDto());
    }

    record ProductOutput(
            UUID uuid,
            String category,
            String name,
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

    public record ProductInput(@NotBlank String name, @NotNull String category, @NotNull BigDecimal price) {
        ProductSaveDto toDto() {
            return new ProductSaveDto(name, category, price);
        }
    }
}
