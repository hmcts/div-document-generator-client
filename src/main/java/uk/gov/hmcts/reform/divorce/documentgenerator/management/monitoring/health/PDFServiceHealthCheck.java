package uk.gov.hmcts.reform.divorce.documentgenerator.management.monitoring.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PDFServiceHealthCheck extends WebServiceHealthCheck {
    @Autowired
    public PDFServiceHealthCheck(HttpEntityFactory httpEntityFactory, RestTemplate restTemplate,
                                 @Value("${service.pdf-service.health.uri}") String uri) {
        super(httpEntityFactory, restTemplate, uri);
    }
}
