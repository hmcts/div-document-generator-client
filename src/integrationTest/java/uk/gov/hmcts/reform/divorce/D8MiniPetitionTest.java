package uk.gov.hmcts.reform.divorce;

import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.Concurrent;
import net.thucydides.junit.annotations.TestData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collection;

import static net.serenitybdd.rest.SerenityRest.given;

@RunWith(SerenityParameterizedRunner.class)
@Concurrent
public class D8MiniPetitionTest extends IntegrationTest {
    private static final String INPUT_CONTEXT_PATH_FORMAT = "documentgenerator/d8minipetition/jsoninput/%s.json";
    private static final String EXPECTED_OUTPUT_CONTEXT_PATH = "documentgenerator/d8minipetition/pdfoutput/%s.pdf";

    private final String inputJson;
    private final String expectedOutput;

    public D8MiniPetitionTest(String fileName) {
        this.inputJson = String.format(INPUT_CONTEXT_PATH_FORMAT, fileName);
        this.expectedOutput = String.format(EXPECTED_OUTPUT_CONTEXT_PATH, fileName);
    }

    @TestData
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {"CC--No_FO--No_CN--A_DR-AD-CRK-NO-PL-NO-DT-NO_LP--NO"},
                {"CC--Res_FO--No_CN--B_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
                {"CC--Corres_FO--No_CN--C_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
                {"CC--CorresRes_FO--No_CN--AC_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
                {"CC--Res_FO--App_CN--D_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
                {"CC--Corres_FO--App_CN--E_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
                {"CC--CoresRes_FO--App_CN--F_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
                {"CC--Res_FO--Child_CN--G_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
                {"CC--Corres_FO--Child_CN--BCDEFG_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
                {"CC--CorresRes_FO--Child_CN--AD_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
                {"CC--CorresRes_FO--ChildApp_CN--ADEF_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
                {"CC--cores_FO--ChildApp_CN--BCDE_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes"},
                {"CC--cores_FO--ChildApp_CN--BCDE_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes-Special-Chars-Confidential"},
                {"CC--Res_FO--ChildApp_CN--CDEF_DR-UB-CRK-No-PL-No-DT-No_LP--No"},
                {"CC--No_FO--App_CN--EF_DR-S2Y-CRK-No-PL-No-DT-No_LP--No"},
                {"CC--No_FO--Child_CN--BCF_DR-S5Y-CRK-No-PL-No-DT-No_LP--No"},
                {"CC--No_FO--ChildApp_CN--ABCDEFG_DR-DES-CRK-No-PL-No-DT-No_LP--No"}
        });
    }

    @Test
    public void givenAJsonInput_whenGeneratePDF_thenShouldGenerateExpectedOutput() throws Exception {
        String requestBody = ResourceLoader.loadJSON(inputJson);

        //check PDF is generated
        Response response = callDivDocumentGenerator(requestBody);

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        String documentUri = response.getBody().jsonPath().get(DOCUMENT_URL_KEY);

        documentUri = getDocumentStoreURI(documentUri);

        String mimeType = response.getBody().jsonPath().get(MIME_TYPE_KEY);

        Assert.assertEquals(mimeType, APPLICATION_PDF_MIME_TYPE);

        //check the data present in the evidence management
        Response responseFromEvidenceManagement = readDataFromEvidenceManagement(documentUri + "/binary");

        Assert.assertEquals(HttpStatus.OK.value(), responseFromEvidenceManagement.getStatusCode());

        Assert.assertEquals(readPdf(ResourceLoader.loadResource(expectedOutput)), readPdf(responseFromEvidenceManagement.asByteArray()));
    }

    private String readPdf(byte[] pdf) throws Exception {
        PDDocument document = PDDocument.load(pdf);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);

        document.close();

        return text;
    }

    private static final String DOCUMENT_URL_KEY = "url";
    private static final String MIME_TYPE_KEY = "mimeType";
    private static final String APPLICATION_PDF_MIME_TYPE = "application/pdf";

    @Value("${divorce.document.generator.uri}")
    private String divDocumentGeneratorURI;

    @Value("${document.management.store.baseUrl}")
    private String documentManagementURL;

    private Response callDivDocumentGenerator(String requestBody) {
        System.out.println("divDocumentGeneratorURI: " + divDocumentGeneratorURI);

        Response response = given()
            .contentType("application/json")
            .body(requestBody)
            .when()
            .post(divDocumentGeneratorURI)
            .andReturn();

        System.out.println("response: " + response.getBody());

        return response;
    }

    /**
     * Given the uri it will update the url to corresponding localhost url for testing with docker
     *
     * @param uri the link to be updated
     * @return updated url
     */
    //this is a hack to make this work with the docker container
    private String getDocumentStoreURI(String uri) {
        if (uri.contains("document-management-store:8080")) {
            return uri.replace("http://document-management-store:8080", documentManagementURL);
        }

        return uri;
    }

    private Response readDataFromEvidenceManagement(String uri) {
        return EvidenceManagementUtil.readDataFromEvidenceManagement(uri, "addcaseworkertoken");
    }
}
