package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplateConfiguration;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.FileUploadResponse;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;
import uk.gov.hmcts.reform.divorce.documentgenerator.factory.PDFGenerationFactory;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.EvidenceManagementService;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID;

@RunWith(MockitoJUnitRunner.class)
public class DocumentManagementServiceImplUTest {

    private static final String COE_TEMPLATE = "FL-DIV-GNO-ENG-00020.docx";
    private static final String DECREE_NISI_TEMPLATE = "FL-DIV-GNO-ENG-00021.docx";
    private static final String COSTS_ORDER_TEMPLATE = "FL-DIV-DEC-ENG-00060.docx";
    private static final String CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID = "FL-DIV-GNO-ENG-00059.docx";
    private static final String AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_TEMPLATE_ID = "FL-DIV-LET-ENG-00075.doc";
    private static final String AOS_OFFLINE_2_YEAR_SEPARATION_FORM_TEMPLATE_ID = "FL-DIV-APP-ENG-00080.docx";
    private static final String AOS_OFFLINE_5_YEAR_SEPARATION_FORM_TEMPLATE_ID = "FL-DIV-APP-ENG-00081.docx";
    private static final String AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_TEMPLATE_ID = "FL-DIV-APP-ENG-00082.docx";
    private static final String AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_TEMPLATE_ID = "FL-DIV-APP-ENG-00083.docx";
    private static final String AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_TEMPLATE_ID = "FL-DIV-APP-ENG-00084.docx";
    private static final String AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_TEMPLATE_ID = "FL-DIV-LET-ENG-00076.doc";

    private static final String A_TEMPLATE = "a-certain-template";
    private static final String A_TEMPLATE_FILE_NAME = "fileName.pdf";
    private static final String TEST_AUTH_TOKEN = "someToken";
    private static final byte[] TEST_GENERATED_DOCUMENT = new byte[] {1};

    private static final FileUploadResponse EXPECTED_FILE_UPLOAD_RESPONSE = new FileUploadResponse(HttpStatus.OK);

    @Rule
    public ExpectedException expectedException = none();

    @Mock
    private PDFGenerationFactory pdfGenerationFactory;

    @Mock
    private PDFGenerationServiceImpl pdfGenerationService;//TODO - this is wrong, but it passes the tests for now

    @Mock
    private EvidenceManagementService evidenceManagementService;

    @Mock
    private TemplateConfiguration templateConfiguration;

    @InjectMocks
    private DocumentManagementServiceImpl classUnderTest;

    @Before
    public void setUp() {
        EXPECTED_FILE_UPLOAD_RESPONSE.setFileUrl("someUrl");
        EXPECTED_FILE_UPLOAD_RESPONSE.setMimeType("someMimeType");
        EXPECTED_FILE_UPLOAD_RESPONSE.setCreatedOn("someCreatedDate");//TODO - builder?
    }

    @Test
    public void givenTemplateNameIsInvalid_whenGenerateAndStoreDocument_thenThrowException() {
        String unknownTemplateName = "unknown-template";
        when(templateConfiguration.getFileNameByTemplateName(unknownTemplateName)).thenThrow(new IllegalArgumentException("Unknown template: " + unknownTemplateName));

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(equalTo("Unknown template: " + unknownTemplateName));

        classUnderTest.generateAndStoreDocument(unknownTemplateName, new HashMap<>(), "some-auth-token");
    }

    @Test
    public void givenTemplateNameIsAosInvitation_whenGenerateAndStoreDocument_thenProceedAsExpected() {
        when(pdfGenerationFactory.getGeneratorService(A_TEMPLATE)).thenReturn(pdfGenerationService);
        when(pdfGenerationService.generate(eq(A_TEMPLATE), any())).thenReturn(TEST_GENERATED_DOCUMENT);
        when(templateConfiguration.getFileNameByTemplateName(A_TEMPLATE)).thenReturn(A_TEMPLATE_FILE_NAME);
        when(evidenceManagementService.storeDocumentAndGetInfo(eq(TEST_GENERATED_DOCUMENT), eq(TEST_AUTH_TOKEN), eq(A_TEMPLATE_FILE_NAME))).thenReturn(EXPECTED_FILE_UPLOAD_RESPONSE);

        GeneratedDocumentInfo generatedDocumentInfo = classUnderTest.generateAndStoreDocument(A_TEMPLATE, new HashMap<>(), TEST_AUTH_TOKEN);

        assertGeneratedDocumentInfoIsAsExpected(generatedDocumentInfo);
        verify(evidenceManagementService).storeDocumentAndGetInfo(eq(TEST_GENERATED_DOCUMENT), eq(TEST_AUTH_TOKEN), eq(A_TEMPLATE_FILE_NAME));
    }

