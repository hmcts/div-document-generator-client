package uk.gov.hmcts.reform.divorce.documentgenerator.service;

import java.util.Map;

public interface PDFGenerationService {

    byte[] generate(String templateName, Map<String, Object> placeholders);

}
