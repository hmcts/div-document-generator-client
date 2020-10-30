package uk.gov.hmcts.reform.divorce.documentgenerator;


import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactFolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Executor;
import org.json.JSONException;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.divorce.documentgenerator.domain.request.GenerateDocumentRequest;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.TemplateManagementService;
import uk.gov.hmcts.reform.divorce.documentgenerator.service.impl.PDFGenerationServiceImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@PactTestFor(providerName = "rpePdfService_PDFGenerationEndpointV2", port = "8891")
@PactFolder("pacts")
@SpringBootTest({
    "service.pdf-service.uri : http://localhost:8891/pdfs"
})
public class PdfGGenerationServiceConsumerTest {

    private static final String SERVICE_AUTHORIZATION_HEADER = "ServiceAuthorization";

    @Autowired
    PDFGenerationServiceImpl pdfGenerationService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private AuthTokenGenerator serviceTokenGenerator;
    @MockBean
    private TemplateManagementService templateManagementService;
    private final String someServiceAuthToken = "someServiceAuthToken";
    private final String template = "<html><body><div>Case number: {{ caseNo }}</div></body></html>";
    private Map placeholders = Map.of("caseNo", "12345");

    @BeforeEach
    public void setUpEachTest() throws InterruptedException, IOException {
        Thread.sleep(2000);
    }

    @After
    void teardown() {
        Executor.closeIdleConnections();
    }

    @Pact(provider = "rpePdfService_PDFGenerationEndpointV2", consumer = "divorce_documentGeneratorClient")
    RequestResponsePact generatePdfFromTemplate(PactDslWithProvider builder) throws JSONException, IOException {
        // @formatter:off

        return builder
            .given("A request to generate a divorce pdf document")
            .uponReceiving("a request to generate a divorce pdf document with a template")
            .method("POST")
            .headers(SERVICE_AUTHORIZATION_HEADER, someServiceAuthToken)
            .body(createJsonObject(new GenerateDocumentRequest(template, placeholders)),
                "application/vnd.uk.gov.hmcts.pdf-service.v2+json;charset=UTF-8")
            .path("/pdfs")
            .willRespondWith()
            .withBinaryData("".getBytes(), "application/octet-stream")
            .matchHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE,
                "application/pdf")
            .status(HttpStatus.SC_OK)
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "generatePdfFromTemplate")
    public void verifyGeneratePdfFromTemplatePact() throws IOException, JSONException {
        Map<String, Object> placeholders = new HashMap<>();
        placeholders.put("caseNo", "12345");

        when(templateManagementService.getTemplateByName("someTemplateName")).thenReturn(template.getBytes());
        when(serviceTokenGenerator.generate()).thenReturn(someServiceAuthToken);

        byte[] response = pdfGenerationService.generate("someTemplateName", placeholders);

    }

    private File getFile(String fileName) throws FileNotFoundException {
        return ResourceUtils.getFile(this.getClass().getResource("/json/" + fileName));
    }

    protected String createJsonObject(Object obj) throws JSONException, IOException {
        return objectMapper.writeValueAsString(obj);
    }

    private GenerateDocumentRequest buildGenerateDocumentRequest() {
        Map<String, Object> placeholders = new HashMap<>();
        placeholders.put("caseNo", "12345");

        return new GenerateDocumentRequest(template, placeholders);

    }

}
