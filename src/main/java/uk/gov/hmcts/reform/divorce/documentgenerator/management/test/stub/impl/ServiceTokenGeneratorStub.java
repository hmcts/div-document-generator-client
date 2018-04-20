package uk.gov.hmcts.reform.divorce.documentgenerator.management.test.stub.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

@Component
@ConditionalOnProperty(value = "service-auth-provider.service.stub.enabled", havingValue = "true")
public class ServiceTokenGeneratorStub implements AuthTokenGenerator {

    private static final String DUMMY_SERVICE_TOKEN = "Dummy Token";

    @Override
    public String generate() {
        return DUMMY_SERVICE_TOKEN;
    }
}
