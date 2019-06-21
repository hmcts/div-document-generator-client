package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.FileUploadResponse;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;
import uk.gov.hmcts.reform.divorce.documentgenerator.factory.PDFGenerationFactory;
import uk.gov.hmcts.reform.divorce.documentgenerator.mapper.GeneratedDocumentInfoMapper;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.EvidenceManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.PDFGenerationService;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.HtmlFieldFormatter;

import java.time.Clock;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.rules.ExpectedException.none;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.PDF_GENERATOR_TYPE;

@PowerMockIgnore("com.microsoft.applicationinsights.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({GeneratedDocumentInfoMapper.class, HtmlFieldFormatter.class, DocumentManagementServiceImpl.class})
public class DocumentManagementServiceImplUTest {

    private static final String MINI_PETITION_NAME_FOR_PDF_FILE = "DivorcePetition.pdf";
    private static final String AOS_INVITATION_NAME_FOR_PDF_FILE = "AOSInvitation.pdf";
    private static final String CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE = "CoRespondentInvitation.pdf";
    private static final String RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE = "RespondentAnswers.pdf";
    private static final String CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE = "CoRespondentAnswers.pdf";
    private static final String CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE = "CertificateOfEntitlement.pdf";
    private static final String COSTS_ORDER_NAME_FOR_PDF_FILE = "CostsOrder.pdf";
    private static final String DECREE_NISI_NAME_FOR_PDF_FILE = "DecreeNisiPronouncement.pdf";
    private static final String A_TEMPLATE = "divorceminipetition";
    private static final String COE_TEMPLATE = "FL-DIV-GNO-ENG-00020.docx";
    private static final String DECREE_NISI_TEMPLATE = "FL-DIV-GNO-ENG-00021.docx";
    private static final String COSTS_ORDER_TEMPLATE = "FL-DIV-DEC-ENG-00060.docx";

    @Rule
    public ExpectedException expectedException = none();

    @Mock
    private PDFGenerationFactory pdfGenerationFactory;

    @Mock
    private PDFGenerationService pdfGenerationService;

    @Mock
    private EvidenceManagementService evidenceManagementService;

    @InjectMocks
    private DocumentManagementServiceImpl classUnderTest;

    @Before
    public void before() {
        mockStatic(GeneratedDocumentInfoMapper.class, HtmlFieldFormatter.class);
    }

    @Test
    public void givenTemplateNameIsInvalid_whenGenerateAndStoreDocument_thenThrowException() {
        mockAndSetClock(Instant.now());
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(equalTo("Unknown template: unknown-template"));

        classUnderTest.generateAndStoreDocument("unknown-template", new HashMap<>(), "some-auth-token");

    }

    @Test
    public void givenTemplateNameIsAosInvitation_whenGenerateAndStoreDocument_thenProceedAsExpected() throws Exception {
        final DocumentManagementServiceImpl classUnderTest = spy(new DocumentManagementServiceImpl());

        final byte[] data = {1};
        final String templateName = "aosinvitation";
        final Map<String, Object> placeholderMap = new HashMap<>();
        final GeneratedDocumentInfo expected = new GeneratedDocumentInfo();
        final Instant instant = Instant.now();
        final String authToken = "someToken";

        expected.setCreatedOn("someCreatedDate");
        expected.setMimeType("someMimeType");
        expected.setUrl("someUrl");

        mockAndSetClock(instant);

        doReturn(data).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
                "generateDocument", String.class, Map.class)).withArguments(templateName, placeholderMap);
        doReturn(expected).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
                "storeDocument", byte[].class, String.class, String.class))
                        .withArguments(data, authToken, AOS_INVITATION_NAME_FOR_PDF_FILE);

        GeneratedDocumentInfo actual = classUnderTest.generateAndStoreDocument(templateName, placeholderMap, authToken);

        assertEquals(expected, actual);

        verifyPrivate(classUnderTest, Mockito.times(1))
                .invoke("generateDocument", templateName, placeholderMap);
        verifyPrivate(classUnderTest, Mockito.times(1))
                .invoke("storeDocument", data, authToken, AOS_INVITATION_NAME_FOR_PDF_FILE);
    }

    @Test
    public void givenTemplateNameIsCoRespondentInvitation_whenGenerateAndStoreDocument_thenProceedAsExpected()
        throws Exception {
        final DocumentManagementServiceImpl classUnderTest = spy(new DocumentManagementServiceImpl());

        final byte[] data = {1};
        final String templateName = "co-respondentinvitation";
        final Map<String, Object> placeholderMap = new HashMap<>();
        final GeneratedDocumentInfo expected = new GeneratedDocumentInfo();
        final Instant instant = Instant.now();
        final String authToken = "someToken";

        expected.setCreatedOn("someCreatedDate");
        expected.setMimeType("someMimeType");
        expected.setUrl("someUrl");

        mockAndSetClock(instant);

        doReturn(data).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
            "generateDocument", String.class, Map.class)).withArguments(templateName, placeholderMap);
        doReturn(expected).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
            "storeDocument", byte[].class, String.class, String.class))
            .withArguments(data, authToken, CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE);

        GeneratedDocumentInfo actual = classUnderTest.generateAndStoreDocument(templateName, placeholderMap, authToken);

        assertEquals(expected, actual);

        verifyPrivate(classUnderTest, Mockito.times(1))
            .invoke("generateDocument", templateName, placeholderMap);
        verifyPrivate(classUnderTest, Mockito.times(1))
            .invoke("storeDocument", data, authToken, CO_RESPONDENT_INVITATION_NAME_FOR_PDF_FILE);
    }

    @Test
    public void givenTemplateNameIsRespondentAnswers_whenGenerateAndStoreDocument_thenProceedAsExpected()
        throws Exception {
        final DocumentManagementServiceImpl classUnderTest = spy(new DocumentManagementServiceImpl());

        final byte[] data = {1};
        final String templateName = "respondentAnswers";
        final Map<String, Object> placeholderMap = new HashMap<>();
        final GeneratedDocumentInfo expected = new GeneratedDocumentInfo();
        final Instant instant = Instant.now();
        final String authToken = "someToken";

        expected.setCreatedOn("someCreatedDate");
        expected.setMimeType("someMimeType");
        expected.setUrl("someUrl");

        mockAndSetClock(instant);

        doReturn(data).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
            "generateDocument", String.class, Map.class)).withArguments(templateName, placeholderMap);
        doReturn(expected).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
            "storeDocument", byte[].class, String.class, String.class))
            .withArguments(data, authToken, RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE);

        GeneratedDocumentInfo actual = classUnderTest.generateAndStoreDocument(templateName, placeholderMap, authToken);

        assertEquals(expected, actual);

        verifyPrivate(classUnderTest, Mockito.times(1))
            .invoke("generateDocument", templateName, placeholderMap);
        verifyPrivate(classUnderTest, Mockito.times(1))
            .invoke("storeDocument", data, authToken, RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE);
    }


    @Test
    public void givenTemplateNameIsCoRespondentAnswers_whenGenerateAndStoreDocument_thenProceedAsExpected()
        throws Exception {
        final DocumentManagementServiceImpl classUnderTest = spy(new DocumentManagementServiceImpl());

        final byte[] data = {1};
        final String templateName = "co-respondent-answers";
        final Map<String, Object> placeholderMap = new HashMap<>();
        final GeneratedDocumentInfo expected = new GeneratedDocumentInfo();
        final Instant instant = Instant.now();
        final String authToken = "someToken";

        expected.setCreatedOn("someCreatedDate");
        expected.setMimeType("someMimeType");
        expected.setUrl("someUrl");

        mockAndSetClock(instant);

        doReturn(data).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
            "generateDocument", String.class, Map.class)).withArguments(templateName, placeholderMap);
        doReturn(expected).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
            "storeDocument", byte[].class, String.class, String.class))
            .withArguments(data, authToken, CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE);

        GeneratedDocumentInfo actual = classUnderTest.generateAndStoreDocument(templateName, placeholderMap, authToken);

        assertEquals(expected, actual);

        verifyPrivate(classUnderTest, Mockito.times(1))
            .invoke("generateDocument", templateName, placeholderMap);
        verifyPrivate(classUnderTest, Mockito.times(1))
            .invoke("storeDocument", data, authToken, CO_RESPONDENT_ANSWERS_NAME_FOR_PDF_FILE);
    }

    @Test
    public void givenTemplateNameIsMiniPetition_whenGenerateAndStoreDocument_thenProceedAsExpected() throws Exception {
        final DocumentManagementServiceImpl classUnderTest = spy(new DocumentManagementServiceImpl());

        final byte[] data = {1};
        final String templateName = "divorceminipetition";
        final Map<String, Object> placeholderMap = new HashMap<>();
        final GeneratedDocumentInfo expected = new GeneratedDocumentInfo();
        final Instant instant = Instant.now();
        final String authToken = "someToken";

        expected.setCreatedOn("someCreatedDate");
        expected.setMimeType("someMimeType");
        expected.setUrl("someUrl");

        mockAndSetClock(instant);

        doReturn(data).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
                "generateDocument", String.class, Map.class)).withArguments(templateName, placeholderMap);
        doReturn(expected).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
                "storeDocument", byte[].class, String.class, String.class))
                        .withArguments(data, authToken, MINI_PETITION_NAME_FOR_PDF_FILE);

        GeneratedDocumentInfo actual = classUnderTest.generateAndStoreDocument(templateName, placeholderMap, authToken);

        assertEquals(expected, actual);

        verifyPrivate(classUnderTest, Mockito.times(1))
                .invoke("generateDocument", templateName, placeholderMap);
        verifyPrivate(classUnderTest, Mockito.times(1))
                .invoke("storeDocument", data, authToken, MINI_PETITION_NAME_FOR_PDF_FILE);
    }

    @Test
    public void givenTemplateNameIsCoE_whenGenerateAndStoreDocument_thenProceedAsExpected() throws Exception {
        final DocumentManagementServiceImpl classUnderTest = spy(new DocumentManagementServiceImpl());

        final byte[] data = {1};
        final String templateName = COE_TEMPLATE;
        final Map<String, Object> placeholderMap = new HashMap<>();
        final GeneratedDocumentInfo expected = new GeneratedDocumentInfo();
        final Instant instant = Instant.now();
        final String authToken = "someToken";

        expected.setCreatedOn("someCreatedDate");
        expected.setMimeType("someMimeType");
        expected.setUrl("someUrl");

        mockAndSetClock(instant);

        doReturn(data).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
            "generateDocument", String.class, Map.class)).withArguments(templateName, placeholderMap);
        doReturn(expected).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
            "storeDocument", byte[].class, String.class, String.class))
            .withArguments(data, authToken, CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE);

        GeneratedDocumentInfo actual = classUnderTest.generateAndStoreDocument(templateName, placeholderMap, authToken);

        assertEquals(expected, actual);

        verifyPrivate(classUnderTest, Mockito.times(1))
            .invoke("generateDocument", templateName, placeholderMap);
        verifyPrivate(classUnderTest, Mockito.times(1))
            .invoke("storeDocument", data, authToken, CERTIFICATE_OF_ENTITLEMENT_NAME_FOR_PDF_FILE);
    }

    @Test
    public void givenTemplateNameIsCostsOrder_whenGenerateAndStoreDocument_thenProceedAsExpected() throws Exception {
        final DocumentManagementServiceImpl classUnderTest = spy(new DocumentManagementServiceImpl());

        final byte[] data = {1};
        final String templateName = COSTS_ORDER_TEMPLATE;
        final Map<String, Object> placeholderMap = new HashMap<>();
        final GeneratedDocumentInfo expected = new GeneratedDocumentInfo();
        final Instant instant = Instant.now();
        final String authToken = "someToken";

        expected.setCreatedOn("someCreatedDate");
        expected.setMimeType("someMimeType");
        expected.setUrl("someUrl");

        mockAndSetClock(instant);

        doReturn(data).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
            "generateDocument", String.class, Map.class)).withArguments(templateName, placeholderMap);
        doReturn(expected).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
            "storeDocument", byte[].class, String.class, String.class))
            .withArguments(data, authToken, COSTS_ORDER_NAME_FOR_PDF_FILE);

        GeneratedDocumentInfo actual = classUnderTest.generateAndStoreDocument(templateName, placeholderMap, authToken);

        assertEquals(expected, actual);

        verifyPrivate(classUnderTest, Mockito.times(1))
            .invoke("generateDocument", templateName, placeholderMap);
        verifyPrivate(classUnderTest, Mockito.times(1))
            .invoke("storeDocument", data, authToken, COSTS_ORDER_NAME_FOR_PDF_FILE);
    }

    @Test
    public void whenGenerateAndStoreDocument_givenTemplateNameIsDecreeNisi_thenProceedAsExpected() throws Exception {
        final DocumentManagementServiceImpl classUnderTest = spy(new DocumentManagementServiceImpl());

        final byte[] data = {1};
        final String templateName = DECREE_NISI_TEMPLATE;
        final Map<String, Object> placeholderMap = new HashMap<>();
        final GeneratedDocumentInfo expected = new GeneratedDocumentInfo();
        final Instant instant = Instant.now();
        final String authToken = "someToken";

        expected.setCreatedOn("someCreatedDate");
        expected.setMimeType("someMimeType");
        expected.setUrl("someUrl");

        mockAndSetClock(instant);

        doReturn(data).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
            "generateDocument", String.class, Map.class)).withArguments(templateName, placeholderMap);
        doReturn(expected).when(classUnderTest, MemberMatcher.method(DocumentManagementServiceImpl.class,
            "storeDocument", byte[].class, String.class, String.class))
            .withArguments(data, authToken, DECREE_NISI_NAME_FOR_PDF_FILE);

        GeneratedDocumentInfo actual = classUnderTest.generateAndStoreDocument(templateName, placeholderMap, authToken);

        assertEquals(expected, actual);

        verifyPrivate(classUnderTest, Mockito.times(1))
            .invoke("generateDocument", templateName, placeholderMap);
        verifyPrivate(classUnderTest, Mockito.times(1))
            .invoke("storeDocument", data, authToken, DECREE_NISI_NAME_FOR_PDF_FILE);
    }

    @Test
    public void whenStoreDocument_thenProceedAsExpected() {
        final byte[] data = {1};
        final String filename = "someFileName";
        final FileUploadResponse fileUploadResponse = new FileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo expected = new GeneratedDocumentInfo();

        when(evidenceManagementService.storeDocumentAndGetInfo(data, "test", filename))
                .thenReturn(fileUploadResponse);
        when(GeneratedDocumentInfoMapper.mapToGeneratedDocumentInfo(fileUploadResponse)).thenReturn(expected);

        GeneratedDocumentInfo actual = classUnderTest.storeDocument(data, "test", filename);

        assertEquals(expected, actual);

        Mockito.verify(evidenceManagementService, Mockito.times(1))
                .storeDocumentAndGetInfo(data, "test", filename);
        verifyStatic(GeneratedDocumentInfoMapper.class);
        GeneratedDocumentInfoMapper.mapToGeneratedDocumentInfo(fileUploadResponse);
    }

    @Test
    public void whenGenerateDocument_thenProceedAsExpected() {
        final byte[] expected = {1};
        final Map<String, Object> placeholderMap = emptyMap();
        final Map<String, Object> formattedPlaceholderMap = Collections.singletonMap("SomeThing", new Object());

        when(pdfGenerationFactory.getGeneratorType(A_TEMPLATE)).thenReturn(PDF_GENERATOR_TYPE);
        when(HtmlFieldFormatter.format(placeholderMap)).thenReturn(formattedPlaceholderMap);
        when(pdfGenerationFactory.getGeneratorService(A_TEMPLATE)).thenReturn(pdfGenerationService);
        when(pdfGenerationService.generate(A_TEMPLATE, formattedPlaceholderMap)).thenReturn(expected);

        byte[] actual = classUnderTest.generateDocument(A_TEMPLATE, placeholderMap);

        assertEquals(expected, actual);

        verifyStatic(HtmlFieldFormatter.class);
        HtmlFieldFormatter.format(placeholderMap);
        Mockito.verify(pdfGenerationFactory, Mockito.times(1))
            .getGeneratorService(A_TEMPLATE);
        Mockito.verify(pdfGenerationService, Mockito.times(1))
                .generate(A_TEMPLATE, formattedPlaceholderMap);
    }

    @Test
    public void whenGenerateCoEDocumentWithDocmosis_thenProceedAsExpected() {
        final byte[] expected = {1};
        final Map<String, Object> placeholderMap = emptyMap();

        when(pdfGenerationFactory.getGeneratorService(COE_TEMPLATE)).thenReturn(pdfGenerationService);
        when(pdfGenerationService.generate(COE_TEMPLATE, placeholderMap)).thenReturn(expected);

        byte[] actual = classUnderTest.generateDocument(COE_TEMPLATE, placeholderMap);

        assertEquals(expected, actual);

        Mockito.verify(pdfGenerationFactory, Mockito.times(1))
            .getGeneratorService(COE_TEMPLATE);
        Mockito.verify(pdfGenerationService, Mockito.times(1))
            .generate(COE_TEMPLATE, placeholderMap);
    }


    @Test
    public void whenGenerateDecreeNisiDocumentWithDocmosis_thenProceedAsExpected() {
        final byte[] expected = {1};
        final Map<String, Object> placeholderMap = emptyMap();

        when(pdfGenerationFactory.getGeneratorService(DECREE_NISI_TEMPLATE)).thenReturn(pdfGenerationService);
        when(pdfGenerationService.generate(DECREE_NISI_TEMPLATE, placeholderMap)).thenReturn(expected);

        byte[] actual = classUnderTest.generateDocument(DECREE_NISI_TEMPLATE, placeholderMap);

        assertEquals(expected, actual);

        Mockito.verify(pdfGenerationFactory, Mockito.times(1))
            .getGeneratorService(DECREE_NISI_TEMPLATE);
        Mockito.verify(pdfGenerationService, Mockito.times(1))
            .generate(DECREE_NISI_TEMPLATE, placeholderMap);
    }

    @Test
    public void whenGenerateCostsOrderDocumentWithDocmosis_thenProceedAsExpected() {
        final byte[] expected = {1};
        final Map<String, Object> placeholderMap = emptyMap();

        when(pdfGenerationFactory.getGeneratorService(COSTS_ORDER_TEMPLATE)).thenReturn(pdfGenerationService);
        when(pdfGenerationService.generate(COSTS_ORDER_TEMPLATE, placeholderMap)).thenReturn(expected);

        byte[] actual = classUnderTest.generateDocument(COSTS_ORDER_TEMPLATE, placeholderMap);

        assertEquals(expected, actual);

        HtmlFieldFormatter.format(placeholderMap);
        Mockito.verify(pdfGenerationFactory, Mockito.times(1))
            .getGeneratorService(COSTS_ORDER_TEMPLATE);
        Mockito.verify(pdfGenerationService, Mockito.times(1))
            .generate(COSTS_ORDER_TEMPLATE, placeholderMap);
    }

    private void mockAndSetClock(Instant instant) {
        final Clock clock = mock(Clock.class);
        when(clock.instant()).thenReturn(instant);

        Whitebox.setInternalState(classUnderTest, "clock", clock);
    }
}
