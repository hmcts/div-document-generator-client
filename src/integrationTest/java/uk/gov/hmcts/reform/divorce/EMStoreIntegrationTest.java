package uk.gov.hmcts.reform.divorce;

import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;

import static org.springframework.http.HttpStatus.OK;

@RunWith(SerenityRunner.class)
public class EMStoreIntegrationTest extends IntegrationTest {

    private static final String VALID_INPUT_JSON = "valid-input.json";
    private static final String COE_INPUT_JSON = "coe.json";
    private static final String DECREE_NISI_INPUT_JSON = "dn.json";
    private static final String DECREE_ABSOLUTE_INPUT_JSON = "da.json";
    private static final String DOCUMENT_URL_KEY = "url";
    private static final String MIME_TYPE_KEY = "mimeType";
    private static final String X_PATH_TO_URL = "_links.self.href";
    private static final String COSTS_ORDER_INPUT_JSON = "costs-order.json";
    private static final String DN_SUBMISSION_INPUT_JSON = "dn-submission.json";
    private static final String DN_REFUSAL_ORDER_INPUT_JSON
        = "documents/jsoninput/decree-nisi-refusal-order-clarification.json";

    @Value("${divorce.document.generator.uri}")

    public void checkPDFGenerated(String filename) throws Exception {
        String requestBody = loadJson(filename);
        Response response = callDivDocumentGenerator(requestBody);
        Assert.assertEquals(OK.value(), response.getStatusCode());

        String documentUri = response.getBody().jsonPath().get(DOCUMENT_URL_KEY);
        checkDataPresentInEvidenceManagement(documentUri);

        String mimeType = response.getBody().jsonPath().get(MIME_TYPE_KEY);
        Assert.assertNotNull(mimeType);
    }

    public void checkDataPresentInEvidenceManagement(String documentUri) {
        Response responseFromEvidenceManagement = readDataFromEvidenceManagement(documentUri);
        Assert.assertEquals(OK.value(), responseFromEvidenceManagement.getStatusCode());
        Assert.assertEquals(documentUri, responseFromEvidenceManagement.getBody().jsonPath().get(X_PATH_TO_URL));
    }

    @Test
    public void givenAllTheRightParameters_whenGeneratePDF_thenGeneratedPDFShouldBeStoredInEMStore() throws Exception {
        checkPDFGenerated(VALID_INPUT_JSON);
    }

    @Test
    public void givenAllTheRightParameters_whenGenerateCoEWithDocmosis_thenGeneratedPDFShouldBeStoredInEMStore()
        throws Exception {
        checkPDFGenerated(COE_INPUT_JSON);
    }

    @Test
    public void givenAllTheRightParameters_whenGenerateCostsOrderWithDocmosis_thenGeneratedPDFShouldBeStoredInEMStore()
        throws Exception {
        checkPDFGenerated(COSTS_ORDER_INPUT_JSON);
    }

    @Test
    public void givenAllTheRightParameters_whenGenerateDecreeNisiWithDocmosis_thenGeneratedPDFShouldBeStoredInEMStore()
        throws Exception {
        checkPDFGenerated(DECREE_NISI_INPUT_JSON);
    }

    @Test
    public void givenAllTheRightParameters_whenGenerateDecreeAbosluteWithDocmosis_thenGeneratedPDFShouldBeStoredInEMStore()
        throws Exception {
        checkPDFGenerated(DECREE_ABSOLUTE_INPUT_JSON);
    }

    @Test
    public void givenAllTheRightParameters_whenGenerateDNAnswersWithDocmosis_thenGeneratedPDFShouldBeStoredInEMStore()
        throws Exception {
        checkPDFGenerated(DN_SUBMISSION_INPUT_JSON);
    }

    @Test
    public void givenAllTheRightParameters_whenGenerateDnRefusalOrderWithDocmosis_thenGeneratedPDFShouldBeStoredInEMStore()
        throws Exception {
        checkPDFGenerated(DN_REFUSAL_ORDER_INPUT_JSON);
    }

    private String loadJson(final String fileName) throws Exception {
        return ResourceLoader.loadJson("documentgenerator/" + fileName);
    }
}
