package pl.wit.shop.product.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ProductRepository implements PanacheRepositoryBase<Product, Long> {

    public boolean existsByNameAndCategoryName(String name, String categoryName) {
        return find("name = ?1 and category.name = ?2", name, categoryName)
                .firstResultOptional()
                .isPresent();
    }

    public List<Product> findAllByCategoryName(String category, Sort sort, int pageNumber, int pageSize) {
        return find(
                "SELECT p FROM Product p INNER JOIN FETCH p.category pc " +
                        "WHERE pc.name = ?1 ", sort, category)
                .page(Page.of(pageNumber, pageSize))
                .list();
    }

    public Product getByUuid(UUID uuid) {
        return find("uuid", uuid)
                .firstResultOptional()
                .orElseThrow(() -> new ProductNotFoundException(uuid));
    }

    public static class ProductNotFoundException extends NotFoundException {
        public ProductNotFoundException(UUID uuid) {
            super(String.format("Product with uuid: %s not found", uuid));
        }
    }
}
