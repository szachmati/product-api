package pl.wit.shop.shared.exception;


public record ResponseError(int code, String message) {}