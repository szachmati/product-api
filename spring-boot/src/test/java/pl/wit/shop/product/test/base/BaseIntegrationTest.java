package pl.wit.shop.product.test.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

@AutoConfigureTestEntityManager
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest extends BaseDatabaseTest {

    @LocalServerPort
    private int port;

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
