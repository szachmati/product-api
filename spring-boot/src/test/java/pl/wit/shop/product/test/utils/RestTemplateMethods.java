package pl.wit.shop.product.test.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public interface RestTemplateMethods<T> {

    TestRestTemplate getTestRestTemplate();

    default ResponseEntity<T> delete(String url) {;
        return (ResponseEntity<T>) getTestRestTemplate().exchange(url, HttpMethod.DELETE, null, String.class);
    }

    default ResponseEntity<T> get(String url, Class clazz, Object...urlVariables) {
        return getTestRestTemplate().getForEntity(url, clazz, urlVariables);
    }

    default ResponseEntity<RestPageResponse<T>> get(String url, ParameterizedTypeReference<RestPageResponse<T>> responseType, Object...urlVariables) {
        HttpEntity request = createRequest(null);
        return getTestRestTemplate().exchange(url, HttpMethod.GET, request, responseType, urlVariables);
    }

    default ResponseEntity<T> post(String url, T requestBody, Class clazz, Object...urlVariables) {
        HttpEntity<T> request = createRequest(requestBody);
        return getTestRestTemplate().postForEntity(url, request, clazz, urlVariables);
    }

    default ResponseEntity<T> put(String url, T requestBody, Class clazz, Object...urlVariables) {
        HttpEntity<T> request = createRequest(requestBody);
        return getTestRestTemplate().exchange(url, HttpMethod.PUT, request, clazz, urlVariables);
    }

    @NotNull
    private HttpEntity<T> createRequest(T requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(requestBody, headers);
    }
}
