package uk.gov.hmcts.reform.divorce.documentgenerator.functionaltest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.divorce.documentgenerator.DocumentGeneratorApplication;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.CcdCollectionMember;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.GenerateDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.FileUploadResponse;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.response.GeneratedDocumentInfo;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.TemplateManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.impl.DocumentManagementServiceImpl;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.impl.PDFGenerationServiceImpl;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DECREE_NISI_GRANTED_DATE_KEY;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID;
import static uk.gov.hmcts.reform.divorce.documentgenerator.domain.TemplateConstants.DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DocumentGeneratorApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource(value = "classpath:application.yml")
@TestPropertySource(properties = {"endpoints.health.time-to-live=0",
    "service-auth-provider.service.stub.enabled=false",
    "evidence-management-api.service.stub.enabled=false"})
@AutoConfigureMockMvc
public class DocumentGenerateAndStoreE2ETest {
    private static final String API_URL = "/version/1/generatePDF";
    private static final String CURRENT_DATE_KEY = "current_date";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSS";
    private static final String A_TEMPLATE = "divorceminipetition";
    private static final String COE_TEMPLATE = "FL-DIV-GNO-ENG-00020.docx";
    private static final String DECREE_NISI_TEMPLATE = "FL-DIV-GNO-ENG-00021.docx";
    private static final String COSTS_ORDER_TEMPLATE = "FL-DIV-DEC-ENG-00060.docx";
    private static final String DECREE_ABSOLUTE_TEMPLATE = "FL-DIV-GOR-ENG-00062.docx";
    private static final String CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID = "FL-DIV-GNO-ENG-00059.docx";
    private static final String AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_TEMPLATE_ID = "FL-DIV-LET-ENG-00075.doc";
    private static final String AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_TEMPLATE_ID = "FL-DIV-LET-ENG-00076.doc";
    private static final String AOS_OFFLINE_2_YEAR_SEPARATION_FORM_TEMPLATE_ID = "FL-DIV-APP-ENG-00080.docx";
    private static final String AOS_OFFLINE_5_YEAR_SEPARATION_FORM_TEMPLATE_ID = "FL-DIV-APP-ENG-00081.docx";
    private static final String AOS_OFFLINE_BEHAVIOUR_DESERTION_TEMPLATE_ID = "FL-DIV-APP-ENG-00082.docx";
    private static final String AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_TEMPLATE_ID = "FL-DIV-APP-ENG-00083.docx";
    private static final String AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_TEMPLATE_ID = "FL-DIV-APP-ENG-00084.docx";

    private static final String CASE_DETAILS = "caseDetails";
    private static final String CASE_DATA = "case_data";
    private static final String CLAIM_COSTS_JSON_KEY = "D8DivorceCostsClaim";
    private static final String CLAIM_COSTS_FROM_JSON_KEY = "D8DivorceClaimFrom";
    private static final String COURT_HEARING_JSON_KEY = "DateAndTimeOfHearing";
    private static final String DN_APPROVAL_DATE_KEY = "DNApprovalDate";

    private static final String FILE_URL = "fileURL";
    private static final String MIME_TYPE = "mimeType";
    private static final String CREATED_ON = "createdOn";
    private static final String CREATED_BY = "createdBy";


    private static final String FEATURE_TOGGLE_RESP_SOLCIITOR = "featureToggleRespSolicitor";

    @Autowired
    private MockMvc webClient;

    @Value("${service.pdf-service.uri}")
    private String pdfServiceUri;

    @Value("${docmosis.service.pdf-service.uri}")
    private String docmosisPdfServiceUri;

    @Value("${service.evidence-management-client-api.uri}")
    private String emClientAPIUri;

    @MockBean
    private AuthTokenGenerator serviceTokenGenerator;

    @Autowired
    private PDFGenerationServiceImpl pdfGenerationService;

    @Autowired
    private TemplateManagementService templateManagementService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DocumentManagementServiceImpl documentManagementService;

    private MockRestServiceServer mockRestServiceServer;

