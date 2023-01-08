package pl.wit.shop.product.test.base;

import lombok.Getter;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import pl.wit.shop.product.test.utils.RestTemplateMethods;


@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest extends BaseDbTest implements RestTemplateMethods {

    @Getter
    @Autowired
    protected TestRestTemplate testRestTemplate;
}
