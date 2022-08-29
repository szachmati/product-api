package pl.wit.shop.common;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import pl.wit.shop.shared.exception.BaseException;

@Produces
@Singleton
@Requires(classes = BaseException.class)
public class GlobalExceptionHandler implements ExceptionHandler<BaseException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, BaseException ex) {
       return HttpResponse.status(ex.getStatus(), ex.getReason());
    }
}
