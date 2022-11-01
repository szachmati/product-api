package pl.wit.shop.product.test.base;

import io.helidon.microprofile.tests.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.TransactionManager;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import pl.wit.shop.product.test.transaction.TransactionOperations;

import java.util.List;

@Tag("integration")
@HelidonTest
public class BaseIT implements TransactionOperations {

    @Getter
    @Inject
    private TransactionManager transactionManager;

    @PersistenceContext
    protected EntityManager entityManager;

    @BeforeEach
    void init() {
        clearAllEntities();
    }

    private void clearAllEntities() {
        List tableNames = getTableNamesInCurrentSchema();
        inTransactionWithoutResult(() ->
            entityManager
                    .createNativeQuery("TRUNCATE TABLE " + String.join(",", tableNames) + " restart IDENTITY")
                    .executeUpdate()
        );
    }

    private List getTableNamesInCurrentSchema() {
        return entityManager
                .createNativeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'public'")
                .getResultList();
    }
}
