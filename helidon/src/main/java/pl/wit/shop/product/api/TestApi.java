package pl.wit.shop.product.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("")
public class TestApi {

    @GET
    public String hello() {
        return "Hello user";
    }
}
