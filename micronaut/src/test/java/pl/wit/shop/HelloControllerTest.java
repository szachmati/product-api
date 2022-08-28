package pl.wit.shop;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@MicronautTest
class HelloControllerTest {

    @Inject
    @Client("/")
    private HttpClient client;

    @Test
    void testHello() {
        HttpRequest<String> request = HttpRequest.GET("/hello");

        String response = client.toBlocking().retrieve(request);

        assertThat(response, is("Hello World"));
    }
}
