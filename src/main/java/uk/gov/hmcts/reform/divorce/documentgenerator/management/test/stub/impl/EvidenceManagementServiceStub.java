package uk.gov.hmcts.reform.divorce.documentgenerator.management.test.stub.impl;

import lombok.extern.slf4j.Slf4j;
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
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@ConditionalOnProperty(value = "evidence-management-api.service.stub.enabled", havingValue = "true")
public class EvidenceManagementServiceStub implements EvidenceManagementService, DocumentDownloadService {
    private static final String TEST_DOCUMENTS_DOWNLOAD_CONTEXT_PATH = "/documents/";

    private static final Map<String, byte[]> DATA_STORE = new ConcurrentHashMap<>();
    private static final String CREATED_BY = "Document Generator Service";
    private static Integer counter = 0;

    private final Clock clock = Clock.systemDefaultZone();

    @Override
    public FileUploadResponse storeDocumentAndGetInfo(byte[] document, String authorizationToken, String fileName) {
        fileName = fileName.replace(".","").concat((counter++).toString());
        DATA_STORE.put(fileName, document);
        log.info("File stored as {}", fileName);
        FileUploadResponse fileUploadResponse = new FileUploadResponse(HttpStatus.OK);
        fileUploadResponse.setFileUrl(getFileURL(fileName));
        fileUploadResponse.setFileName(fileName);
        fileUploadResponse.setMimeType(MediaType.APPLICATION_PDF_VALUE);
        fileUploadResponse.setCreatedOn(clock.instant().toString());
        fileUploadResponse.setCreatedBy(CREATED_BY);
        log.info("File response created with url: {} and filename: {} ",
            fileUploadResponse.getFileUrl(),fileUploadResponse.getFileName());
        return fileUploadResponse;
    }

    private String getFileURL(String fileName) {
        return "https://dm-store:8080" + TEST_DOCUMENTS_DOWNLOAD_CONTEXT_PATH + fileName;
    }

    @Override
    public byte[] getDocument(String fileName) {
        return DATA_STORE.get(fileName);
    }
}
