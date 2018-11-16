package uk.gov.hmcts.reform.divorce;

import feign.Feign;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.authorisation.generators.ServiceAuthTokenGenerator;

@Lazy
@Configuration
@ComponentScan(basePackages = {"uk.gov.hmcts.reform.divorce.divorce", "uk.gov.hmcts.auth.provider.service"})
@PropertySource({"classpath:application.properties"})
@PropertySource({"classpath:application-${env}.properties"})
public class ServiceContextConfiguration {

    @Bean
    public AuthTokenGenerator serviceAuthTokenGenerator(
        @Value("${idam.auth.secret}") final String secret,
        @Value("${idam.auth.microservice}") final String microService,
        @Value("${idam.s2s-auth.url}") final String s2sUrl
    ) {
        final ServiceAuthorisationApi serviceAuthorisationApi = Feign.builder()
            .encoder(new JacksonEncoder())
            .contract(new SpringMvcContract())
            .target(ServiceAuthorisationApi.class, s2sUrl);

        return new ServiceAuthTokenGenerator(secret, microService, serviceAuthorisationApi);
    }

    @Bean
    public IdamUtils getIdamUtil() {
        return new IdamUtils();
    }
}
