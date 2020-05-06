package uk.gov.hmcts.reform.divorce.documentgenerator.config;

import lombok.Data;

@Data
public class TemplateConfigurationUnit {

    private String templateName;
    private String fileName;
    private String documentGenerator;

}
