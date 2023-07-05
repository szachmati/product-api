package pl.wit.shop.product.test.base;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionOperations;

import jakarta.persistence.EntityManager;
import java.util.List;

public abstract class BaseDbTest extends PostgresContainer {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected TransactionOperations transactionOperations;

    @BeforeEach
    void clearEntities() {
        clearAllEntities();
    }

    private void clearAllEntities() {
        List<String> tableNames = getTableNamesInCurrentSchema();
        transactionOperations.executeWithoutResult(status -> {
            truncateTables(tableNames);
            restartSequences(tableNames);
        });
    }

    private List getTableNamesInCurrentSchema() {
        return entityManager
                .createNativeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'public' AND TABLE_NAME <> 'flyway_schema_history'")
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
