package pl.wit;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class HelloControllerTest {

    @Inject
    @Client("/")
    private HttpClient client;

    @Test
    void testHello() {
        HttpRequest<String> request = HttpRequest.GET("/hello");

        String response = client.toBlocking().retrieve(request);

        assertEquals("Hello World", response);
    }
}
