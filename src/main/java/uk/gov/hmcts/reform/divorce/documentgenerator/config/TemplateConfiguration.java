package uk.gov.hmcts.reform.divorce.documentgenerator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data//TODO - reconsider these annotations
@Component
@ConfigurationProperties(prefix = "document.templates", ignoreUnknownFields = false)
public class TemplateConfiguration {

    private Map<String, String> map;

    //TODO - rename this
    private List<TemplateConfigurationUnit> templateConfigurationUnits;//TODO - rename bean after refactoring

    private Map<String, TemplateConfigurationUnit> templateConfig;//TODO - rename this

    public String getFileNameByTemplateName(String templateName) {
        this.templateConfig = templateConfigurationUnits.stream().collect(Collectors.toMap(t -> t.getTemplateName(), t -> t));//TODO - load this only once

        return Optional.ofNullable(templateConfig.get(templateName))
            .orElseThrow(() -> new IllegalArgumentException("Unknown template: " + templateName))
            .getFileName();
        //TODO - write tests for when template name is duplicated
    }

    //TODO - one of the questions this should answer is what's the pdf generator for this template

    //TODO - the other is what's the default file name

}
