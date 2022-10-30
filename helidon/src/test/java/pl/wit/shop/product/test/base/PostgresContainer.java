package pl.wit.shop.product.test.base;

import io.helidon.config.mp.MpConfigSources;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;


@Testcontainers
public class PostgresContainer {

    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.5"))
                    .withDatabaseName("shop")
                    .withUsername("shop")
                    .withPassword("shop")
                    .withReuse(true);


    protected static void addAdditionalConfig() {
        Map<String, String> configValues = Map.of(
                "mp.initializer.allow", "true",
        "javax.sql.DataSource.shopDataSource.dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource",
        "javax.sql.DataSource.shopDataSource.dataSource.url", POSTGRE_SQL_CONTAINER.getJdbcUrl(),
        "javax.sql.DataSource.shopDataSource.dataSource.user", POSTGRE_SQL_CONTAINER.getUsername(),
        "javax.sql.DataSource.shopDataSource.dataSource.password", POSTGRE_SQL_CONTAINER.getPassword()
        );

        org.eclipse.microprofile.config.Config mpConfig = ConfigProviderResolver.instance()
                .getBuilder()
                .withSources(MpConfigSources.create(configValues))
                .build();
        ConfigProviderResolver.instance().registerConfig(mpConfig, Thread.currentThread().getContextClassLoader());
    }



    @BeforeAll
    public static void initContainer() {
        POSTGRE_SQL_CONTAINER.start();
        System.setProperty("TEST_DB_URL", POSTGRE_SQL_CONTAINER.getJdbcUrl());
        System.setProperty("TEST_DB_USER", POSTGRE_SQL_CONTAINER.getUsername());
        System.setProperty("TEST_DB_PASSWORD", POSTGRE_SQL_CONTAINER.getPassword());

        addAdditionalConfig();
    }
}
