package pl.wit.shop.product.api;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import pl.wit.shop.product.domain.Product;
import pl.wit.shop.product.domain.ProductService;

import java.util.UUID;

@Path("/api/products")
@RequestScoped
public class ProductApi {

    private final ProductService productService;

    @Inject
    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    @GET
    @Path("/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductOutput getProduct(@PathParam("uuid") UUID uuid) {
        return ProductOutput.from(productService.getProduct(uuid));
    }


    record ProductOutput(UUID uuid, String name, String category) {
        static ProductOutput from(Product product) {
            return new ProductOutput(product.getUuid(), product.getName(), product.getCategory().getName());
        }
    }
}
