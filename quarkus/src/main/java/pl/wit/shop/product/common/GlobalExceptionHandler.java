package pl.wit.shop.product.common;

import pl.wit.shop.product.shared.exception.BaseException;
import pl.wit.shop.product.shared.exception.ResponseError;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<BaseException> {

    @Override
    public Response toResponse(BaseException ex) {
        return Response
                .status(ex.getCode())
                .entity(new ResponseError(ex.getCode(), ex.getMessage()))
                .build();
    }


}
