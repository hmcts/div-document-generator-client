package uk.gov.hmcts.reform.divorce.documentgenerator.service;

import java.util.Map;

public interface PDFGenerationService {
    byte[] generateFromHtml(byte[] template, Map<String, Object> placeholders);
}
