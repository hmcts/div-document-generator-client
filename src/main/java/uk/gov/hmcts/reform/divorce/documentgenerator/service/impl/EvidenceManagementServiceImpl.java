package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.FileUploadResponse;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.DocumentStorageException;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.EvidenceManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.NullOrEmptyValidator;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@ConditionalOnProperty(value = "evidence-management-api.service.stub.enabled", havingValue = "false")
public class EvidenceManagementServiceImpl implements EvidenceManagementService {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String FILE_PARAMETER = "file";
    private static final String DEFAULT_NAME_FOR_PDF_FILE = "DivorceDocument.pdf";

    @Value("${service.evidence-management-client-api.uri}")
    private String evidenceManagementEndpoint;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public FileUploadResponse storeDocumentAndGetInfo(byte[] document, String authorizationToken, String fileName) {
        log.info("Save document call to evidence management is made document of size [{}]", document.length);

        try {
            FileUploadResponse fileUploadResponse = storeDocument(document, authorizationToken, fileName);

            if (fileUploadResponse.getStatus() == HttpStatus.OK) {
                return fileUploadResponse;
            } else {
                throw new DocumentStorageException("Failed to store document");
            }
        } catch (Exception e) {
            throw new DocumentStorageException("Error storing document " + e.getMessage(), e);
        }
    }

    private FileUploadResponse storeDocument(byte[] document, String authorizationToken, String fileName) {
        NullOrEmptyValidator.requireNonEmpty(document);

        ResponseEntity<List<FileUploadResponse>> responseEntity = restTemplate.exchange(evidenceManagementEndpoint,
                HttpMethod.POST,
                new HttpEntity<>(
                        buildRequest(document, Optional.ofNullable(fileName).orElse(DEFAULT_NAME_FOR_PDF_FILE)),
                    getHttpHeaders(authorizationToken)),
                new ParameterizedTypeReference<List<FileUploadResponse>>() {
                });

        return responseEntity.getBody().get(0);
    }

    private HttpHeaders getHttpHeaders(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add(AUTHORIZATION_HEADER, authToken);
        return headers;
    }

    private LinkedMultiValueMap<String, Object> buildRequest(byte[] document, String filename) {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        HttpEntity<Resource> httpEntity = new HttpEntity<>(new ByteArrayResource(document) {
            @Override
            public String getFilename() {
                return filename;
            }
        }, headers);

        parameters.add(FILE_PARAMETER, httpEntity);
        return parameters;
    }
}