    @Before
    public void before() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void givenTemplateNameIsNull_whenGenerateAndStoreDocument_thenReturnHttp400() throws Exception {
        final String template = null;
        final Map<String, Object> values = Collections.emptyMap();

        final GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(template, values);

        webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void givenTemplateNameIsBlank_whenGenerateAndStoreDocument_thenReturnHttp400() throws Exception {
        final String template = "  ";
        final Map<String, Object> values = Collections.emptyMap();

        final GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(template, values);

        webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void givenTemplateNotFound_whenGenerateAndStoreDocument_thenReturnHttp400() throws Exception {
        final String template = "nonExistingTemplate";
        final Map<String, Object> values = Collections.emptyMap();

        final GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(template, values);

        webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void givenCouldNotConnectToAuthService_whenGenerateAndStoreDocument_thenReturnHttp503() throws Exception {
        final Map<String, Object> values = Collections.emptyMap();

        final GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(A_TEMPLATE, values);

        when(serviceTokenGenerator.generate()).thenThrow(new HttpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isServiceUnavailable());
    }

    @Test
    public void givenAuthServiceReturnAuthenticationError_whenGenerateAndStoreDocument_thenReturnHttp401() throws Exception {
        final Map<String, Object> values = Collections.emptyMap();

        final GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(A_TEMPLATE, values);

        when(serviceTokenGenerator.generate()).thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenObjectMapperThrowsException_whenGenerateAndStoreDocument_thenReturnHttp500() throws Exception {
        final ObjectMapper objectMapper = mock(ObjectMapper.class);

        ReflectionTestUtils.setField(pdfGenerationService, "objectMapper", objectMapper);

        ReflectionTestUtils.setField(documentManagementService, "featureToggleRespSolicitor", "true");

        final Map<String, Object> values = new HashMap<>();
        values.put("someKey", "someValue");
        final String securityToken = "securityToken";
        final Instant instant = Instant.now();
        mockAndSetClock(instant);

        final Map<String, Object> valuesWithDate = new HashMap<>(values);
        valuesWithDate.put(CURRENT_DATE_KEY, new SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            .format(Date.from(instant)));

        valuesWithDate.put(FEATURE_TOGGLE_RESP_SOLCIITOR, true);

        final GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(A_TEMPLATE, values);

        final GenerateDocumentRequest requestToPDFService =
            new GenerateDocumentRequest(new String(templateManagementService.getTemplateByName(A_TEMPLATE)),
                valuesWithDate);

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);
        when(objectMapper.writeValueAsString(requestToPDFService)).thenThrow(mock(JsonProcessingException.class));

        webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenCouldNotConnectToPDFService_whenGenerateAndStoreDocument_thenReturnHttp503() throws Exception {
        final Map<String, Object> values = Collections.emptyMap();
        final String securityToken = "securityToken";

        final GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(A_TEMPLATE, values);

        mockPDFService(HttpStatus.BAD_REQUEST, new byte[] {1});

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isServiceUnavailable());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenPDFServiceReturnNon2xxStatus_whenGenerateAndStoreDocument_thenReturnSameStatus() throws Exception {
        final Map<String, Object> values = Collections.emptyMap();
        final String securityToken = "securityToken";

        final GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(A_TEMPLATE, values);

        mockPDFService(HttpStatus.INTERNAL_SERVER_ERROR, new byte[] {1});

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenPDFServiceReturnNull_whenGenerateAndStoreDocument_thenReturn400() throws Exception {
        final Map<String, Object> values = Collections.emptyMap();
        final String securityToken = "securityToken";

        final GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(A_TEMPLATE, values);

        mockPDFService(HttpStatus.OK, null);

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenCouldNotConnectToEMClientAPI_whenGenerateAndStoreDocument_thenReturn503() throws Exception {
        final Map<String, Object> values = Collections.emptyMap();
        final String securityToken = "securityToken";

        final GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(A_TEMPLATE, values);

        mockPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.BAD_REQUEST, null);

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isServiceUnavailable());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenEMClientAPIReturnNon2xxStatus_whenGenerateAndStoreDocument_thenReturnSameStatus() throws Exception {
        final Map<String, Object> values = Collections.emptyMap();
        final String securityToken = "securityToken";

        final GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(A_TEMPLATE, values);

        mockPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.UNAUTHORIZED, null);

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenEMClientAPIReturnDataContainsNon200Status_whenGenerateAndStoreDocument_thenReturn500() throws Exception {
        final Map<String, Object> values = Collections.emptyMap();
        final String securityToken = "securityToken";

        final FileUploadResponse fileUploadResponse = new FileUploadResponse(HttpStatus.INTERNAL_SERVER_ERROR);

        final GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(A_TEMPLATE, values);

        mockPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWell_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final Map<String, Object> values = Collections.emptyMap();
        final String securityToken = "securityToken";

        final GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(A_TEMPLATE, values);


        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForAosInvitation_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final String template = "aosinvitation";
        final Map<String, Object> values = Collections.emptyMap();
        final String securityToken = "securityToken";

        final GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(template, values);


        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForDivorceMiniPetition_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final String template = "divorceminipetition";
        final Map<String, Object> values = Collections.emptyMap();
        final String securityToken = "securityToken";

        final GenerateDocumentRequest generateDocumentRequest = new GenerateDocumentRequest(template, values);

        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForDivorceCoRespondentAos_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final String coRespondentTemplate = "co-respondentinvitation";
        final Map<String, Object> values = Collections.emptyMap();
        final String securityToken = "securityToken";

        final GenerateDocumentRequest generateDocumentRequest =
            new GenerateDocumentRequest(coRespondentTemplate, values);

        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForDivorceRespondentAnswers_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final String respondentAnsTemplate = "respondentAnswers";
        final Map<String, Object> values = Collections.emptyMap();
        final String securityToken = "securityToken";

        final GenerateDocumentRequest generateDocumentRequest =
            new GenerateDocumentRequest(respondentAnsTemplate, values);

        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForDivorceCoRespondentAnswers_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final String coRespondentTemplate = "co-respondent-answers";
        final Map<String, Object> values = Collections.emptyMap();
        final String securityToken = "securityToken";

        final GenerateDocumentRequest generateDocumentRequest =
            new GenerateDocumentRequest(coRespondentTemplate, values);

        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForDivorceCertificateOfEntitlement_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final Map<String, Object> values = new HashMap<>();
        final String securityToken = "securityToken";

        values.put(CASE_DETAILS, Collections.singletonMap(CASE_DATA, Collections.EMPTY_MAP));

        final GenerateDocumentRequest generateDocumentRequest =
            new GenerateDocumentRequest(COE_TEMPLATE, values);

        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockDocmosisPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForDivorceCertificateOfEntitlementWithDnApprovalDate_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final Map<String, Object> values = new HashMap<>();
        final String securityToken = "securityToken";

        final Map<String, Object> caseData = Collections.singletonMap(
            DN_APPROVAL_DATE_KEY, "2019-10-10");

        values.put(CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData));

        final GenerateDocumentRequest generateDocumentRequest =
            new GenerateDocumentRequest(COE_TEMPLATE, values);

        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockDocmosisPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForDivorceCertificateOfEntitlementWithCourtHearing_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final Map<String, Object> values = new HashMap<>();
        final String securityToken = "securityToken";

        final CcdCollectionMember<Map<String, Object>> courtHearingDate = new CcdCollectionMember<>();
        courtHearingDate.setValue(Collections.emptyMap());
        final Map<String, Object> caseData = Collections.singletonMap(
            COURT_HEARING_JSON_KEY, Collections.singletonList(courtHearingDate));

        values.put(CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData));

        final GenerateDocumentRequest generateDocumentRequest =
            new GenerateDocumentRequest(COE_TEMPLATE, values);

        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockDocmosisPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForDivorceCertificateOfEntitlementWithClaimFromBoth_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final Map<String, Object> values = new HashMap<>();
        final String securityToken = "securityToken";

        final String[] claimFrom = new String[] {"respondent", "correspondent"};
        final Map<String, Object> caseData = new HashMap<>();
        caseData.put(CLAIM_COSTS_JSON_KEY, "YES");
        caseData.put(CLAIM_COSTS_FROM_JSON_KEY, claimFrom);

        values.put(CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData));

        final GenerateDocumentRequest generateDocumentRequest =
            new GenerateDocumentRequest(COE_TEMPLATE, values);

        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockDocmosisPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForDivorceCertificateOfEntitlementWithClaimFromRespondent_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final Map<String, Object> values = new HashMap<>();
        final String securityToken = "securityToken";

        final String[] claimFrom = new String[] {"respondent"};
        final Map<String, Object> caseData = new HashMap<>();
        caseData.put(CLAIM_COSTS_JSON_KEY, "YES");
        caseData.put(CLAIM_COSTS_FROM_JSON_KEY, claimFrom);

        values.put(CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData));

        final GenerateDocumentRequest generateDocumentRequest =
            new GenerateDocumentRequest(COE_TEMPLATE, values);

        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockDocmosisPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForDivorceCertificateOfEntitlementWithClaimFromCoRespondent_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final Map<String, Object> values = new HashMap<>();
        final String securityToken = "securityToken";

        final String[] claimFrom = new String[] {"correspondent"};
        final Map<String, Object> caseData = new HashMap<>();
        caseData.put(CLAIM_COSTS_JSON_KEY, "YES");
        caseData.put(CLAIM_COSTS_FROM_JSON_KEY, claimFrom);

        values.put(CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData));

        final GenerateDocumentRequest generateDocumentRequest =
            new GenerateDocumentRequest(COE_TEMPLATE, values);

        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockDocmosisPDFService(HttpStatus.OK, new byte[]{1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForDivorceCostsOrder_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final Map<String, Object> values = new HashMap<>();
        final String securityToken = "securityToken";

        final String[] claimFrom = new String[] { "correspondent" };
        final Map<String, Object> caseData = new HashMap<>();
        caseData.put(CLAIM_COSTS_JSON_KEY, "YES");
        caseData.put(CLAIM_COSTS_FROM_JSON_KEY, claimFrom);

        values.put(CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData));

        final GenerateDocumentRequest generateDocumentRequest =
            new GenerateDocumentRequest(COSTS_ORDER_TEMPLATE, values);

        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockDocmosisPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForDecreeNisiPronouncement_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final Map<String, Object> values = new HashMap<>();
        final String securityToken = "securityToken";

        final Map<String, Object> caseData = ImmutableMap.of(
            DECREE_NISI_GRANTED_DATE_KEY, "2019-10-10",
            DECREE_ABSOLUTE_ELIGIBLE_FROM_DATE_KEY, "2019-11-23"
        );

        values.put(CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData));

        final GenerateDocumentRequest generateDocumentRequest =
            new GenerateDocumentRequest(DECREE_NISI_TEMPLATE, values);

        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockDocmosisPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForDecreeAbsolutePronouncement_whenGenerateAndStoreDocument_thenReturn()
        throws Exception {
        assertReturnWhenAllGoesWellForGeneratingAndStoringDocuments(DECREE_ABSOLUTE_TEMPLATE);
    }

    @Test
    public void givenAllGoesWellForPrintForPronouncement_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        assertReturnWhenAllGoesWellForGeneratingAndStoringDocuments(CASE_LIST_FOR_PRONOUNCEMENT_TEMPLATE_ID);
    }

