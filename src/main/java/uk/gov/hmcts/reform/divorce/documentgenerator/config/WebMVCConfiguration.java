package uk.gov.hmcts.reform.divorce.documentgenerator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMVCConfiguration {

    @Bean
    public WebMvcConfigurer configurer() {
        return new WebMvcConfigurerAdapter() {
            @Value("${pdf.test.enabled}")
            private boolean pdfTestEnabled;

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                if (pdfTestEnabled) {
                    registry.addResourceHandler("/**")
                            .addResourceLocations("classpath:/static/");
                }
            }
        };
    }
}
