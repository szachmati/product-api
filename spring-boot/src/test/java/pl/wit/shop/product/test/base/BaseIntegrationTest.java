package pl.wit.shop.product.test.base;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionOperations;
import pl.wit.shop.product.test.utils.RestTemplateMethods;

import javax.persistence.EntityManager;
import java.util.List;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest extends BaseDatabaseTest implements RestTemplateMethods {

    @LocalServerPort
    private int port;

    @Getter
    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected TransactionOperations transactionOperations;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void init() {
        clearAllEntities();
    }

    private void clearAllEntities() {
        List tableNames = getTableNamesInCurrentSchema();
        transactionOperations.executeWithoutResult(status -> {
            entityManager
                    .createNativeQuery("TRUNCATE TABLE " + String.join(",", tableNames) + " restart IDENTITY")
                    .executeUpdate();
        });
    }

    private List getTableNamesInCurrentSchema() {
        return entityManager
                .createNativeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'public'")
                .getResultList();
    }

    @TestConfiguration
    public class TestRestTemplateConfig {
        @Bean
        public TestRestTemplate testRestTemplate() {
            return new TestRestTemplate(new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
            );
        }
    }
}
