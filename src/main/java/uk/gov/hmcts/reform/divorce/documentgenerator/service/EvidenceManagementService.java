package uk.gov.hmcts.reform.divorce.documentgenerator.service;

import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.FileUploadResponse;

public interface EvidenceManagementService {
    FileUploadResponse storeDocumentAndGetInfo(byte[] document, String authorizationToken, String fileName);
}
