package uk.gov.hmcts.reform.divorce;

import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.TestData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(SerenityParameterizedRunner.class)
public class PDFGenerationTest extends IntegrationTest {
    private static final String DOCUMENT_URL_KEY = "url";
    private static final String MIME_TYPE_KEY = "mimeType";
    private static final String APPLICATION_PDF_MIME_TYPE = "application/pdf";

    private static final String INPUT_CONTEXT_PATH_FORMAT = "documentgenerator/documents/jsoninput/%s.json";
    private static final String EXPECTED_OUTPUT_CONTEXT_PATH = "documentgenerator/documents/pdfoutput/%s.pdf";

    private final String inputJson;
    private final String expectedOutput;

    @Value("${feature-toggle.resp-solicitor-details}")
    private static boolean featureToggleRespSolicitor;

    public PDFGenerationTest(String fileName) {
        this.inputJson = String.format(INPUT_CONTEXT_PATH_FORMAT, fileName);
        this.expectedOutput = String.format(EXPECTED_OUTPUT_CONTEXT_PATH, fileName);
    }

    @TestData
    public static Collection<Object[]> testData() {
        List<Object[]> basicTestData = Arrays.asList(new Object[][]{
                {"mini-petition-draft"},
                {"mini-petition-draft-no-place-of-marriage"},
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
                {"CC--No_FO--ChildApp_CN--ABCDEFG_DR-DES-CRK-No-PL-No-DT-No_LP--No"},
                {"CC--cores_FO--ChildApp_CN--BCDE_DR-UB-CRK-Yes-PL-Yes-DT-Yes_LP--Yes-Long-SoC-Solicitors"},
                {"CC--cores_FO--ChildApp_CN--BCDE_DR-AD-CRK-Yes-PL-Yes-DT-Yes_LP--Yes-Petitioner-Solicitor"},
                {"5YearSeparationWithMentalSeparationDate"},
                {"AOS_Co-respondent_Online"},
                {"AOS_Co-respondent_Paper"},
                {"Desertion"},
                {"Desertion-Amend-Case"},
                {"Resp_2Year_Defend_Response"},
                {"Resp_2Year_Undefend_Response"},
                {"Resp_5Year_Defend_Response_No_Claim_Costs"},
                {"Resp_5Year_Undefend_Response"},
                {"Resp_Adultery_Defend_Response"},
                {"Resp_Adultery_Undefend_Response"},
                {"Resp_Behaviour_Defend_Response_No_Claim_Costs"},
                {"Resp_Behaviour_Undefend_noAdmit_Response"},
                {"Resp_Behaviour_Undefend_Response"},
                {"Resp_Desertion_Defend_Response"},
                {"Resp_Desertion_Undefend_noAdmit_Response"},
                {"Resp_Desertion_Undefend_Response"},
                {"co-respondent-answers-defended-admit-costs"},
                {"co-respondent-answers-undefended-no-admit-no-costs"}
        });

        List testData = new ArrayList(basicTestData);

        if (featureToggleRespSolicitor) {
            testData.addAll(Arrays.asList(new Object[][] {
                {"AOS_Solicitor"},
                {"AOS_Hus_Res-Addr_DivUnit-SC-Sol-Online-Avl"},
                {"AOS_Same-Sex-Female-Sol-Online-Avl"},
                {"AOS_Same-Sex-Male-Sol-Online-Avl"},
                {"AOS_Amend_Petition-Sol-Online-Avl"},
                {"AOS_Hus_Res-Addr_DivUnit-EM-Sol-Online-Avl"}
            }));
        } else {
            testData.addAll(Arrays.asList(new Object[][] {
                {"AOS_Wife_Sol-Addr_DivUnit-WM"},
                {"AOS_Wife_Sol-Addr_DivUnit-SW_No-Sol-Company"},
                {"AOS_Hus_Res-Addr_DivUnit-SC"},
                {"AOS_Same-Sex-Female"},
                {"AOS_Same-Sex-Male"},
                {"AOS_Amend_Petition"},
                {"AOS_Hus_Res-Addr_DivUnit-EM"}
            }));
        }
        return testData;
    }

    @Test
    public void givenAJsonInput_whenGeneratePDF_thenShouldGenerateExpectedOutput() throws Exception {
        Response actual = generatePdfSuccessfully(inputJson);
        byte[] expected = ResourceLoader.loadResource(expectedOutput);

        Assert.assertEquals(readPdf(expected), readPdf(actual.asByteArray()));
    }

    /**
     * This is not really a test, just a utility to re-generate the PDFs after changing a template.
     *
     * <p>Should be @ignored in master branch.
     * */
    @Test
    @Ignore
    public void ignoreMe_updateGeneratedPdfs() throws Exception  {
        Response responseFromEvidenceManagement = generatePdfSuccessfully(inputJson);

        savePdf(responseFromEvidenceManagement.asByteArray());
    }

    private Response generatePdfSuccessfully(String inputJson) throws Exception {
        String requestBody = ResourceLoader.loadJson(inputJson);
        Response response = callDivDocumentGenerator(requestBody);
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        String documentUri = response.getBody().jsonPath().get(DOCUMENT_URL_KEY);
        documentUri = getDocumentStoreURI(documentUri);
        String mimeType = response.getBody().jsonPath().get(MIME_TYPE_KEY);
        Assert.assertEquals(mimeType, APPLICATION_PDF_MIME_TYPE);
        Response responseFromEvidenceManagement = readDataFromEvidenceManagement(documentUri + "/binary");
        Assert.assertEquals(HttpStatus.OK.value(), responseFromEvidenceManagement.getStatusCode());

        return responseFromEvidenceManagement;
    }

    private String readPdf(byte[] pdf) throws Exception {
        PDDocument document = PDDocument.load(pdf);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);

        document.close();

        return text;
    }

    private void savePdf(byte[] pdf) throws IOException {
        File expectedPdfFile = new File("src/integrationTest/resources/" + expectedOutput);
        try (FileOutputStream fileOutputStream = new FileOutputStream(expectedPdfFile)) {
            fileOutputStream.write(pdf);
            fileOutputStream.flush();
        }
    }
}
