package uk.gov.hmcts.reform.divorce;

import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

import static net.serenitybdd.rest.SerenityRest.given;


@RunWith(SerenityRunner.class)
public class GeneratePDFIntegrationTest extends IntegrationTest {

    private static final String INVALID_TEMPLATE_NAME_JSON = "invalid-template-name.json";

    private static final String INVALID_TEMPLATE_DATA_JSON = "invalid-template-data.json";

    private static final String VALID_INPUT_JSON = "valid-input.json";

    @Value("${divorce.document.generator.uri}")
    private String divDocumentGeneratorURI;

    private static final String DOCUMENT_URL_KEY = "url";

    private static final String MIME_TYPE_KEY = "mimeType";

    private static final String APPLICATION_PDF_MIME_TYPE = "application/pdf";

    private static final String X_PATH_TO_URL = "_links.self.href";

    @Test
    public void givenAllTheRightParameters_whenGeneratePDF_thenGeneratedPDFShouldBeStoredInEMStore() throws Exception {
        String requestBody = loadJSON(VALID_INPUT_JSON);
        //check PDF is generated
        Response response = callDivDocumentGenerator(requestBody);
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        String documentUri = response.getBody().jsonPath().get(DOCUMENT_URL_KEY);
        String mimeType = response.getBody().jsonPath().get(MIME_TYPE_KEY);
        Assert.assertEquals(mimeType, APPLICATION_PDF_MIME_TYPE);
        //check the data present in the evidence management
        Response responseFromEvidenceManagement = readDataFromEvidenceManagement(documentUri);
        Assert.assertEquals(HttpStatus.OK.value(), responseFromEvidenceManagement.getStatusCode());
        Assert.assertEquals(documentUri, responseFromEvidenceManagement.getBody().jsonPath().get(X_PATH_TO_URL));
    }

    @Test
    public void givenTemplateIsNotPresent_whenGeneratePDF_thenExpectHttpStatus400() throws Exception {
        String requestBody = loadJSON(INVALID_TEMPLATE_NAME_JSON);
        Response response = callDivDocumentGenerator(requestBody);
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    public void givenRequiredTemplateDataNotPresent_whenGeneratePDF_thenExpectHttpStatus503() throws Exception {
        String requestBody = loadJSON(INVALID_TEMPLATE_DATA_JSON);
        Response response = callDivDocumentGenerator(requestBody);
        Assert.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), response.getStatusCode());
    }

    private Response callDivDocumentGenerator(String requestBody) {
        return given()
            .contentType("application/json")
            .body(requestBody)
            .when()
            .post(divDocumentGeneratorURI)
            .andReturn();
    }

    private String loadJSON(final String fileName) throws Exception {
        return ResourceLoader.loadJSON("documentgenerator/" + fileName);
    }
}
