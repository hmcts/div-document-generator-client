package uk.gov.hmcts.reform.divorce.documentgenerator.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data//TODO - reconsider these annotations
@Component
@ConfigurationProperties(prefix = "document.templates")
public class TemplateConfiguration {

    private Map<String, String> map;

    //TODO - rename this
    private Map<String, TemplateConfigurationUnit> templateConfigurationUnits;//TODO - rename bean after refactoring

    public String getFileNameByTemplateName(String templateName) {
        return templateConfigurationUnits.get(templateName).getFileName();
    }

    //TODO - one of the questions this should answer is what's the pdf generator for this template

    //TODO - the other is what's the default file name

}
