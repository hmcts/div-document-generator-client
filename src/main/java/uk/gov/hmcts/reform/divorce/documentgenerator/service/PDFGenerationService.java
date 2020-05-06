package uk.gov.hmcts.reform.divorce.documentgenerator.service;

import java.util.Map;

public interface PDFGenerationService {//TODO - rename this to a more generic name

    byte[] generate(String templateName, Map<String, Object> placeholders);

}
