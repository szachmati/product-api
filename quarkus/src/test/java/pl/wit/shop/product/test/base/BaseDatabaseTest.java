package pl.wit.shop.product.test.base;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;


@QuarkusTestResource(PostgresResource.class)
public class BaseDatabaseTest {

    @Inject
    private EntityManager entityManager;

    @BeforeEach
    void cleanAllEntities() {
       clearAllEntities();
    }

    private void clearAllEntities() {
        List<String> tableNames = getTableNamesInCurrentSchema();
        QuarkusTransaction.run(() -> {
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
