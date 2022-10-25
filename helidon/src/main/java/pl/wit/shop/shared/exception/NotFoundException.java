package pl.wit.shop.shared.exception;


import io.helidon.common.http.Http;

public class NotFoundException extends BaseException {

    public NotFoundException(String cause) {
        super(cause, Http.Status.NOT_FOUND_404);
    }
}