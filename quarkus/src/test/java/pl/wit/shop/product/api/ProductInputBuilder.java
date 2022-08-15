package pl.wit.shop.product.api;



import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.With;

import java.math.BigDecimal;

import static pl.wit.shop.product.api.ProductApi.ProductInput;

@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductInputBuilder {
    private final String name;
    private final String category;
    private final BigDecimal price;

    private ProductInputBuilder() {
        this.name = "Home product";
        this.category = "HOME";
        this.price = BigDecimal.ONE;
    }

    public static ProductInputBuilder aProductInput() {
        return new ProductInputBuilder();
    }

    public ProductInput build() {
        return new ProductInput(name, category, price);
    }
}
