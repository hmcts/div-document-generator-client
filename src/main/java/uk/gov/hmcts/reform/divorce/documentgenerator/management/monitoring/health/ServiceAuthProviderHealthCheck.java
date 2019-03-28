package uk.gov.hmcts.reform.divorce.documentgenerator.management.monitoring.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ServiceAuthProviderHealthCheck extends WebServiceHealthCheck {
    @Autowired
    public ServiceAuthProviderHealthCheck(HttpEntityFactory httpEntityFactory,
                                          @Qualifier("healthCheckRestTemplate") RestTemplate restTemplate,
                                          @Value("${service.service-auth-provider.health.uri}") String uri) {
        super(httpEntityFactory, restTemplate, uri);
    }
}
