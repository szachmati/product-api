package pl.wit.shop.product.domain;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;


}
