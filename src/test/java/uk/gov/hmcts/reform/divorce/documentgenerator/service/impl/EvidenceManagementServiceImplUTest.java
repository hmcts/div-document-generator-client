package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.FileUploadResponse;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.DocumentStorageException;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.NullOrEmptyValidator;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EvidenceManagementServiceImpl.class, NullOrEmptyValidator.class})
public class EvidenceManagementServiceImplUTest {
    private static final String EVIDENCE_MANAGEMENT_ENDPOINT = "evidence_management_endpoint";
    private static final String AUTHORIZATION_HEADER = "authorizationToken";
    private static final String FILE_PARAMETER = "file";
    private static final String DEFAULT_NAME_FOR_PDF_FILE = "D8MiniPetition.pdf";

    @Mock
    private AuthTokenGenerator serviceTokenGenerator;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    @Spy
    private final EvidenceManagementServiceImpl classUnderTest = new EvidenceManagementServiceImpl();

    @Before
    public void before() {
        Whitebox.setInternalState(classUnderTest, "evidenceManagementEndpoint", EVIDENCE_MANAGEMENT_ENDPOINT);
        mockStatic(NullOrEmptyValidator.class);
    }

    @Test
    public void givenStoreDocumentThrowsException_whenStoreDocumentAndGetInfo_thenThrowDocumentStorageException() throws Exception {
        final byte[] data = {1};
        final RuntimeException documentStorageException = new RuntimeException();

        doThrow(documentStorageException).when(classUnderTest,
                MemberMatcher.method(EvidenceManagementServiceImpl.class, "storeDocument", byte[].class))
                .withArguments(data);

        try {
            classUnderTest.storeDocumentAndGetInfo(data);
            fail();
        } catch (DocumentStorageException exception) {
            assertEquals(documentStorageException, exception.getCause());
        }

        verifyPrivate(classUnderTest, Mockito.times(1)).invoke("storeDocument", data);
    }

    @Test(expected = DocumentStorageException.class)
    public void givenStoreDocumentReturnsNon200HttpStatus_whenStoreDocumentAndGetInfo_thenThrowDocumentStorageException() throws Exception {
        final byte[] data = {1};
        final FileUploadResponse fileUploadResponse = new FileUploadResponse(HttpStatus.SERVICE_UNAVAILABLE);

        doReturn(fileUploadResponse).when(classUnderTest,
                MemberMatcher.method(EvidenceManagementServiceImpl.class, "storeDocument", byte[].class))
                .withArguments(data);

        classUnderTest.storeDocumentAndGetInfo(data);

        verifyPrivate(classUnderTest, Mockito.times(1)).invoke("storeDocument", data);
    }

    @Test
    public void givenStoreDocumentReturns200HttpStatus_whenStoreDocumentAndGetInfo_thenProceedAsExpected() throws Exception {
        final byte[] data = {1};
        final FileUploadResponse expected = new FileUploadResponse(HttpStatus.OK);

        doReturn(expected).when(classUnderTest,
                MemberMatcher.method(EvidenceManagementServiceImpl.class, "storeDocument", byte[].class))
                .withArguments(data);

        FileUploadResponse actual = classUnderTest.storeDocumentAndGetInfo(data);

        assertEquals(expected, actual);

        verifyPrivate(classUnderTest, Mockito.times(1)).invoke("storeDocument", data);
    }

    @Test
    public void givenDocument_whenStoreDocument_thenProceedAsExpected() throws Exception {
        final byte[] document = {1};
        final String serviceToken = "serviceToken";
        final FileUploadResponse expected = new FileUploadResponse(HttpStatus.OK);

        final ParameterizedTypeReference<List<FileUploadResponse>> parameterizedTypeReference =
                new ParameterizedTypeReference<List<FileUploadResponse>>() {};

        final ResponseEntity responseEntity = new ResponseEntity(Collections.singletonList(expected), HttpStatus.OK);

        final LinkedMultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
        final HttpHeaders httpHeaders = new HttpHeaders();

        final HttpEntity httpEntity = new HttpEntity<>(request, httpHeaders);

        doNothing().when(NullOrEmptyValidator.class);
        NullOrEmptyValidator.requireNonEmpty(document);
        doReturn(request).when(classUnderTest,
                MemberMatcher.method(EvidenceManagementServiceImpl.class, "buildRequest", byte[].class, String.class))
                .withArguments(document, DEFAULT_NAME_FOR_PDF_FILE);
        doReturn(serviceToken).when(serviceTokenGenerator).generate();
        doReturn(httpHeaders).when(classUnderTest,
                MemberMatcher.method(EvidenceManagementServiceImpl.class, "getHttpHeaders", String.class))
                .withArguments(serviceToken);
        doReturn(responseEntity).when(restTemplate).exchange(EVIDENCE_MANAGEMENT_ENDPOINT, HttpMethod.POST, httpEntity,
                parameterizedTypeReference);

        FileUploadResponse actual = storeDocument(document);

        assertEquals(expected, actual);

        verifyStatic(NullOrEmptyValidator.class);
        NullOrEmptyValidator.requireNonEmpty(document);
        verifyPrivate(classUnderTest, Mockito.times(1)).invoke("buildRequest", document,
                DEFAULT_NAME_FOR_PDF_FILE);
        Mockito.verify(serviceTokenGenerator, Mockito.times(1)).generate();
        verifyPrivate(classUnderTest, Mockito.times(1)).invoke("getHttpHeaders", serviceToken);
        Mockito.verify(restTemplate, Mockito.times(1)).exchange(EVIDENCE_MANAGEMENT_ENDPOINT,
                HttpMethod.POST, httpEntity, parameterizedTypeReference);
    }

    @Test
    public void givenAuthToken_whenGetHttpHeaders_thenReturnHeaders() {
        final String authToken = "authToken";

        HttpHeaders actual = getHttpHeaders(authToken);

        assertEquals(authToken, actual.get(AUTHORIZATION_HEADER).get(0));
        assertEquals(MediaType.MULTIPART_FORM_DATA, actual.getContentType());
    }

    @Test
    public void givenDocumentAndFileName_whenGetHttpHeaders_thenReturnHeadersMap() {
        final byte[] document = {1};

        final LinkedMultiValueMap<String, Object> actual = buildRequest(document, DEFAULT_NAME_FOR_PDF_FILE);

        assertEquals(1, actual.size());

        HttpEntity<Resource> httpEntity = (HttpEntity<Resource>) actual.get(FILE_PARAMETER).get(0);

        assertEquals(MediaType.APPLICATION_PDF, httpEntity.getHeaders().getContentType());
        assertEquals(document, ((ByteArrayResource) httpEntity.getBody()).getByteArray());
        assertEquals(DEFAULT_NAME_FOR_PDF_FILE, httpEntity.getBody().getFilename());

    }

    private FileUploadResponse storeDocument(byte[] document) {
        try {
            return Whitebox.invokeMethod(classUnderTest, "storeDocument", document);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HttpHeaders getHttpHeaders(String authToken) {
        try {
            return Whitebox.invokeMethod(classUnderTest, "getHttpHeaders", authToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private LinkedMultiValueMap<String, Object> buildRequest(byte[] document, String filename) {
        try {
            return Whitebox.invokeMethod(classUnderTest, "buildRequest", document, filename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}