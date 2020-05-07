package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplateConfiguration;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.FileUploadResponse;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;
import uk.gov.hmcts.reform.divorce.documentgenerator.factory.PDFGenerationFactory;
import uk.gov.hmcts.reform.divorce.documentgenerator.mapper.GeneratedDocumentInfoMapper;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.EvidenceManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.HtmlFieldFormatter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_ANSWERS_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID;

//@PowerMockIgnore("com.microsoft.applicationinsights.*")
//@RunWith(PowerMockRunner.class)
//@PrepareForTest( {GeneratedDocumentInfoMapper.class, HtmlFieldFormatter.class, DocumentManagementServiceImpl.class})
@RunWith(MockitoJUnitRunner.class)
public class DocumentManagementServiceImplUTest {

    private static final String A_TEMPLATE = "divorceminipetition";
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

    private static final String TEST_AUTH_TOKEN = "someToken";
    private static final String A_TEMPLATE_FILE_NAME = "fileName.pdf";
    private static final byte[] TEST_GENERATED_DOCUMENT = new byte[] {1};

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

    @Test
    public void givenTemplateNameIsInvalid_whenGenerateAndStoreDocument_thenThrowException() {
        String unknownTemplateName = "unknown-template";
        when(pdfGenerationFactory.getGeneratorService(unknownTemplateName)).thenReturn(pdfGenerationService);
        when(templateConfiguration.getFileNameByTemplateName(unknownTemplateName)).thenThrow(new IllegalArgumentException("Unknown template: " + unknownTemplateName));

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(equalTo("Unknown template: " + unknownTemplateName));

        classUnderTest.generateAndStoreDocument(unknownTemplateName, new HashMap<>(), "some-auth-token");
    }

    @Test
    public void givenTemplateNameIsAosInvitation_whenGenerateAndStoreDocument_thenProceedAsExpected() {
        FileUploadResponse fileUploadResponse = new FileUploadResponse(HttpStatus.OK);
        fileUploadResponse.setFileUrl("someUrl");
        fileUploadResponse.setMimeType("someMimeType");
        fileUploadResponse.setCreatedOn("someCreatedDate");

        when(pdfGenerationFactory.getGeneratorService(A_TEMPLATE)).thenReturn(pdfGenerationService);
        when(pdfGenerationService.generate(eq(A_TEMPLATE), any())).thenReturn(TEST_GENERATED_DOCUMENT);
        when(templateConfiguration.getFileNameByTemplateName(A_TEMPLATE)).thenReturn(A_TEMPLATE_FILE_NAME);
        when(evidenceManagementService.storeDocumentAndGetInfo(eq(TEST_GENERATED_DOCUMENT), eq(TEST_AUTH_TOKEN), eq(A_TEMPLATE_FILE_NAME))).thenReturn(fileUploadResponse);

        GeneratedDocumentInfo generatedDocumentInfo = classUnderTest.generateAndStoreDocument(A_TEMPLATE, new HashMap<>(), TEST_AUTH_TOKEN);

        assertThat(generatedDocumentInfo.getUrl(), equalTo("someUrl"));
        assertThat(generatedDocumentInfo.getMimeType(), equalTo("someMimeType"));
        assertThat(generatedDocumentInfo.getCreatedOn(), equalTo("someCreatedDate"));
        verify(evidenceManagementService).storeDocumentAndGetInfo(eq(TEST_GENERATED_DOCUMENT), eq(TEST_AUTH_TOKEN), eq(A_TEMPLATE_FILE_NAME));
    }

    //TODO - these tests shouldn't really be passing - the fact they are mean they're probably overly mocked

    //TODO - what being tested here? can I move some of it to the factory test?
    //TODO - this class urgently needs to be cleaned up
    //TODO - maybe I should start by writing more sensible tests instead of these

    @Test
    public void whenStoreDocument_thenProceedAsExpected() {
        final byte[] data = {1};
        final String filename = "someFileName";
        final FileUploadResponse fileUploadResponse = new FileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo expected = new GeneratedDocumentInfo();

        when(evidenceManagementService.storeDocumentAndGetInfo(data, "test", filename)).thenReturn(fileUploadResponse);
        when(GeneratedDocumentInfoMapper.mapToGeneratedDocumentInfo(fileUploadResponse)).thenReturn(expected);

        GeneratedDocumentInfo actual = classUnderTest.storeDocument(data, "test", filename);

        assertEquals(expected, actual);

        verify(evidenceManagementService, Mockito.times(1)).storeDocumentAndGetInfo(data, "test", filename);
        verifyStatic(GeneratedDocumentInfoMapper.class);
        GeneratedDocumentInfoMapper.mapToGeneratedDocumentInfo(fileUploadResponse);
    }

    //TODO - this seems to be a bit different - I think it also checks that the HtmlFieldFormatter was called
    @Test
    public void whenGenerateDocument_thenProceedAsExpected() {
        final byte[] expected = {1};
        final Map<String, Object> placeholderMap = emptyMap();
        final Map<String, Object> formattedPlaceholderMap = Collections.singletonMap("SomeThing", new Object());

        when(HtmlFieldFormatter.format(placeholderMap)).thenReturn(formattedPlaceholderMap);
        when(pdfGenerationFactory.getGeneratorService(A_TEMPLATE)).thenReturn(pdfGenerationService);
        when(pdfGenerationService.generate(A_TEMPLATE, formattedPlaceholderMap)).thenReturn(expected);

        byte[] actual = classUnderTest.generateDocument(A_TEMPLATE, placeholderMap);

        assertEquals(expected, actual);//TODO - leave this for now until tests are refactored - I think this will fix this test

        verifyStatic(HtmlFieldFormatter.class);
        HtmlFieldFormatter.format(placeholderMap);
        verify(pdfGenerationFactory, Mockito.times(1))
            .getGeneratorService(A_TEMPLATE);
        verify(pdfGenerationService, Mockito.times(1))
            .generate(A_TEMPLATE, formattedPlaceholderMap);
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

        verify(pdfGenerationFactory, Mockito.times(1)).getGeneratorService(templateId);
        verify(pdfGenerationService, Mockito.times(1)).generate(templateId, placeholderMap);
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

        verify(pdfGenerationFactory, Mockito.times(1))
            .getGeneratorService(DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID);
        verify(pdfGenerationService, Mockito.times(1))
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

        verify(pdfGenerationFactory, Mockito.times(1))
            .getGeneratorService(DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID);
        verify(pdfGenerationService, Mockito.times(1))
            .generate(DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID, placeholderMap);
    }
}
