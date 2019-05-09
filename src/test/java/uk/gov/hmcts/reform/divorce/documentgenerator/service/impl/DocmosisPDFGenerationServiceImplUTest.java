package uk.gov.hmcts.reform.divorce.documentgenerator.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.divorce.documentgenerator.config.DocmosisBasePdfConfig;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.PdfDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.exception.PDFGenerationException;
import uk.gov.hmcts.reform.divorce.documentgenerator.util.NullOrEmptyValidator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;


@PowerMockIgnore("com.microsoft.applicationinsights.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest({DocmosisPDFGenerationServiceImpl.class, NullOrEmptyValidator.class, Objects.class})
public class DocmosisPDFGenerationServiceImplUTest {

    private static final String CASE_DETAILS = "caseDetails";
    private static final String CASE_DATA = "case_data";

    private static final String PDF_SERVICE_ENDPOINT = "pdf_service_endpoint";
    private static final String PDF_SERVICE_KEY = "pdf_service_key" ;

    @Mock
    private DocmosisBasePdfConfig docmosisBasePdfConfig;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private final DocmosisPDFGenerationServiceImpl classUnderTest = new DocmosisPDFGenerationServiceImpl();

    @Before
    public void before() {
        Whitebox.setInternalState(classUnderTest, "pdfServiceEndpoint", PDF_SERVICE_ENDPOINT);
        Whitebox.setInternalState(classUnderTest, "pdfServiceAccessKey", PDF_SERVICE_KEY);
        mockStatic(NullOrEmptyValidator.class, Objects.class);
    }

    @Test
    public void givenHttpClientErrorExceptionThrown_whenGenerateCalled_thenThrowPDFGenerationException() {
        final String template = "1";
        final Map<String, Object> dataMap = new HashMap<>();
        final Map<String, Object> caseData = Collections.singletonMap(CASE_DATA, dataMap);
        final Map<String, Object> placeholders = Collections.singletonMap(CASE_DETAILS, caseData);
        final HttpClientErrorException httpClientErrorException = mock(HttpClientErrorException.class);

        Map<String, Object> expectedPlaceholders = new HashMap<>();
        expectedPlaceholders.put(docmosisBasePdfConfig.getDisplayTemplateKey(),
            docmosisBasePdfConfig.getDisplayTemplateVal());
        expectedPlaceholders.put(docmosisBasePdfConfig.getFamilyCourtImgKey(),
            docmosisBasePdfConfig.getFamilyCourtImgVal());
        expectedPlaceholders.put(docmosisBasePdfConfig.getHmctsImgKey(), docmosisBasePdfConfig.getHmctsImgVal());

        PdfDocumentRequest pdfDocumentRequest = PdfDocumentRequest.builder()
            .accessKey(PDF_SERVICE_KEY)
            .data(expectedPlaceholders)
            .templateName(template)
            .outputName("result.pdf")
            .build();

        doThrow(httpClientErrorException).when(restTemplate)
            .postForEntity(PDF_SERVICE_ENDPOINT, pdfDocumentRequest, byte[].class);

        try {
            classUnderTest.generate(template, placeholders);
            fail();
        } catch (PDFGenerationException exception) {
            assertEquals(httpClientErrorException, exception.getCause());
        }
        NullOrEmptyValidator.requireNonBlank(template);
    }

    @Test
    public void givenHttpRequestGoesThrough_whenGenerateFromHtml_thenReturnProperResponse() {
        final String template = "1";
        final Map<String, Object> dataMap = new HashMap<>();
        final Map<String, Object> caseData = Collections.singletonMap(CASE_DATA, dataMap);
        final Map<String, Object> placeholders = Collections.singletonMap(CASE_DETAILS, caseData);

        Map<String, Object> expectedPlaceholders = new HashMap<>();
        expectedPlaceholders.put(docmosisBasePdfConfig.getDisplayTemplateKey(),
            docmosisBasePdfConfig.getDisplayTemplateVal());
        expectedPlaceholders.put(docmosisBasePdfConfig.getFamilyCourtImgKey(),
            docmosisBasePdfConfig.getFamilyCourtImgVal());
        expectedPlaceholders.put(docmosisBasePdfConfig.getHmctsImgKey(), docmosisBasePdfConfig.getHmctsImgVal());

        PdfDocumentRequest pdfDocumentRequest = PdfDocumentRequest.builder()
            .accessKey(PDF_SERVICE_KEY)
            .data(expectedPlaceholders)
            .templateName(template)
            .outputName("result.pdf")
            .build();

        byte[] pdfBytes = "output".getBytes();
        ResponseEntity responseEntity = Mockito.mock(ResponseEntity.class);
        doReturn(pdfBytes).when(responseEntity).getBody();
        doReturn(responseEntity).when(restTemplate).postForEntity(PDF_SERVICE_ENDPOINT,
            pdfDocumentRequest, byte[].class);

        byte[] bytes = classUnderTest.generate(template, placeholders);
        assertEquals(pdfBytes, bytes);

        NullOrEmptyValidator.requireNonBlank(template);
        verifyStatic(Objects.class);
    }

}
