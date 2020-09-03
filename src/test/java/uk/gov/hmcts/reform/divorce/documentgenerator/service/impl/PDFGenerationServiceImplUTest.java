package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.GenerateDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.PDFGenerationException;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.TemplateManagementService;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_PDF;

@RunWith(MockitoJUnitRunner.class)
public class PDFGenerationServiceImplUTest {

    private static final MediaType API_VERSION = MediaType.valueOf("application/vnd.uk.gov.hmcts.pdf-service.v2+json;charset=UTF-8");
    private static final String SERVICE_AUTHORIZATION_HEADER = "ServiceAuthorization";
    private static final String PDF_SERVICE_ENDPOINT = "pdf_service_endpoint";

    private static final String TEST_SERVICE_TOKEN = "serviceToken";
    private static final String TEST_JSON_PAYLOAD = "{'transformedName':'transformedValue'}";
    private static final String TEST_TEMPLATE_NAME = "testTemplateName";
    private static final byte[] TEST_TEMPLATE_CONTENT = {1, 2, 3};
    private static final byte[] TEST_PDF_FILE_CONTENT = {4, 5, 6};

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TemplateManagementService templateManagementService;

    @Mock
    private AuthTokenGenerator serviceTokenGenerator;

    @Captor
    private ArgumentCaptor<HttpEntity> httpEntityArgumentCaptor;

    @Captor
    private ArgumentCaptor<GenerateDocumentRequest> generateDocumentRequestArgumentCaptor;

    @InjectMocks
    private PDFGenerationServiceImpl classUnderTest = new PDFGenerationServiceImpl(PDF_SERVICE_ENDPOINT);

    @Before
    public void setUp() throws JsonProcessingException {
        when(serviceTokenGenerator.generate()).thenReturn(TEST_SERVICE_TOKEN);
        when(templateManagementService.getTemplateByName(TEST_TEMPLATE_NAME)).thenReturn(TEST_TEMPLATE_CONTENT);
        when(objectMapper.writeValueAsString(any())).thenReturn(TEST_JSON_PAYLOAD);
    }

    @Test
    public void givenHttpRequestGoesThrough_whenGenerateFromHtml_thenReturnProperResponse() throws JsonProcessingException {
        when(restTemplate.postForObject(eq(PDF_SERVICE_ENDPOINT), any(), eq(byte[].class))).thenReturn(TEST_PDF_FILE_CONTENT);
        Map<String, Object> placeholders = new HashMap<>();
        placeholders.put("htmlValue", "<b>This should be escaped</b>");

        byte[] generatedDocument = classUnderTest.generate(TEST_TEMPLATE_NAME, placeholders);

        assertThat(generatedDocument, is(TEST_PDF_FILE_CONTENT));
        verify(serviceTokenGenerator).generate();
        verify(restTemplate).postForObject(eq(PDF_SERVICE_ENDPOINT), httpEntityArgumentCaptor.capture(), eq(byte[].class));
        assertCapturedHttpEntity(httpEntityArgumentCaptor.getValue());
        verify(objectMapper).writeValueAsString(generateDocumentRequestArgumentCaptor.capture());
        GenerateDocumentRequest generateDocumentRequest = generateDocumentRequestArgumentCaptor.getValue();
        assertThat(generateDocumentRequest.getValues(), hasEntry("htmlValue", "&lt;b&gt;This should be escaped&lt;/b&gt;"));
    }

    @Test
    public void givenHttpClientErrorExceptionThrown_whenGenerateFromHtml_thenThrowPDFGenerationException() {
        when(restTemplate.postForObject(eq(PDF_SERVICE_ENDPOINT), any(), eq(byte[].class))).thenThrow(HttpClientErrorException.class);

        try {
            classUnderTest.generate(TEST_TEMPLATE_NAME, emptyMap());
            fail("Should have thrown exception");
        } catch (PDFGenerationException exception) {
            assertThat(exception.getCause(), is(instanceOf(HttpClientErrorException.class)));
            assertThat(exception.getMessage(), containsString("Failed to request PDF from REST endpoint"));
        }

        verify(serviceTokenGenerator).generate();
        verify(restTemplate).postForObject(eq(PDF_SERVICE_ENDPOINT), httpEntityArgumentCaptor.capture(), eq(byte[].class));
        assertCapturedHttpEntity(httpEntityArgumentCaptor.getValue());
    }

    @Test
    public void givenObjectMapperThrowsException_whenBuildRequest_thenThrowPDFGenerationException() throws Exception {
        final GenerateDocumentRequest request = new GenerateDocumentRequest(new String(TEST_TEMPLATE_CONTENT), emptyMap());
        when(objectMapper.writeValueAsString(eq(request))).thenThrow(JsonProcessingException.class);

        try {
            classUnderTest.generate(TEST_TEMPLATE_NAME, emptyMap());
            fail("Should have thrown exception");
        } catch (PDFGenerationException exception) {
            assertThat(exception.getCause(), is(instanceOf(JsonProcessingException.class)));
            assertThat(exception.getMessage(), is("Failed to convert PDF request into JSON"));
        }

        verify(objectMapper).writeValueAsString(eq(request));
    }

    private void assertCapturedHttpEntity(HttpEntity capturedHttpEntity) {
        HttpHeaders httpHeaders = capturedHttpEntity.getHeaders();
        assertThat(httpHeaders.getContentType(), is(API_VERSION));
        assertThat(httpHeaders.getAccept(), is(asList(APPLICATION_PDF)));
        assertThat(httpHeaders.get(SERVICE_AUTHORIZATION_HEADER), is(asList(TEST_SERVICE_TOKEN)));
        assertThat(capturedHttpEntity.getBody(), is(TEST_JSON_PAYLOAD));
    }

}
