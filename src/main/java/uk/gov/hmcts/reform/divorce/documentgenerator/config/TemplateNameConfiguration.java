package uk.gov.hmcts.reform.divorce.documentgenerator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "document-template")
public class TemplateNameConfiguration {
    private Map<String, String> templatesName;
}
