package pl.wit.shop.product.test.base;

import io.micronaut.test.support.TestPropertyProvider;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

//TODO Does not work, working solution in application-test.yml
@Testcontainers
public class PostgresContainer implements TestPropertyProvider {

    private static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;

    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("test")
                .withPassword("test")
                .withUsername("test");
        POSTGRE_SQL_CONTAINER.start();
    }

    @Override
    public Map<String, String> getProperties() {
        return Map.of(
                "datasource.default.jdbc-url", POSTGRE_SQL_CONTAINER.getJdbcUrl(),
                "datasource.default.username", POSTGRE_SQL_CONTAINER.getUsername(),
                "datasource.default.password", POSTGRE_SQL_CONTAINER.getPassword()
        );
    }
}
