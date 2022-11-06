package pl.wit.shop.product.test.base;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.TransactionOperations;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@MicronautTest
public class BaseDatabaseTest extends PostgresContainer {

    @Inject
    private TransactionOperations<?> transactionOperations;

    @Inject
    private EntityManager entityManager;

    @BeforeEach
    void clearEntities() {
        clearAllEntities();
    }

    private void clearAllEntities() {
        List<String> tableNames = new ArrayList<>();
        transactionOperations.executeRead(callback -> {
            tableNames.addAll(getTableNamesInCurrentSchema());
            return callback;
        });
        transactionOperations.executeWrite(callback -> {
            truncateTables(tableNames);
            restartSequences(tableNames);
            return callback;
        });
    }

    private List getTableNamesInCurrentSchema() {
        return entityManager
                .createNativeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'public'")
                .getResultList();
    }

    private void truncateTables(List<String> tableNames) {
        entityManager
                .createNativeQuery("TRUNCATE TABLE " + String.join(",", tableNames) + " restart IDENTITY")
                .executeUpdate();
    }
    private void restartSequences(List<String> tableNames) {
        tableNames.forEach(name -> entityManager
                .createNativeQuery(String.format("ALTER SEQUENCE %s_seq RESTART WITH 1", name))
                .executeUpdate()
        );
    }
}
