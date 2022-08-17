package pl.wit.shop.product.shared.exception;

import javax.ws.rs.core.Response;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(Response.Status.NOT_FOUND.getStatusCode(), message);
    }
}
