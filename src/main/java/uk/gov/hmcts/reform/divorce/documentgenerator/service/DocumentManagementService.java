package uk.gov.hmcts.reform.divorce.documentgenerator.service;

import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;

import java.util.Map;

public interface DocumentManagementService {
    GeneratedDocumentInfo generateAndStoreDocument(String templateName, Map<String, Object> placeholders,
                                                   String authorizationToken);

    GeneratedDocumentInfo storeDocument(byte[] document, String authorizationToken);

    byte[] generateDocument(String templateName, Map<String, Object> placeholders);
}
