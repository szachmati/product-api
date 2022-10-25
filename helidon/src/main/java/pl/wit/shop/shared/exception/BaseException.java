package pl.wit.shop.shared.exception;

import io.helidon.common.http.Http;
import io.helidon.webserver.HttpException;

public class BaseException extends HttpException {
    public BaseException(String message, Http.Status status) {
        super(message, status);
    }
}
