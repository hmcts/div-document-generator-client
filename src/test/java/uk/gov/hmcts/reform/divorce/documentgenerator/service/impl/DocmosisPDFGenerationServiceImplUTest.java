package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.PdfDocumentRequest;
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

@PowerMockIgnore("com.microsoft.applicationinsights.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({DocmosisPDFGenerationServiceImpl.class, NullOrEmptyValidator.class, Objects.class})
public class DocmosisPDFGenerationServiceImplUTest {

    private static final String PDF_SERVICE_ENDPOINT = "pdf_service_endpoint";
    private static final String PDF_SERVICE_KEY = "pdf_service_key" ;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    @Spy
    private final DocmosisPDFGenerationServiceImpl classUnderTest = new DocmosisPDFGenerationServiceImpl();

    @Before
    public void before() {
        Whitebox.setInternalState(classUnderTest, "pdfServiceEndpoint", PDF_SERVICE_ENDPOINT);
        Whitebox.setInternalState(classUnderTest, "pdfServiceAccessKey", PDF_SERVICE_KEY);
        mockStatic(NullOrEmptyValidator.class, Objects.class);
    }

    @Test
    public void givenHttpClientErrorExceptionThrown_whenGenerateCalled_thenThrowPDFGenerationException()
        throws Exception {
        final String template = "1";
        final Map<String, Object> placeholders = Collections.emptyMap();
        final HttpClientErrorException httpClientErrorException = mock(HttpClientErrorException.class);

        doNothing().when(NullOrEmptyValidator.class);
        NullOrEmptyValidator.requireNonBlank(template);
        doReturn(placeholders).when(Objects.class);
        Objects.requireNonNull(placeholders);
        PdfDocumentRequest pdfDocumentRequest = PdfDocumentRequest.builder()
            .accessKey(PDF_SERVICE_KEY)
            .data(placeholders)
            .templateName(template)
            .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<PdfDocumentRequest> expectedEntity = new HttpEntity<>(pdfDocumentRequest, headers);

        doReturn(pdfDocumentRequest).when(classUnderTest,
            MemberMatcher.method(DocmosisPDFGenerationServiceImpl.class, "request",
                String.class, Map.class))
            .withArguments(Mockito.anyString(), Mockito.any(Map.class));

        doThrow(httpClientErrorException).when(restTemplate).exchange(PDF_SERVICE_ENDPOINT, HttpMethod.POST,
            expectedEntity, byte[].class);

        try {
            classUnderTest.generate(template, placeholders);
            fail();
        } catch (PDFGenerationException exception) {
            assertEquals(httpClientErrorException, exception.getCause());
        }
        NullOrEmptyValidator.requireNonBlank(template);
    }

    @Test
    public void givenHttpRequestGoesThrough_whenGenerateFromHtml_thenReturnProperResponse() throws Exception {
        final String template = "1";
        final Map<String, Object> placeholders = Collections.emptyMap();
        final HttpClientErrorException httpClientErrorException = mock(HttpClientErrorException.class);

        doNothing().when(NullOrEmptyValidator.class);
        NullOrEmptyValidator.requireNonBlank(template);
        doReturn(placeholders).when(Objects.class);
        Objects.requireNonNull(placeholders);
        PdfDocumentRequest pdfDocumentRequest = PdfDocumentRequest.builder()
            .accessKey(PDF_SERVICE_KEY)
            .data(placeholders)
            .templateName(template)
            .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<PdfDocumentRequest> expectedEntity = new HttpEntity<>(pdfDocumentRequest, headers);

        doReturn(pdfDocumentRequest).when(classUnderTest,
            MemberMatcher.method(DocmosisPDFGenerationServiceImpl.class, "request",
                String.class, Map.class))
            .withArguments(Mockito.anyString(), Mockito.any(Map.class));
        byte[] pdfBytes = "output".getBytes();
        ResponseEntity responseEntity = Mockito.mock(ResponseEntity.class);
        doReturn(pdfBytes).when(responseEntity).getBody();
        doReturn(responseEntity).when(restTemplate).exchange(PDF_SERVICE_ENDPOINT, HttpMethod.POST,
            expectedEntity, byte[].class);

        try {
            byte[] bytes = classUnderTest.generate(template, placeholders);
            assertEquals(pdfBytes, bytes);
        } catch (PDFGenerationException exception) {
            assertEquals(httpClientErrorException, exception.getCause());
        }
        NullOrEmptyValidator.requireNonBlank(template);
    }

}
