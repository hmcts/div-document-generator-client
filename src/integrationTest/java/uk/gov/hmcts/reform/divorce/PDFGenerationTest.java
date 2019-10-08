package uk.gov.hmcts.reform.divorce;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.TestData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

@Slf4j
@RunWith(SerenityParameterizedRunner.class)
public class PDFGenerationTest extends IntegrationTest {
    private static final String DOCUMENT_URL_KEY = "url";
    private static final String MIME_TYPE_KEY = "mimeType";
    private static final String APPLICATION_PDF_MIME_TYPE = "application/pdf";

    private static final String INPUT_CONTEXT_PATH_FORMAT = "documentgenerator/documents/jsoninput/%s.json";
    private static final String EXPECTED_OUTPUT_CONTEXT_PATH = "documentgenerator/documents/pdfoutput/%s.pdf";
    private static final String TEMP_OUTPUT_CONTEXT_PATH = "documentgenerator/documents/regenerated/%s.pdf";

    private final String inputJson;
    private final String expectedOutput;
    private final String tempOutput;

    public PDFGenerationTest(String formName) {
        this.inputJson = String.format(INPUT_CONTEXT_PATH_FORMAT, formName);
        this.expectedOutput = String.format(EXPECTED_OUTPUT_CONTEXT_PATH, formName);
        this.tempOutput = String.format(TEMP_OUTPUT_CONTEXT_PATH, formName);
    }

    @TestData
    public static Collection<Object[]> testData() {
        return PDFGenerationSupport.getFormNames(featureToggleRespSolicitor);
    }

    @Test
    public void givenAJsonInput_whenGeneratePDF_thenShouldGenerateExpectedOutput() throws Exception {
        Response actual = generatePdfSuccessfully(inputJson);
        byte[] expected = ResourceLoader.loadResource(expectedOutput);

        Assert.assertEquals(readPdf(expected), readPdf(actual.asByteArray()));
    }

    /**
     * This is not really a test, just a utility to re-generate the PDFs from templates exist in the Docmosis Tornado.
     * The generated PDFs are stored at `integrationTest/resources/documentgenerator/documents/regenerated`
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
        log.info("Generating PDF {} based on request \n{}", inputJson, requestBody);
        Response response = callDivDocumentGenerator(requestBody);
        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertMimeType(response, APPLICATION_PDF_MIME_TYPE);

        String documentUri = getDocumentStoreURI(response.getBody().jsonPath().get(DOCUMENT_URL_KEY));
        log.info("Read data from Evidence Management service: {}", documentUri);
        Response responseFromEvidenceManagement = readDataFromEvidenceManagement(documentUri + "/binary");
        Assert.assertEquals(String.format("Failed to generate PDF:  %s", documentUri),
                HttpStatus.OK.value(), responseFromEvidenceManagement.getStatusCode());

        return responseFromEvidenceManagement;
    }

    private void assertMimeType(final Response response, final String expectedType) {
        String mimeType = response.getBody().jsonPath().get(MIME_TYPE_KEY);
        Assert.assertEquals(mimeType, expectedType);
    }

    //this is a hack to make this work with the docker container
    private String getDocumentStoreURI(final String uri) {
        if (uri.contains("document-management-store:8080")) {
            return uri.replace("http://document-management-store:8080", documentManagementURL);
        }
        return uri;
    }

    private String readPdf(final byte[] pdf) throws Exception {
        PDDocument document = PDDocument.load(pdf);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);

        document.close();

        return text;
    }

    private void savePdf(final byte[] pdf) throws IOException {
        final File expectedPdfFile = new File("src/integrationTest/resources/" + tempOutput);
        try (FileOutputStream fileOutputStream = new FileOutputStream(expectedPdfFile)) {
            fileOutputStream.write(pdf);
            fileOutputStream.flush();
        }
    }
}
