package pl.wit.shop.product.test.base;

import org.springframework.transaction.support.TransactionOperations;

import javax.persistence.EntityManager;
import java.util.List;

public interface DbCleaner {

    TransactionOperations getTransactionOperations();

    EntityManager getEntityManager();

    default void clearAllEntities() {
        List<String> tableNames = getTableNamesInCurrentSchema();
        getTransactionOperations().executeWithoutResult(status -> {
            truncateTables(tableNames);
            restartSequences(tableNames);
        });
    }

    private List getTableNamesInCurrentSchema() {
        return getEntityManager()
                .createNativeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'public' AND TABLE_NAME <> 'flyway_schema_history'")
                .getResultList();
    }

    private void truncateTables(List<String> tableNames) {
        getEntityManager()
                .createNativeQuery("TRUNCATE TABLE " + String.join(",", tableNames) + " restart IDENTITY")
                .executeUpdate();
    }
    private void restartSequences(List<String> tableNames) {
        tableNames.forEach(name -> getEntityManager()
                .createNativeQuery(String.format("ALTER SEQUENCE %s_seq RESTART WITH 1", name))
                .executeUpdate()
        );
    }
}
