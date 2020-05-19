package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import com.google.common.collect.ImmutableMap;
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
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplatesConfiguration;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.TemplateNameConfiguration;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.FileUploadResponse;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;
import uk.gov.hmcts.reform.divorce.documentgenerator.factory.PDFGenerationFactory;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.EvidenceManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.PDFGenerationService;

import java.util.HashMap;

import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;
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

    @Rule
    public ExpectedException expectedException = none();

    @Mock
    private PDFGenerationFactory pdfGenerationFactory;

    @Mock
    private PDFGenerationService pdfGenerationService;

    @Mock
    private EvidenceManagementService evidenceManagementService;

    @Mock
    private TemplatesConfiguration templatesConfiguration;

    @Mock
    private TemplateNameConfiguration templateNameConfiguration;

    @InjectMocks
    private DocumentManagementServiceImpl classUnderTest;

    @Captor
    ArgumentCaptor<Map<String, Object>> placeHolderCaptor;

    @Before
    public void setUp() {
        expectedFileUploadResponse = new FileUploadResponse(HttpStatus.OK);
        expectedFileUploadResponse.setFileUrl("someUrl");
        expectedFileUploadResponse.setMimeType("someMimeType");
        expectedFileUploadResponse.setCreatedOn("someCreatedDate");
    public void before() {
        mockStatic(GeneratedDocumentInfoMapper.class, HtmlFieldFormatter.class);
    }

    @Test
    public void divorceminiPetition_thenProceedAsExpectedWelsh() throws Exception {
        assertGenerateAndStoreDocument(D8_PETITION_WELSH_TEMPLATE, MINI_PETITION_NAME_FOR_WELSH_PDF_FILE);
    }

    @Test
    public void aosInvition_thenProceedAsExpectedWelsh() throws Exception {
        assertGenerateAndStoreDocument("FL-DIV-LET-WEL-00257.docx", "AOSInvitationWelsh.pdf");
    }

    @Test
    public void coRespondentAnswers_thenProceedAsExpectedWelsh() throws Exception {
        assertGenerateAndStoreDocument("FL-DIV-GNO-WEL-00258.docx", "CoRespondentAnswersWelsh.pdf");
    }

    @Test
    public void coRespondentInvitation_thenProceedAsExpectedWelsh() throws Exception {
        assertGenerateAndStoreDocument("FL-DIV-GNO-WEL-00259.docx", "CoRespondentInvitationWelsh.pdf");
    }

    @Test
    public void respondentAnswersWelsh_thenProceedAsExpectedWelsh() throws Exception {
        assertGenerateAndStoreDocument("FL-DIV-GNO-WEL-00260.docx", "RespondentAnswersWelsh.pdf");
    }

    @Test
    public void givenTemplateNameIsInvalid_whenGenerateAndStoreDocument_thenThrowException() {
        mockAndSetClock(Instant.now());
        expectedException.expect(NullPointerException.class);

        templateMap = ImmutableMap.of("unknown-template-id","unknown-template");
        when(templateNameConfiguration.getTemplatesName()).thenReturn(templateMap);

        classUnderTest.setTemplateNameConfiguration(templateNameConfiguration);
        classUnderTest.generateAndStoreDocument("unknown-template", new HashMap<>(), "some-auth-token");

    }

    @Test
    public void givenTemplateNameIsAosInvitation_whenGenerateAndStoreDocument_thenProceedAsExpected() throws Exception {
        final String templateId = "aosinvitation";
        assertGenerateAndStoreDocument(templateId, AOS_INVITATION_NAME_FOR_PDF_FILE);
    }

    @Test
    public void givenTemplateNameIsCoRespondentInvitation_whenGenerateAndStoreDocument_thenProceedAsExpected()
        throws Exception {
        final String templateId = "co-respondentinvitation";
        assertGenerateAndStoreDocument(templateId, CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE);
    }

    @Test
    public void givenTemplateNameIsRespondentAnswers_whenGenerateAndStoreDocument_thenProceedAsExpected()
        throws Exception {
        final String templateId = "respondentAnswers";
        assertGenerateAndStoreDocument(templateId, RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE);
    }

    @Test
    public void givenTemplateNameIsCoRespondentAnswers_whenGenerateAndStoreDocument_thenProceedAsExpected()
        throws Exception {
        final String templateId = "co-respondent-answers";
        assertGenerateAndStoreDocument(templateId, CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE);
    }

    @Test
    public void givenTemplateNameIsMiniPetition_whenGenerateAndStoreDocument_thenProceedAsExpected() throws Exception {
        final String templateId = "divorceminipetition";
        assertGenerateAndStoreDocument(templateId, MINI_PETITION_NAME_FOR_PDF_FILE);
    }

    @Test
    public void givenTemplateNameIsCoE_whenGenerateAndStoreDocument_thenProceedAsExpected() throws Exception {
        assertGenerateAndStoreDocument(COE_TEMPLATE, CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE);
    }

    @Test
    public void coeWelsh_whenGenerateAndStoreDocument_thenProceedAsExpected() throws Exception {
        assertGenerateAndStoreDocument(COE_WELSH_TEMPLATE, CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_WELSH_FILE);
    }

    @Test
    public void givenTemplateNameIsCostsOrder_gands_thenProceedAsExpectedWelsh() throws Exception {
        assertGenerateAndStoreDocument(COSTS_ORDER_WELSH_DOCUMENT_ID,
            COSTS_ORDER_NAME_FOR_PDF_WELSH_FILE);
    }

    @Test
    public void givenTemplateNameIsCostsOrder_whenGenerateAndStoreDocument_thenProceedAsExpected() throws Exception {
        assertGenerateAndStoreDocument(COSTS_ORDER_TEMPLATE, COSTS_ORDER_NAME_FOR_PDF_FILE);
    }

    @Test
    public void givenTemplateNameIsDNAnswers_thenProceedAsExpectedWelsh() throws Exception {
        assertGenerateAndStoreDocument(DN_ANSWERS_WELSH_TEMPLATE_ID, DECREE_NISI_ANSWERS_TEMPLATE_PDF_WELSH_FILE);
    }

    @Test
    public void givenTemplateNameDecreeAbsolute_thenProceedAsExpected() throws Exception {
        assertGenerateAndStoreDocument(DECREE_ABSOLUTE_WELSH_TEMPLATE_ID, DECREE_ABSOLUTE_WELSH_PDF_FILE);
    }

    @Test
    public void givenTemplateNameIsDNAnswers_whenGenerateAndStoreDocument_thenProceedAsExpected() throws Exception {
        assertGenerateAndStoreDocument(DN_ANSWERS_TEMPLATE_ID, DECREE_NISI_ANSWERS_TEMPLATE_NAME);
    }

    @Test
    public void whenGenerateAndStoreDocument_givenTemplateNameIsCaseListForPronouncement_thenProceedAsExpected()
        throws Exception {
        assertGenerateAndStoreDocument(CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID, CASE_LIST_FOR_PRONOUNCEMENT_PDF_FILE);
    }

    @Test
    public void givenTemplateNameIsCaseListForPronouncement_thenProceedAsExpected()
        throws Exception {
        assertGenerateAndStoreDocument(CASE_LIST_FOR_PRONOUNCEMENT_WELSH_TEMPLATE_ID,
            CASE_LIST_FOR_PRONOUNCEMENT_PDF_WELSH_FILE);
    }

    @Test
    public void whenGenerateAndStoreDocument_givenTemplateNameIsDraftMiniPetition_thenProceedAsExpected()
        throws Exception {
        assertGenerateAndStoreDocument(DRAFT_MINI_PETITION_TEMPLATE_ID, DRAFT_MINI_PETITION_NAME_FOR_PDF_FILE);
    }

    @Test
    public void whenGenerateAndStoreDocument_givenTemplateNameIsDecreeNisi_thenProceedAsExpected() throws Exception {
        assertGenerateAndStoreDocument(DECREE_NISI_TEMPLATE, DECREE_NISI_NAME_FOR_PDF_FILE);
    }

    @Test
    public void givenTemplateNameIsDecreeNisi_thenProceedAsExpectedWelsh() throws Exception {
        assertGenerateAndStoreDocument(DECREE_NISI_WELSH_TEMPLATE, DECREE_NISI_NAME_FOR_PDF_WELSH_FILE);
    }

    @Test
    public void whenGenerateAndStoreDocument_givenTemplateNameIsAOSOfflineInvitationLetterResp_thenProceedAsExpected()
        throws Exception {
        assertGenerateAndStoreDocument(
            AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_TEMPLATE_ID,
            AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_NAME_FOR_PDF_FILE
        );
    }

    @Test
    public void givenTemplateNameIsAOSOfflineInvitationLetterResp_thenProceedAsExpectedWelsh()
        throws Exception {
        assertGenerateAndStoreDocument(
            AOS_OFFLINE_INV_LET_RESPONDENT_WELSH_TEMPLATE_ID,
            AOS_OFFLINE_INV_LET_RESPONDENT_NAME_FOR_PDF_WELSH_FILE
        );
    }

    @Test
    public void whenGenerateAndStoreDocument_givenTemplateNameIsAOSOfflineInvitationLetterCoResp_thenProceedAsExpected()
        throws Exception {
        assertGenerateAndStoreDocument(
            AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_TEMPLATE_ID,
            AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_NAME_FOR_PDF_FILE
        );
    }

    @Test
    public void givenTemplateNameIsAOSOfflineInvitationLetterCoResp_thenProceedAsExpectedWelsh()
        throws Exception {
        assertGenerateAndStoreDocument(
            AOS_OFFLINE_INV_LET_RESPONDENT_WELSH_TEMPLATE_ID,
            AOS_OFFLINE_INV_LET_RESPONDENT_NAME_FOR_PDF_WELSH_FILE
        );
    }

    @Test
    public void whenGenerateAndStoreDocument_givenTemplateNameIsAOSOffline2YearSeparationForm_thenProceedAsExpected()
        throws Exception {
        assertGenerateAndStoreDocument(
            AOS_OFFLINE_2_YEAR_SEPARATION_FORM_TEMPLATE_ID,
            AOS_OFFLINE_2_YEAR_SEPARATION_FORM_NAME_FOR_PDF_FILE
        );
    }

    @Test
    public void givenTemplateNameIsAOSOffline2YearSeparationForm_thenProceedAsExpectedWelsh()
        throws Exception {
        assertGenerateAndStoreDocument(
            AOS_OFFLINE_2_YEAR_SEPARATION_FORM_WELSH_TEMPLATE_ID,
            AOS_OFFLINE_2_YEAR_SEPARATION_FORM_NAME_FOR_PDF_WELSH_FILE
        );
    }

    @Test
    public void givenTemplateNameIsAOSOffline5YearSeparationForm_thenProceedAsExpectedWelsh()
        throws Exception {
        assertGenerateAndStoreDocument(
            AOS_OFFLINE_5_YEAR_SEP_FORM_WELSH_TEMPLATE_ID,
            AOS_OFFLINE_5_YEAR_SEP_FORM_NAME_FOR_PDF_WELSH_FILE
        );
    }

    @Test
    public void givenTemplateNameIsAOSOfflineBehaviourDesertionForm_thenProceedAsExpectedWelsh()
        throws Exception {
        assertGenerateAndStoreDocument(
            AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_WELSH_TEMPLATE_ID,
            AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_NAME_FOR_PDF_WELSH_FILE
        );
    }

    @Test
    public void whenGenerateAndStoreDocument_givenTemplateNameIsAOSOfflineBehaviourDesertionForm_thenProceedAsExpected()
        throws Exception {
        assertGenerateAndStoreDocument(
            AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_TEMPLATE_ID,
            AOS_OFFLINE_BEHAVIOUR_DESERTION_FORM_NAME_FOR_PDF_FILE
        );
    }

    @Test
    public void givenTemplateNameIsAOSOfflineAdulteryRespondent_thenProceedAsExpectedWelsh()
        throws Exception {
        assertGenerateAndStoreDocument(
            AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_WELSH_TEMPLATE_ID,
            AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_NAME_FOR_PDF_WELSH_FILE
        );
    }

    @Test
    public void whenGenerateAndStoreDocument_givenTemplateNameIsAOSOfflineAdulteryRespondent_thenProceedAsExpected()
        throws Exception {
        assertGenerateAndStoreDocument(
            AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_TEMPLATE_ID,
            AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_NAME_FOR_PDF_FILE
        );
    }

    @Test
    public void givenTemplateNameIsAOSOfflineAdulteryCoRespondent_thenProceedAsExpectedWelsh()
        throws Exception {
        assertGenerateAndStoreDocument(
            AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_WELSH_TEMPLATE_ID,
            AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_NAME_FOR_PDF_WELSH_FILE
        );
    }

    @Test
    public void whenGenerateAndStoreDocument_givenTemplateNameIsAOSOfflineAdulteryCoRespondent_thenProceedAsExpected()
        throws Exception {
        assertGenerateAndStoreDocument(
            AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_TEMPLATE_ID,
            AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_NAME_FOR_PDF_FILE
        );
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
    public void givenTemplateIsDnRefusalOrder_thenProceedAsExpectedWelsh() throws Exception {
        final DocumentManagementServiceImpl classUnderTest = spy(new DocumentManagementServiceImpl());

        final byte[] data = {1};
        final String templateName = DN_REFUSAL_ORDER_REJECTION_WELSH_TEMPLATE_ID;
        final Map<String, Object> placeholderMap = new HashMap<>();
        final GeneratedDocumentInfo expected = new GeneratedDocumentInfo();
        final Instant instant = Instant.now();
        final String authToken = "someToken";

        expected.setCreatedOn("someCreatedDate");
        expected.setMimeType("someMimeType");
        expected.setUrl("someUrl");

        mockAndSetClock(instant);
        templateMap = ImmutableMap.of(templateName,DN_REFUSAL_ORDER_REJECTION_NAME_FOR_PDF_WELSH_FILE);
        when(templateNameConfiguration.getTemplatesName()).thenReturn(templateMap);

        doReturn(data).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
            "generateDocument", String.class, Map.class)).withArguments(templateName, placeholderMap);
        doReturn(expected).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
            "storeDocument", byte[].class, String.class, String.class))
            .withArguments(data, authToken, DN_REFUSAL_ORDER_REJECTION_NAME_FOR_PDF_WELSH_FILE);

        classUnderTest.setTemplateNameConfiguration(templateNameConfiguration);
        GeneratedDocumentInfo actual = classUnderTest.generateAndStoreDocument(templateName, placeholderMap, authToken);

        assertEquals(expected, actual);

        verifyPrivate(classUnderTest, Mockito.times(1))
            .invoke("generateDocument", templateName, placeholderMap);
        verifyPrivate(classUnderTest, Mockito.times(1))
            .invoke("storeDocument", data, authToken, DN_REFUSAL_ORDER_REJECTION_NAME_FOR_PDF_WELSH_FILE);
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
        when(templatesConfiguration.getFileNameByTemplateName(unknownTemplateName))
            .thenThrow(new IllegalArgumentException("Unknown template: " + unknownTemplateName));

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(equalTo("Unknown template: " + unknownTemplateName));

        classUnderTest.generateAndStoreDocument(unknownTemplateName, new HashMap<>(), "some-auth-token");
    }

    private void assertGeneratedDocumentInfoIsAsExpected(GeneratedDocumentInfo generatedDocumentInfo) {
        assertThat(generatedDocumentInfo.getUrl(), equalTo("someUrl"));
        assertThat(generatedDocumentInfo.getMimeType(), equalTo("someMimeType"));
        assertThat(generatedDocumentInfo.getCreatedOn(), equalTo("someCreatedDate"));
    }

}
