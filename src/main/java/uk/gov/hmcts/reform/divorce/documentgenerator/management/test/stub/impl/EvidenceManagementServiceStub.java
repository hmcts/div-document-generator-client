package uk.gov.hmcts.reform.divorce.documentgenerator.management.test.stub.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.FileUploadResponse;
import uk.gov.hmcts.reform.divorce.documentgenerator.management.test.stub.DocumentDownloadService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.EvidenceManagementService;

import java.time.Clock;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@ConditionalOnProperty(value = "evidence-management-api.service.stub.enabled", havingValue = "true")
public class EvidenceManagementServiceStub implements EvidenceManagementService, DocumentDownloadService {
    private static final String TEST_DOCUMENTS_DOWNLOAD_CONTEXT_PATH = "/test/documents/";

    private static final Map<String, byte[]> DATA_STORE = new ConcurrentHashMap<>();
    private static final String CREATED_BY = "Document Generator Service";

    private final Clock clock = Clock.systemDefaultZone();

    @Override
    public FileUploadResponse storeDocumentAndGetInfo(byte[] document, String authorizationToken) {
        String fileName = UUID.randomUUID().toString();

        DATA_STORE.put(fileName, document);

        FileUploadResponse fileUploadResponse = new FileUploadResponse(HttpStatus.OK);
        fileUploadResponse.setFileUrl(getFileURL(fileName));
        fileUploadResponse.setMimeType(MediaType.APPLICATION_PDF_VALUE);
        fileUploadResponse.setCreatedOn(clock.instant().toString());
        fileUploadResponse.setCreatedBy(CREATED_BY);

        return fileUploadResponse;
    }

    private String getFileURL(String fileName) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(TEST_DOCUMENTS_DOWNLOAD_CONTEXT_PATH + fileName)
                .build()
                .toUri()
                .toString();
    }

    @Override
    public byte[] getDocument(String fileName) {
        return DATA_STORE.get(fileName);
    }
}
