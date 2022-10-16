package pl.wit.shop.example;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
class GreetingService {


    @PersistenceContext
    private EntityManager entityManager;

    List<Greeting> getAll() {
        return entityManager.createQuery("SELECT g FROM Greeting g")
                .getResultList();
    }
}
