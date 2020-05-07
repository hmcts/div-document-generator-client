package uk.gov.hmcts.reform.divorce.documentgenerator.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PDF_GENERATOR_TYPE;

@Component
@ConfigurationProperties(prefix = "document.templates", ignoreUnknownFields = false)
public class TemplatesConfiguration {

    @Setter
    private List<TemplateConfiguration> configurationList;

    private Map<String, TemplateConfiguration> templateConfigurationMap;

    @PostConstruct
    public void init() {
        this.templateConfigurationMap = configurationList.stream().collect(Collectors.toMap(TemplateConfiguration::getTemplateName, t -> t));
    }

    public String getFileNameByTemplateName(String templateName) {
        return Optional.ofNullable(templateConfigurationMap.get(templateName))
            .orElseThrow(() -> new IllegalArgumentException("Unknown template: " + templateName))
            .getFileName();
        //TODO - write tests for when template name is duplicated
    }

    public String getGeneratorServiceNameByTemplateName(String templateName) {
        //TODO - what happens if I don't find the template - keep behaviour on first PR
        return Optional.ofNullable(templateConfigurationMap.get(templateName)).map(TemplateConfiguration::getDocumentGenerator).orElse(PDF_GENERATOR_TYPE);

        //TODO - preparation for refactoring - what happens if I pass an unexisting docmosis template as a parameter - do we want to protect against this?
        //  careful changing this. we might have templates that are not listed here but still being passed by clients
        //TODO - we probably got to a point where Docmosis is the default
    }

}
