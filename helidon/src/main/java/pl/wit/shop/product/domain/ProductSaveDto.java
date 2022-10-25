package pl.wit.shop.product.domain;


import lombok.Value;

import java.math.BigDecimal;

@Value
public class ProductSaveDto {
    String name;
    String category;
    BigDecimal price;
}
