package pl.wit.shop.product.test.base;

import lombok.Getter;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import pl.wit.shop.product.test.utils.RestTemplateMethods;

import javax.persistence.EntityManager;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest extends BaseDbTest implements RestTemplateMethods {

    @LocalServerPort
    private int port;

    @Getter
    @Autowired
    protected TestRestTemplate testRestTemplate;

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
