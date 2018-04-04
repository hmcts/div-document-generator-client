package uk.gov.hmcts.reform.divorce.documentgenerator.service;

import java.util.Map;

import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;

public interface DocumentManagementService {
    GeneratedDocumentInfo generateAndStoreDocument(String templateName, Map<String, Object> placeholders);

    GeneratedDocumentInfo storeDocument(byte[] document);

    byte[] generateDocument(String templateName, Map<String, Object> placeholders);
}
