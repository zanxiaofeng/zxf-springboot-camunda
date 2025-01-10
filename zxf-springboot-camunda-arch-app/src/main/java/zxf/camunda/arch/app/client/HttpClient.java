package zxf.camunda.arch.app.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import zxf.camunda.arch.app.client.http.RestTemplateFactory;

import java.net.URI;
import java.util.Map;

@Service
public class HttpClient {
    public Map request(String method, String url, String body, Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        RequestEntity<String> requestEntity = new RequestEntity<>(body, httpHeaders, HttpMethod.valueOf(method), URI.create(url));
        ResponseEntity<Map> response = RestTemplateFactory.basicRestTemplate(false).exchange(requestEntity, Map.class);
        return response.getBody();
    }
}