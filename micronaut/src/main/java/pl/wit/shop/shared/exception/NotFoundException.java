package pl.wit.shop.shared.exception;

import io.micronaut.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(String cause) {
        super(HttpStatus.NOT_FOUND, cause);
    }
}
