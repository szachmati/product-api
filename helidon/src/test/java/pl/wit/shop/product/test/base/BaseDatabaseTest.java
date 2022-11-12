package pl.wit.shop.product.test.base;

import io.helidon.microprofile.tests.junit5.Configuration;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.TransactionManager;
import lombok.Getter;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import pl.wit.shop.product.test.transaction.TransactionOperations;

import javax.sql.DataSource;
import java.util.List;

@HelidonTest
@Configuration(useExisting = true) //TODO sets microprofile config from configure() method
public class BaseDatabaseTest extends PostgresContainer implements TransactionOperations {

    @Getter
    @Inject
    protected TransactionManager transactionManager;

    @PersistenceContext
    protected EntityManager entityManager;

    @Inject
    private DataSource dataSource;

    @BeforeEach
    public void init() {
       createEntities();
       clearAllEntities();
    }

    @AfterEach
    public void clearEntities() {
        clearAllEntities();
    }

    private void createEntities() {
        Flyway flyway = new Flyway(Flyway.configure().dataSource(dataSource));
        if (flyway.info().current() == null) {
            flyway.migrate();
        }
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
                .createNativeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'public' AND table_name <> 'flyway_schema_history'")
                .getResultList();
    }
}