    @Test
    public void givenInvalidDnApprovalDateForGenerateCoE_whenGenerateAndStoreDocument_thenThrowException() throws Exception {
        final Map<String, Object> values = new HashMap<>();

        final Map<String, Object> caseData = Collections.singletonMap(
            DN_APPROVAL_DATE_KEY, "invalidDateFormat");

        values.put(CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData));

        final GenerateDocumentRequest generateDocumentRequest =
            new GenerateDocumentRequest(COE_TEMPLATE, values);

        webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is5xxServerError())
            .andReturn();
    }

    @Test
    public void givenAllGoesWellForAosOfflineInvitationLetterRespondent_whenGenerateAndStoreDocument_thenReturn()
        throws Exception {
        assertReturnWhenAllGoesWellForGeneratingAndStoringDocuments(
            AOS_OFFLINE_INVITATION_LETTER_RESPONDENT_TEMPLATE_ID
        );
    }

    @Test
    public void givenAllGoesWellForAosOfflineInvitationLetterCoRespondent_whenGenerateAndStoreDocument_thenReturn()
        throws Exception {
        assertReturnWhenAllGoesWellForGeneratingAndStoringDocuments(
            AOS_OFFLINE_INVITATION_LETTER_CO_RESPONDENT_TEMPLATE_ID
        );
    }

    @Test
    public void givenAllGoesWellForAosOffline2YearSeparationForm_whenGenerateAndStoreDocument_thenReturn()
        throws Exception {
        assertReturnWhenAllGoesWellForGeneratingAndStoringDocuments(AOS_OFFLINE_2_YEAR_SEPARATION_FORM_TEMPLATE_ID);
    }

    @Test
    public void givenAllGoesWellForAosOffline5YearSeparationForm_whenGenerateAndStoreDocument_thenReturn()
        throws Exception {
        assertReturnWhenAllGoesWellForGeneratingAndStoringDocuments(AOS_OFFLINE_5_YEAR_SEPARATION_FORM_TEMPLATE_ID);
    }

    @Test
    public void givenAllGoesWellForAosOfflineBehaviourDesertionForm_whenGenerateAndStoreDocument_thenReturn()
        throws Exception {
        assertReturnWhenAllGoesWellForGeneratingAndStoringDocuments(AOS_OFFLINE_BEHAVIOUR_DESERTION_TEMPLATE_ID);
    }

    @Test
    public void givenAllGoesWellForAosOfflineAdulteryFormRespondent_whenGenerateAndStoreDocument_thenReturn()
        throws Exception {
        assertReturnWhenAllGoesWellForGeneratingAndStoringDocuments(AOS_OFFLINE_ADULTERY_FORM_RESPONDENT_TEMPLATE_ID);
    }

    @Test
    public void givenAllGoesWellForAosOfflineAdulteryFormCoRespondent_whenGenerateAndStoreDocument_thenReturn()
        throws Exception {
        assertReturnWhenAllGoesWellForGeneratingAndStoringDocuments(
            AOS_OFFLINE_ADULTERY_FORM_CO_RESPONDENT_TEMPLATE_ID);
    }

    private void assertReturnWhenAllGoesWellForGeneratingAndStoringDocuments(String templateId) throws Exception {
        final Map<String, Object> values = new HashMap<>();
        final String securityToken = "securityToken";

        final Map<String, Object> caseData = Collections.emptyMap();

        values.put(CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData));

        final GenerateDocumentRequest generateDocumentRequest =
            new GenerateDocumentRequest(templateId, values);

        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockDocmosisPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForDecreeNisiClarificationOrder_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final Map<String, Object> values = new HashMap<>();
        final String securityToken = "securityToken";

        final Map<String, Object> caseData = Collections.emptyMap();

        values.put(CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData));

        final GenerateDocumentRequest generateDocumentRequest =
            new GenerateDocumentRequest(DN_REFUSAL_ORDER_CLARIFICATION_TEMPLATE_ID, values);

        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockDocmosisPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    @Test
    public void givenAllGoesWellForDecreeNisiRefusalOrder_whenGenerateAndStoreDocument_thenReturn() throws Exception {
        final Map<String, Object> values = new HashMap<>();
        final String securityToken = "securityToken";

        final Map<String, Object> caseData = Collections.emptyMap();

        values.put(CASE_DETAILS, Collections.singletonMap(CASE_DATA, caseData));

        final GenerateDocumentRequest generateDocumentRequest =
            new GenerateDocumentRequest(DN_REFUSAL_ORDER_REJECTION_TEMPLATE_ID, values);

        final FileUploadResponse fileUploadResponse = getFileUploadResponse(HttpStatus.OK);

        final GeneratedDocumentInfo generatedDocumentInfo = getGeneratedDocumentInfo();

        mockDocmosisPDFService(HttpStatus.OK, new byte[] {1});
        mockEMClientAPI(HttpStatus.OK, Collections.singletonList(fileUploadResponse));

        when(serviceTokenGenerator.generate()).thenReturn(securityToken);

        MvcResult result = webClient.perform(post(API_URL)
            .content(ObjectMapperTestUtil.convertObjectToJsonString(generateDocumentRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertEquals(ObjectMapperTestUtil.convertObjectToJsonString(generatedDocumentInfo),
            result.getResponse().getContentAsString());

        mockRestServiceServer.verify();
    }

    private FileUploadResponse getFileUploadResponse(HttpStatus httpStatus) {
        final FileUploadResponse fileUploadResponse = new FileUploadResponse(httpStatus);
        fileUploadResponse.setFileUrl(FILE_URL);
        fileUploadResponse.setMimeType(MIME_TYPE);
        fileUploadResponse.setCreatedOn(CREATED_ON);
        fileUploadResponse.setCreatedBy(CREATED_BY);
        return fileUploadResponse;
    }

    private GeneratedDocumentInfo getGeneratedDocumentInfo() {
        GeneratedDocumentInfo generatedDocumentInfo = new GeneratedDocumentInfo();
        generatedDocumentInfo.setUrl(FILE_URL);
        generatedDocumentInfo.setMimeType(MIME_TYPE);
        generatedDocumentInfo.setCreatedOn(CREATED_ON);
        return generatedDocumentInfo;
    }

    private void mockAndSetClock(Instant instant) {
        final Clock clock = mock(Clock.class);
        when(clock.instant()).thenReturn(instant);

        ReflectionTestUtils.setField(documentManagementService, "clock", clock);
    }

    private void mockPDFService(HttpStatus expectedResponse, byte[] body) {
        mockRestServiceServer.expect(once(), requestTo(pdfServiceUri)).andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(expectedResponse)
                .body(ObjectMapperTestUtil.convertObjectToJsonBytes(body))
                .contentType(MediaType.APPLICATION_JSON));
    }

    private void mockDocmosisPDFService(HttpStatus expectedResponse, byte[] body) {
        mockRestServiceServer.expect(once(), requestTo(docmosisPdfServiceUri)).andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(expectedResponse)
                .body(ObjectMapperTestUtil.convertObjectToJsonBytes(body))
                .contentType(MediaType.APPLICATION_JSON));
    }

    private void mockEMClientAPI(HttpStatus expectedResponse, List<FileUploadResponse> fileUploadResponse) {
        mockRestServiceServer.expect(once(), requestTo(emClientAPIUri)).andExpect(method(HttpMethod.POST))
            .andRespond(withStatus(expectedResponse)
                .body(ObjectMapperTestUtil.convertObjectToJsonString(fileUploadResponse))
                .contentType(MediaType.APPLICATION_JSON));
    }
}
