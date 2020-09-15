package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplatesConfiguration;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.FileUploadResponse;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;
import uk.gov.hmcts.reform.divorce.documentgenerator.factory.PDFGenerationFactory;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.EvidenceManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.PDFGenerationService;

import java.util.HashMap;

import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentManagementServiceImplUTest {

    private static final String A_TEMPLATE = "a-certain-template";
    private static final String A_TEMPLATE_FILE_NAME = "fileName.pdf";
    private static final String TEST_AUTH_TOKEN = "someToken";
    private static final byte[] TEST_GENERATED_DOCUMENT = new byte[] {1};

    private FileUploadResponse expectedFileUploadResponse;

    @Mock
    private PDFGenerationFactory pdfGenerationFactory;

    @Mock
    private PDFGenerationService pdfGenerationService;

    @Mock
    private EvidenceManagementService evidenceManagementService;

    @Mock
    private TemplatesConfiguration templatesConfiguration;

    @InjectMocks
    private DocumentManagementServiceImpl classUnderTest;

    @Before
    public void setUp() {
        expectedFileUploadResponse = new FileUploadResponse(HttpStatus.OK);
        expectedFileUploadResponse.setFileUrl("someUrl");
        expectedFileUploadResponse.setMimeType("someMimeType");
        expectedFileUploadResponse.setCreatedOn("someCreatedDate");
    }

    @Test
    public void givenTemplateNameIsAosInvitation_whenGenerateAndStoreDocument_thenProceedAsExpected() {
        when(pdfGenerationFactory.getGeneratorService(A_TEMPLATE)).thenReturn(pdfGenerationService);
        when(pdfGenerationService.generate(eq(A_TEMPLATE), any())).thenReturn(TEST_GENERATED_DOCUMENT);
        when(templatesConfiguration.getFileNameByTemplateName(A_TEMPLATE)).thenReturn(A_TEMPLATE_FILE_NAME);
        when(evidenceManagementService.storeDocumentAndGetInfo(eq(TEST_GENERATED_DOCUMENT), eq(TEST_AUTH_TOKEN), eq(A_TEMPLATE_FILE_NAME)))
            .thenReturn(expectedFileUploadResponse);

        GeneratedDocumentInfo generatedDocumentInfo = classUnderTest.generateAndStoreDocument(A_TEMPLATE, new HashMap<>(), TEST_AUTH_TOKEN);

        assertGeneratedDocumentInfoIsAsExpected(generatedDocumentInfo);
        verify(evidenceManagementService).storeDocumentAndGetInfo(eq(TEST_GENERATED_DOCUMENT), eq(TEST_AUTH_TOKEN), eq(A_TEMPLATE_FILE_NAME));
    }

    @Test
    public void givenPdfGeneratorIsUsed_whenGenerateDocumentWithHtmlCharacters_thenEscapeHtmlCharacters() {
        when(pdfGenerationFactory.getGeneratorService(A_TEMPLATE)).thenReturn(pdfGenerationService);
        when(pdfGenerationService.generate(eq(A_TEMPLATE), any())).thenReturn(TEST_GENERATED_DOCUMENT);

        byte[] generatedDocument = classUnderTest.generateDocument(A_TEMPLATE, new HashMap<>());

        assertThat(generatedDocument, equalTo(TEST_GENERATED_DOCUMENT));
        verify(pdfGenerationFactory).getGeneratorService(A_TEMPLATE);
        verify(pdfGenerationService).generate(eq(A_TEMPLATE), eq(emptyMap()));
    }

    @Test
    public void whenStoreDocument_thenProceedAsExpected() {
        when(evidenceManagementService.storeDocumentAndGetInfo(TEST_GENERATED_DOCUMENT, TEST_AUTH_TOKEN, A_TEMPLATE_FILE_NAME))
            .thenReturn(expectedFileUploadResponse);

        GeneratedDocumentInfo generatedDocumentInfo = classUnderTest.storeDocument(TEST_GENERATED_DOCUMENT, TEST_AUTH_TOKEN, A_TEMPLATE_FILE_NAME);

        assertGeneratedDocumentInfoIsAsExpected(generatedDocumentInfo);
        verify(evidenceManagementService).storeDocumentAndGetInfo(TEST_GENERATED_DOCUMENT, TEST_AUTH_TOKEN, A_TEMPLATE_FILE_NAME);
    }

    @Test
    public void givenTemplateNameIsInvalid_whenGenerateAndStoreDocument_thenThrowException() {
        String unknownTemplateName = "unknown-template";
        HashMap<String, Object> placeholders = new HashMap<>();
        when(templatesConfiguration.getFileNameByTemplateName(unknownTemplateName))
            .thenThrow(new IllegalArgumentException("Unknown template: " + unknownTemplateName));

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            classUnderTest.generateAndStoreDocument(unknownTemplateName, placeholders, "some-auth-token");
        });

        assertThat(illegalArgumentException.getMessage(), equalTo("Unknown template: " + unknownTemplateName));
    }

    private void assertGeneratedDocumentInfoIsAsExpected(GeneratedDocumentInfo generatedDocumentInfo) {
        assertThat(generatedDocumentInfo.getUrl(), equalTo("someUrl"));
        assertThat(generatedDocumentInfo.getMimeType(), equalTo("someMimeType"));
        assertThat(generatedDocumentInfo.getCreatedOn(), equalTo("someCreatedDate"));
    }

}
