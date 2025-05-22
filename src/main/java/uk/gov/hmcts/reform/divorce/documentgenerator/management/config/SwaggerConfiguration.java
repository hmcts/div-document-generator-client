package uk.gov.hmcts.reform.divorce.documentgenerator.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfiguration implements WebMvcConfigurer {
    @Value("${documentation.swagger.enabled}")
    private boolean swaggerEnabled;

    @Bean
    public OpenAPI customOpenAPI() {
        if (!swaggerEnabled) {
            // Return empty OpenAPI object when disabled
            return new OpenAPI();
        }

        return new OpenAPI()
            .info(new Info()
                .title("Divorce Document Generation API")
                .description("Given a template name and the placeholder text, this will generate a PDF file, " +
                    "store it in evidence management and will return the URI to retrieve the stored document"));
    }

}
