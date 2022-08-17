package pl.wit.shop.product.shared.exception;

import javax.ws.rs.core.Response;

public class ConflictException extends BaseException {
    public ConflictException(String message) {
        super(Response.Status.CONFLICT.getStatusCode(), message);
    }
}
