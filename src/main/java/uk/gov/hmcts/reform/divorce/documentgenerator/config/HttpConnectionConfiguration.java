package uk.gov.hmcts.reform.divorce.documentgenerator.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableList;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.logging.httpcomponents.OutboundRequestIdSettingInterceptor;
import uk.gov.hmcts.reform.logging.httpcomponents.OutboundRequestLoggingInterceptor;

import static java.util.Arrays.asList;

@Configuration
public class HttpConnectionConfiguration {

    private static final MediaType MEDIA_TYPE_HAL_JSON =
            new MediaType("application",
                    "vnd.uk.gov.hmcts.dm.document-collection.v1+hal+json",
                    MappingJackson2HttpMessageConverter.DEFAULT_CHARSET);

    @Value("${http.connect.timeout}")
    private int httpConnectTimeout;

    @Value("${http.connect.request.timeout}")
    private int httpConnectRequestTimeout;

    @Value("${health.check.http.connect.timeout}")
    private int healthCheckHttpConnectTimeout;

    @Value("${health.check.http.connect.timeout}")
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
        jackson2HttpConverter.setSupportedMediaTypes(ImmutableList.of(MEDIA_TYPE_HAL_JSON, MediaType.APPLICATION_JSON));

        return jackson2HttpConverter;
    }

    @Bean
    public RestTemplate restTemplate(@Autowired MappingJackson2HttpMessageConverter jackson2HttpConverter) {
        return getRestTemplate(jackson2HttpConverter, httpConnectTimeout, httpConnectRequestTimeout);
    }

    @Bean
    public RestTemplate healthCheckRestTemplate(@Autowired MappingJackson2HttpMessageConverter jackson2HttpConverter) {
        return getRestTemplate(
            jackson2HttpConverter,
            healthCheckHttpConnectTimeout,
            healthCheckHttpConnectRequestTimeout
        );
    }

    private RestTemplate getRestTemplate(
        @Autowired MappingJackson2HttpMessageConverter jackson2HttpConverter,
        int connectTimeout,
        int connectRequestTimeout) {
        RestTemplate restTemplate = new RestTemplate(asList(jackson2HttpConverter,
            new FormHttpMessageConverter(),
            new ResourceHttpMessageConverter(),
            new ByteArrayHttpMessageConverter(),
            new StringHttpMessageConverter()));

        RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(connectTimeout)
            .setConnectionRequestTimeout(connectRequestTimeout)
            .build();

        CloseableHttpClient client = HttpClientBuilder
            .create()
            .useSystemProperties()
            .addInterceptorFirst(new OutboundRequestIdSettingInterceptor())
            .addInterceptorFirst((HttpRequestInterceptor) new OutboundRequestLoggingInterceptor())
            .addInterceptorLast((HttpResponseInterceptor) new OutboundRequestLoggingInterceptor())
            .setDefaultRequestConfig(config)
            .build();

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));

        return restTemplate;
    }
}
