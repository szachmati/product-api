package pl.wit.shop.shared.exception;

import io.micronaut.http.HttpStatus;

public class ConflictException extends BaseException {
    public ConflictException(String reason) {
        super(HttpStatus.CONFLICT, reason);
    }
}
