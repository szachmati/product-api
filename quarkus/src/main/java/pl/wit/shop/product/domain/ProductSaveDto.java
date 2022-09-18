package pl.wit.shop.product.domain;


import java.math.BigDecimal;

public record ProductSaveDto(String name, String category, BigDecimal price) { }
