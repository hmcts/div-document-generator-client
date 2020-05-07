package uk.gov.hmcts.reform.divorce.documentgenerator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PDF_GENERATOR_TYPE;

@Data//TODO - reconsider these annotations
@Component
@ConfigurationProperties(prefix = "document.templates", ignoreUnknownFields = false)
public class TemplateConfiguration {

    private Map<String, String> map;

    //TODO - rename this
    private List<TemplateConfigurationUnit> templateConfigurationUnits;//TODO - rename bean after refactoring

    private Map<String, TemplateConfigurationUnit> templates;//TODO - rename this

    @PostConstruct
    public void init() {
        this.templates = templateConfigurationUnits.stream().collect(Collectors.toMap(t -> t.getTemplateName(), t -> t));//TODO - load this only once
    }

    public String getFileNameByTemplateName(String templateName) {
        return Optional.ofNullable(templates.get(templateName))
            .orElseThrow(() -> new IllegalArgumentException("Unknown template: " + templateName))
            .getFileName();
        //TODO - write tests for when template name is duplicated
    }

    public String getGeneratorServiceNameByTemplateName(String templateName) {
        //TODO - what happens if I don't find the template - keep behaviour on first PR
        return Optional.ofNullable(templates.get(templateName)).map(TemplateConfigurationUnit::getDocumentGenerator).orElse(PDF_GENERATOR_TYPE);

        //TODO - preparation for refactoring - what happens if I pass an unexisting docmosis template as a parameter - do we want to protect against this?
        //  careful changing this. we might have templates that are not listed here but still being passed by clients
        //TODO - we probably got to a point where Docmosis is the default
    }

    //TODO - the other is what's the default file name

}
