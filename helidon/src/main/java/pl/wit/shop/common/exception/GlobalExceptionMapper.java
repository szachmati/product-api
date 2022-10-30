package pl.wit.shop.common.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.wit.shop.shared.exception.BaseException;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<BaseException> {

    @Override
    public Response toResponse(BaseException ex) {
        return Response
                .status(ex.getCode())
                .entity(new ResponseError(ex.getCode(), ex.getMessage()))
                .build();
    }

    record ResponseError(int code, String message) {}
}
