package uk.gov.hmcts.reform.divorce.documentgenerator.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class HttpConnectionConfiguration {

    private static final MediaType MEDIA_TYPE_HAL_JSON =
            new MediaType("application",
                    "vnd.uk.gov.hmcts.dm.document-collection.v1+hal+json",
                    StandardCharsets.UTF_8);

    @Value("${http.connect.timeout}")
    private int httpConnectTimeout;

    @Value("${http.connect.request.timeout}")
    private int httpConnectRequestTimeout;

    @Value("${health.check.http.connect.timeout}")
    private int healthCheckHttpConnectTimeout;

    @Value("${health.check.http.connect.request.timeout}")
    private int healthCheckHttpConnectRequestTimeout;

    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(
        @Autowired ObjectMapper objectMapper) {

        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new Jackson2HalModule());
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        MappingJackson2HttpMessageConverter jackson2HttpConverter
            = new MappingJackson2HttpMessageConverter(objectMapper);
        jackson2HttpConverter.setObjectMapper(objectMapper);
        jackson2HttpConverter.setSupportedMediaTypes(List.of(MEDIA_TYPE_HAL_JSON, MediaType.APPLICATION_JSON));

        return jackson2HttpConverter;
    }

    @Bean
    public RestTemplate restTemplate() {
        return getRestTemplate(httpConnectTimeout, httpConnectRequestTimeout);
    }

    @Bean
    public RestTemplate healthCheckRestTemplate() {
        return getRestTemplate(healthCheckHttpConnectTimeout, healthCheckHttpConnectRequestTimeout);
    }

    private RestTemplate getRestTemplate(int connectTimeout, int connectRequestTimeout) {
        SimpleClientHttpRequestFactory clientHttpRequestFactory  = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(connectTimeout);
        clientHttpRequestFactory.setReadTimeout(connectRequestTimeout);

        return new RestTemplate(clientHttpRequestFactory);
    }

}
