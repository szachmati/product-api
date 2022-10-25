package pl.wit.shop.shared.exception;


import io.helidon.common.http.Http;

public class ConflictException extends BaseException {

    public ConflictException(String message) {
        super(message, Http.Status.CONFLICT_409);
    }
}
