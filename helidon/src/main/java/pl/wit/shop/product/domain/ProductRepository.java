package pl.wit.shop.product.domain;

import jakarta.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;


}
