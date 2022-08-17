package pl.wit.shop.product.shared.exception;


public record ResponseError(int code, String message) {}