    //TODO - what being tested here? can I move some of it to the factory test?
    //TODO - this class urgently needs to be cleaned up
    //TODO - maybe I should start by writing more sensible tests instead of these

    @Test
    public void whenStoreDocument_thenProceedAsExpected() {
        when(evidenceManagementService.storeDocumentAndGetInfo(TEST_GENERATED_DOCUMENT, TEST_AUTH_TOKEN, A_TEMPLATE_FILE_NAME)).thenReturn(EXPECTED_FILE_UPLOAD_RESPONSE);

        GeneratedDocumentInfo generatedDocumentInfo = classUnderTest.storeDocument(TEST_GENERATED_DOCUMENT, TEST_AUTH_TOKEN, A_TEMPLATE_FILE_NAME);

        assertGeneratedDocumentInfoIsAsExpected(generatedDocumentInfo);
        verify(evidenceManagementService).storeDocumentAndGetInfo(TEST_GENERATED_DOCUMENT, TEST_AUTH_TOKEN, A_TEMPLATE_FILE_NAME);
    }

    @Captor
    private ArgumentCaptor<Map<String, Object>> payloadCaptor;

    @Test
    public void givenPdfGeneratorIsUsed_whenGenerateDocumentWithHtmlCharacters_thenEscapeHtmlCharacters() {
        final Map<String, Object> placeholderMap = new HashMap<>();
        placeholderMap.put("htmlValue", "<b>This should be escaped</b>");

        when(pdfGenerationFactory.getGeneratorService(A_TEMPLATE)).thenReturn(pdfGenerationService);
        when(pdfGenerationService.generate(eq(A_TEMPLATE), any())).thenReturn(TEST_GENERATED_DOCUMENT);
        //TODO - what tests that the formatter is not called? check coverage

        byte[] generatedDocument = classUnderTest.generateDocument(A_TEMPLATE, placeholderMap);

        assertThat(generatedDocument, equalTo(TEST_GENERATED_DOCUMENT));
        verify(pdfGenerationFactory).getGeneratorService(A_TEMPLATE);
        verify(pdfGenerationService).generate(eq(A_TEMPLATE), payloadCaptor.capture());
        assertThat(payloadCaptor.getValue(), hasEntry("htmlValue", "&lt;b&gt;This should be escaped&lt;/b&gt;"));
    }

    //TODO - these seem to do a more sensible job and actually just use proper mocks to assert the same thing the ones above do
    @Test
    public void whenGenerateCoEDocumentWithDocmosis_thenProceedAsExpected() {
        assertDocumentGenerated(COE_TEMPLATE);
    }

    @Test
    public void whenGenerateDecreeNisiDocumentWithDocmosis_thenProceedAsExpected() {
        assertDocumentGenerated(DECREE_NISI_TEMPLATE);
    }

    @Test
    public void whenGenerateCostsOrderDocumentWithDocmosis_thenProceedAsExpected() {
        assertDocumentGenerated(COSTS_ORDER_TEMPLATE);
    }

    @Test
    public void whenGenerateDNAnswersDocumentWithDocmosis_thenProceedAsExpected() {
        assertDocumentGenerated(DN_ANSWERS_TEMPLATE_ID);
    }

    @Test
    public void whenGenerateCaseListForPronouncementWithDocmosis_thenProceedAsExpected() {
        assertDocumentGenerated(CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID);
    }

