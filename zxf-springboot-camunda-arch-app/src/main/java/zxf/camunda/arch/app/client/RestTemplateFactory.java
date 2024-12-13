package zxf.camunda.arch.app.client;

import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import zxf.camunda.arch.app.client.http.LoggingRequestInterceptor;
import zxf.camunda.arch.app.client.http.MyResponseErrorHandler;
import zxf.camunda.arch.app.client.http.SetHeaderRequestInterceptor;

public class RestTemplateFactory {
    private final ClientHttpRequestFactory clientHttpRequestFactory;

    public RestTemplateFactory(ClientHttpRequestFactory clientHttpRequestFactory) {
        this.clientHttpRequestFactory = clientHttpRequestFactory;
    }

    public RestTemplate basicRestTemplate(Boolean exceptionable) {
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(clientHttpRequestFactory));
        if (!exceptionable) {
            restTemplate.setErrorHandler(new MyResponseErrorHandler());
        }
        restTemplate.getInterceptors().add(new LoggingRequestInterceptor());
        return restTemplate;
    }

    public RestTemplate restTemplateWithBasicAuth(Boolean exceptionable, String username, String passwd) {
        RestTemplate basicRestTemplate = basicRestTemplate(exceptionable);
        basicRestTemplate.getInterceptors().add(0, new BasicAuthenticationInterceptor(username, passwd));
        return basicRestTemplate;
    }

    public RestTemplate restTemplateWithTokenAuth(Boolean exceptionable, String token) {
        RestTemplate basicRestTemplate = basicRestTemplate(exceptionable);
        basicRestTemplate.getInterceptors().add(0, new SetHeaderRequestInterceptor("X-Token", token));
        return basicRestTemplate;
    }
}
