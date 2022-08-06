package pl.wit.shop.product.api;



import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.With;

import java.math.BigDecimal;

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

    public ProductController.ProductInput build() {
        return new ProductController.ProductInput(name, category, price);
    }
}
