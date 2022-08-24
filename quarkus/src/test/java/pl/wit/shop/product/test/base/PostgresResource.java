package pl.wit.shop.product.test.base;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

@Testcontainers
public class PostgresResource implements QuarkusTestResourceLifecycleManager {

    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withDatabaseName("shop")
                    .withUsername("shop")
                    .withPassword("shop");

    @Override
    public Map<String, String> start() {
        POSTGRE_SQL_CONTAINER.start();
        return Map.of(
                "quarkus.datasource.jdbc.url", POSTGRE_SQL_CONTAINER.getJdbcUrl(),
                "quarkus.datasource.username", POSTGRE_SQL_CONTAINER.getUsername(),
                "quarkus.datasource.password", POSTGRE_SQL_CONTAINER.getPassword()
        );
    }

    @Override
    public void stop() {
        POSTGRE_SQL_CONTAINER.stop();
    }
}