    @Test
    public void whenGenerateNameIsAOSOfflineInvitationLetterRespondentWithDocmosis_thenProceedAsExpected() {
        assertDocumentGenerated(AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_TEMPLATE_ID);
    }

    @Test
    public void whenGenerateNameIsAOSOfflineInvitationLetterCoRespondentWithDocmosis_thenProceedAsExpected() {
        assertDocumentGenerated(AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_TEMPLATE_ID);
    }

    @Test
    public void whenGenerateNameIsAOSOffline2YearSeparationFormWithDocmosis_thenProceedAsExpected() {
        assertDocumentGenerated(AOS_OFFLINE_2_YEAR_SEPARATION_FORM_TEMPLATE_ID);
    }

    @Test
    public void whenGenerateNameIsAOSOffline5YearSeparationFormWithDocmosis_thenProceedAsExpected() {
        assertDocumentGenerated(AOS_OFFLINE_5_YEAR_SEPARATION_FORM_TEMPLATE_ID);
    }

    @Test
    public void whenGenerateNameIsAOSOfflineBehaviourDesertionFormWithDocmosis_thenProceedAsExpected() {
        assertDocumentGenerated(AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_TEMPLATE_ID);
    }

    @Test
    public void whenGenerateNameIsAOSOfflineAdulteryFormRespondentWithDocmosis_thenProceedAsExpected() {
        assertDocumentGenerated(AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_TEMPLATE_ID);
    }

    @Test
    public void whenGenerateNameIsAOSOfflineAdulteryFormCoRespondentWithDocmosis_thenProceedAsExpected() {
        assertDocumentGenerated(AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_TEMPLATE_ID);
    }

    private void assertDocumentGenerated(String templateId) {
        final byte[] expected = {1};
        final Map<String, Object> placeholderMap = emptyMap();

        when(pdfGenerationFactory.getGeneratorService(templateId)).thenReturn(pdfGenerationService);
        when(pdfGenerationService.generate(templateId, placeholderMap)).thenReturn(expected);

        byte[] actual = classUnderTest.generateDocument(templateId, placeholderMap);

        assertEquals(expected, actual);

        verify(pdfGenerationFactory).getGeneratorService(templateId);
        verify(pdfGenerationService).generate(templateId, placeholderMap);
    }

    //TODO - these seem to repeat the tests above - with the proper mocking
    @Test
    public void whenGenerateDnClarificationOrderWithDocmosis_thenProceedAsExpected() {
        final byte[] expected = {1};
        final Map<String, Object> placeholderMap = emptyMap();

        when(pdfGenerationFactory.getGeneratorService(DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID))
            .thenReturn(pdfGenerationService);
        when(pdfGenerationService.generate(DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID, placeholderMap))
            .thenReturn(expected);

        byte[] actual = classUnderTest.generateDocument(DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID, placeholderMap);

        assertEquals(expected, actual);

        verify(pdfGenerationFactory)
            .getGeneratorService(DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID);
        verify(pdfGenerationService)
            .generate(DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID, placeholderMap);
    }

    @Test
    public void whenGenerateDnRefusalOrderWithDocmosis_thenProceedAsExpected() {
        final byte[] expected = {1};
        final Map<String, Object> placeholderMap = emptyMap();

        when(pdfGenerationFactory.getGeneratorService(DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID))
            .thenReturn(pdfGenerationService);
        when(pdfGenerationService.generate(DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID, placeholderMap))
            .thenReturn(expected);

        byte[] actual = classUnderTest.generateDocument(DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID, placeholderMap);

        assertEquals(expected, actual);

        verify(pdfGenerationFactory)
            .getGeneratorService(DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID);
        verify(pdfGenerationService)
            .generate(DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID, placeholderMap);
    }

    private void assertGeneratedDocumentInfoIsAsExpected(GeneratedDocumentInfo generatedDocumentInfo) {
        assertThat(generatedDocumentInfo.getUrl(), equalTo("someUrl"));
        assertThat(generatedDocumentInfo.getMimeType(), equalTo("someMimeType"));
        assertThat(generatedDocumentInfo.getCreatedOn(), equalTo("someCreatedDate"));
    }

}
