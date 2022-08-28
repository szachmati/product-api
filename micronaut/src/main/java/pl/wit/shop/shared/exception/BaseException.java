package pl.wit.shop.shared.exception;

import io.micronaut.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private HttpStatus status;
    private String reason;
}
