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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import pl.wit.shop.product.test.utils.RestTemplateMethods;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.List;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest extends BaseDatabaseTest
        implements RestTemplateMethods {

    @LocalServerPort
    private int port;

    @Getter
    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManager entityManager;

    protected TransactionTemplate transactionTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        clearAllEntities();
    }
    //TODO need to be fixed
    private void clearAllEntities() {
        List<String> entities = entityManager.getMetamodel().getEntities()
                .stream()
                .filter(entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null)
                .map(EntityType::getName)
                .toList();
        transactionTemplate.executeWithoutResult(status -> {
            entityManager.createNativeQuery("truncate table product_category, product CASCADE ")
                    .executeUpdate();
        });
//        entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED")
//                .executeUpdate();
//        entityManager.createNativeQuery("TRUNCATE TABLE " + String.join(",", entities) + " CASCADE")
//                .executeUpdate();
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
