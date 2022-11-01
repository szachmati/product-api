package pl.wit.shop.product.api;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.wit.shop.common.repository.Pageable;
import pl.wit.shop.common.repository.Sort;
import pl.wit.shop.product.domain.Product;
import pl.wit.shop.product.domain.ProductSaveDto;
import pl.wit.shop.product.domain.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Path("/api/products")
@RequestScoped
public class ProductApi {

    private final ProductService productService;

    @Inject
    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(ProductInput input) {
        productService.create(input.toDto());
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("{uuid}")
    public Response delete(@PathParam("uuid") UUID uuid) {
        productService.delete(uuid);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductOutput> findAllProductsInCategory(
            @QueryParam("category") String category,
            @QueryParam("sort") Sort sort,
            @QueryParam("sortField") String sortField,
            @QueryParam("page") String page,
            @QueryParam("size") String size
    ) {
        Pageable p = new Pageable(sort, sortField, Integer.parseInt(page), Integer.parseInt(size));
        return productService.findAllProductsInCategory(category, p).stream()
                .map(ProductOutput::from)
                .toList();
    }

    @GET
    @Path("{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProductOutput getProduct(@PathParam("uuid") UUID uuid) {
        return ProductOutput.from(productService.getProduct(uuid));
    }

    @PUT
    @Path("{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("uuid") UUID uuid, ProductInput input) {
        productService.update(uuid, input.toDto());
        return Response.noContent() .build();
    }

    public record ProductInput(@NotBlank String name, @NotNull String category, @NotNull BigDecimal price) {
        ProductSaveDto toDto() {
            return new ProductSaveDto(name, category, price);
        }
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductOutput {
        private UUID uuid;
        private String name;
        private String category;

        static ProductOutput from(Product product) {
            return new ProductOutput(product.getUuid(), product.getName(), product.getCategory().getName());
        }
    }
}
