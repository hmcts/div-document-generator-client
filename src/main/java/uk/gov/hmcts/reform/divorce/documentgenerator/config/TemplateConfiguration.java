package uk.gov.hmcts.reform.divorce.documentgenerator.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateConfiguration {

    private String templateName;
    private String fileName;
    private String documentGenerator;

}
