package pl.wit.shop.product.domain;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import pl.wit.shop.common.repository.BaseRepository;
import pl.wit.shop.shared.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public interface ProductRepository extends BaseRepository<Product> {

    boolean existsByNameAndCategoryName(String productName, String categoryName);

    List<Product> findAllProductsInCategory(String category);

    @Override
    default Class<Product> clazz() {
        return Product.class;
    }

    @Override
    default Optional<Product> findByUuid(UUID uuid) {
        CriteriaBuilder criteria = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Product> query = criteria.createQuery(Product.class);
        Root<Product> root = query.from(clazz());
        root.fetch("category", JoinType.INNER);
        query.select(root).where(criteria.equal(root.get("uuid"), uuid));
        try {
            return Optional.ofNullable(getEntityManager().createQuery(query).getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    default Supplier<NotFoundException> notFoundException(String cause) {
        return () -> new ProductNotFoundException(cause);
    }

    class ProductNotFoundException extends NotFoundException {
        public ProductNotFoundException(String cause) {
            super(cause);
        }
    }
}
