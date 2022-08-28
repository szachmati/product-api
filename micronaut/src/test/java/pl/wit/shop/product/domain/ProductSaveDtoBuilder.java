package pl.wit.shop.product.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.With;

import java.math.BigDecimal;

@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductSaveDtoBuilder {

    private final String name;
    private final String category;
    private final BigDecimal price;

    private ProductSaveDtoBuilder() {
        this.name = "Home product";
        this.category = "HOME";
        this.price = BigDecimal.ONE;
    }

    public static ProductSaveDtoBuilder aProductSaveDto() {
        return new ProductSaveDtoBuilder();
    }

    public ProductSaveDto build() {
        return new ProductSaveDto(name, category, price);
    }
}
