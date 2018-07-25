package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.GenerateDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.PDFGenerationException;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.NullOrEmptyValidator;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PDFGenerationServiceImpl.class, NullOrEmptyValidator.class, Objects.class})
public class PDFGenerationServiceImplUTest {

    private static final MediaType API_VERSION = MediaType
            .valueOf("application/vnd.uk.gov.hmcts.pdf-service.v2+json;charset=UTF-8");

    private static final String SERVICE_AUTHORIZATION_HEADER = "ServiceAuthorization";

    private static final String PDF_SERVICE_ENDPOINT = "pdf_service_endpoint";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AuthTokenGenerator serviceTokenGenerator;

    @InjectMocks
    @Spy
    private final PDFGenerationServiceImpl classUnderTest = new PDFGenerationServiceImpl();

    @Before
    public void before() {
        Whitebox.setInternalState(classUnderTest, "pdfServiceEndpoint", PDF_SERVICE_ENDPOINT);
        mockStatic(NullOrEmptyValidator.class, Objects.class);
    }

    @Test
    public void givenHttpClientErrorExceptionThrown_whenGenerateFromHtml_thenThrowPDFGenerationException() throws Exception {
        final byte[] template = {1};
        final Map<String, Object> placeholders = Collections.emptyMap();
        final String serviceToken = "serviceToken";

        final HttpEntity<String> httpEntity = mock(HttpEntity.class);

        final HttpClientErrorException httpClientErrorException = mock(HttpClientErrorException.class);

        doNothing().when(NullOrEmptyValidator.class);
        NullOrEmptyValidator.requireNonEmpty(template);
        doReturn(placeholders).when(Objects.class);
        Objects.requireNonNull(placeholders);

        doReturn(serviceToken).when(serviceTokenGenerator).generate();

        doReturn(httpEntity).when(classUnderTest,
                MemberMatcher.method(PDFGenerationServiceImpl.class, "buildRequest",
                        String.class, byte[].class, Map.class)).withArguments(serviceToken, template, placeholders);

        doThrow(httpClientErrorException).when(restTemplate).postForObject(PDF_SERVICE_ENDPOINT, httpEntity,
                byte[].class);

        try {
            classUnderTest.generateFromHtml(template, placeholders);
            fail();
        } catch (PDFGenerationException exception) {
            assertEquals(httpClientErrorException, exception.getCause());
        }

        verifyStatic(NullOrEmptyValidator.class);
        NullOrEmptyValidator.requireNonEmpty(template);
        verifyStatic(Objects.class);
        Objects.requireNonNull(placeholders);
        Mockito.verify(serviceTokenGenerator, Mockito.times(1)).generate();
        verifyPrivate(classUnderTest, Mockito.times(1)).invoke("buildRequest", serviceToken,
                template, placeholders);
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject(PDF_SERVICE_ENDPOINT,
                httpEntity, byte[].class);
    }

    @Test
    public void givenHttpRequestGoesThrough_whenGenerateFromHtml_thenReturnProperResponse() throws Exception {
        final byte[] template = {1};
        final byte[] pdfFile = {2};

        final Map<String, Object> placeholders = Collections.emptyMap();
        final String serviceToken = "serviceToken";

        final HttpEntity<String> httpEntity = mock(HttpEntity.class);

        doNothing().when(NullOrEmptyValidator.class);
        NullOrEmptyValidator.requireNonEmpty(template);
        doReturn(placeholders).when(Objects.class);
        Objects.requireNonNull(placeholders);

        doReturn(serviceToken).when(serviceTokenGenerator).generate();

        doReturn(httpEntity).when(classUnderTest,
                MemberMatcher.method(PDFGenerationServiceImpl.class, "buildRequest",
                        String.class, byte[].class, Map.class)).withArguments(serviceToken, template, placeholders);

        doReturn(pdfFile).when(restTemplate).postForObject(PDF_SERVICE_ENDPOINT, httpEntity, byte[].class);

        byte[] actual = classUnderTest.generateFromHtml(template, placeholders);

        assertEquals(pdfFile, actual);

        verifyStatic(NullOrEmptyValidator.class);
        NullOrEmptyValidator.requireNonEmpty(template);
        verifyStatic(Objects.class);
        Objects.requireNonNull(placeholders);
        Mockito.verify(serviceTokenGenerator, Mockito.times(1)).generate();
        verifyPrivate(classUnderTest, Mockito.times(1)).invoke("buildRequest", serviceToken,
                template, placeholders);
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject(PDF_SERVICE_ENDPOINT, httpEntity,
                byte[].class);
    }

    @Test
    public void givenObjectMapperThrowsException_whenBuildRequest_thenThrowPDFGenerationException() throws Exception {
        final String serviceToken = "serviceToken";
        final byte[] template = {1};
        Map<String, Object> placeholders = Collections.emptyMap();

        final JsonProcessingException jsonProcessingException = mock(JsonProcessingException.class);

        final GenerateDocumentRequest request = new GenerateDocumentRequest(new String(template), placeholders);

        when(objectMapper.writeValueAsString(request)).thenThrow(jsonProcessingException);

        try {
            buildRequest(serviceToken, template, placeholders);
            fail();
        } catch (PDFGenerationException exception) {
            assertEquals(jsonProcessingException, exception.getCause());
        }

        Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsString(request);
    }

    @Test
    public void givenNoErrors_whenBuildRequest_thenReturnRequest() throws Exception {
        final String serviceToken = "serviceToken";
        final byte[] template = {1};
        final Map<String, Object> placeholders = Collections.emptyMap();

        final GenerateDocumentRequest request = new GenerateDocumentRequest(new String(template), placeholders);

        final String requestString = "requestString";

        when(objectMapper.writeValueAsString(request)).thenReturn(requestString);

        HttpEntity<String> actual = buildRequest(serviceToken, template, placeholders);

        HttpHeaders httpHeaders = actual.getHeaders();

        assertEquals(requestString, actual.getBody());
        assertEquals(API_VERSION, httpHeaders.getContentType());
        assertEquals(MediaType.APPLICATION_PDF, httpHeaders.getAccept().get(0));
        assertEquals(serviceToken, httpHeaders.get(SERVICE_AUTHORIZATION_HEADER).get(0));

        Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsString(request);
    }

    private HttpEntity<String> buildRequest(String serviceAuthToken, byte[] template,
                                            Map<String, Object> placeholders) {
        try {
            return Whitebox.invokeMethod(classUnderTest, "buildRequest", serviceAuthToken, template,
                    placeholders);
        } catch (PDFGenerationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
