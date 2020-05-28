package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.TemplateManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.NullOrEmptyValidator;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.ResourceLoader;

@Service
@Slf4j
public class TemplateManagementServiceImpl implements TemplateManagementService {

    private static final String RESOURCE_PATH_TEMPLATE = "data/templates/%s.html";

    @Override
    public byte[] getTemplateByName(String templateName) {
        log.info("Get template requested with templateName [{}]", templateName);
        return ResourceLoader.loadResource(getResourcePath(templateName));
    }

    private String getResourcePath(String templateName) {
        NullOrEmptyValidator.requireNonBlank(templateName);
        return String.format(RESOURCE_PATH_TEMPLATE, templateName);
    }

}
