package uk.gov.hmcts.reform.divorce.documentgenerator.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;

import static java.util.stream.Collectors.toMap;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PDF_GENERATOR_TYPE;

@Component
@ConfigurationProperties(prefix = "document.templates", ignoreUnknownFields = false)
public class TemplatesConfiguration {

    @Setter
    private List<TemplateConfiguration> configurationList;

    private Map<String, TemplateConfiguration> templateConfigurationMap;

    @PostConstruct
    public void init() {
        this.templateConfigurationMap = configurationList.stream()
            .collect(toMap(TemplateConfiguration::getTemplateName, t -> t));
    }

    public String getFileNameByTemplateName(String templateName) {
        return Optional.ofNullable(templateConfigurationMap.get(templateName))
            .orElseThrow(() -> new IllegalArgumentException("Unknown template: " + templateName))
            .getFileName();
    }

    public String getGeneratorServiceNameByTemplateName(String templateName) {
        return Optional.ofNullable(templateConfigurationMap.get(templateName))
            .map(TemplateConfiguration::getDocumentGenerator)
            .orElse(PDF_GENERATOR_TYPE);
        //TODO - for next PR - make docmosis the default engine and take a file name as an optional parameter - the only downside is that the docmosis service would fail the request. this might be ok

    }

}
