package uk.gov.hmcts.reform.divorce.documentgenerator.management.test.stub.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.powermock.reflect.Whitebox;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.FileUploadResponse;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class EvidenceManagementServiceStubUTest {
    private static final String TEST_DOCUMENTS_DOWNLOAD_CONTEXT_PATH = "/test/documents/";
    private static final String CREATED_BY = "Document Generator Service";

    private static final String EXISTING_FILE_KEY = "EXISTING_FILE_KEY";
    private static final String NON_EXISTING_FILE_KEY = "NON_EXISTING_FILE_KEY";
    private static final String NEW_FILE_NAME = "493410b3-dd0b-4b78-97bf-289f50f6e74f";
    private static final String EXPECTED_FILE_URL_PREFIX = "http://localhost" + TEST_DOCUMENTS_DOWNLOAD_CONTEXT_PATH;
    private static final String EXPECTED_FILE_URL = EXPECTED_FILE_URL_PREFIX + NEW_FILE_NAME;

    private static final byte[] FILE = {1};
    private static final byte[] NEW_FILE = {2};
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private Map<String, byte[]> dataStore;

    private final EvidenceManagementServiceStub classUnderTest = new EvidenceManagementServiceStub();

    @Before
    public void before() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        dataStore = Whitebox.getInternalState(classUnderTest.getClass(), "DATA_STORE");
        dataStore.put(EXISTING_FILE_KEY, FILE);
    }

    @Test
    @Disabled
    public void givenADocument_whenStoreDocumentAndGetInfo_thenReturnInfo() {
        final String testFileName = "testFileName";
        final Instant instant = Instant.now();
        mockAndSetClock(instant);

        dataStore.clear();

        FileUploadResponse actual = classUnderTest.storeDocumentAndGetInfo(NEW_FILE, "testToken", testFileName);

        Map.Entry<String, byte[]> entry = dataStore.entrySet().iterator().next();

        String fileName = entry.getKey();

        assertEquals(testFileName, fileName);
        assertEquals(NEW_FILE, entry.getValue());
        assertEquals(EXPECTED_FILE_URL_PREFIX + fileName, actual.getFileUrl());
        assertEquals(MediaType.APPLICATION_PDF_VALUE, actual.getMimeType());
        assertEquals(instant.toString(), actual.getCreatedOn());
        assertEquals(CREATED_BY, actual.getCreatedBy());
    }

    @Test
    public void givenAFileName_whenGetFileURL_thenReturnServerURL() {
        assertEquals(EXPECTED_FILE_URL, getFileURL(NEW_FILE_NAME));
    }

    @Test
    public void givenDocumentDoesNotExist_whenGetDocument_thenReturnNull() {
        assertNull(classUnderTest.getDocument(NON_EXISTING_FILE_KEY));
    }

    @Test
    public void givenDocumentExist_whenGetDocument_thenReturnDocument() {
        assertEquals(FILE, classUnderTest.getDocument(EXISTING_FILE_KEY));
    }

    private void mockAndSetClock(Instant instant) {
        final Clock clock = mock(Clock.class);
        when(clock.instant()).thenReturn(instant);

        Whitebox.setInternalState(classUnderTest, "clock", clock);
    }

    private String getFileURL(String fileName) {
        try {
            return Whitebox.invokeMethod(classUnderTest, "getFileURL", fileName);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
