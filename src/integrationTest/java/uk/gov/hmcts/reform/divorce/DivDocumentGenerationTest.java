package uk.gov.hmcts.reform.divorce;

import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignAutoConfiguration;
import org.springframework.cloud.netflix.feign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

import static net.serenitybdd.rest.SerenityRest.given;

@Lazy
@RunWith(SerenityRunner.class)
@ComponentScan(basePackages = {"uk.gov.hmcts.reform.divorce.emclient", "uk.gov.hmcts.auth.provider.service"})
@ImportAutoConfiguration({RibbonAutoConfiguration.class,HttpMessageConvertersAutoConfiguration.class,
    FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@ContextConfiguration(classes = {ServiceContextConfiguration.class})
@EnableFeignClients(basePackageClasses = ServiceAuthorisationApi.class)
@PropertySource({"classpath:application-${env:local}.properties"})
public class DivDocumentGenerationTest {

    private static final String INVALID_TEMPLATE_NAME_JSON = "invalid-template-name.json";

    private static final String INVALID_TEMPLATE_DATA_JSON = "invalid-template-data.json";

    private static final String VALID_INPUT_JSON = "valid-input.json";

    @Rule
    public SpringIntegrationMethodRule springMethodIntegration = new SpringIntegrationMethodRule();

    @Value("${evidence.management.client.api.baseUrl}")
    private String evidenceManagementClientApiBaseUrl;

    @Value("${evidence.management.client.api.endpoint.uploadwiths2stoken}")
    private String emClientApiUploadEndpoint;

    @Autowired
    private IDAMUtils idamTestSupportUtil;

    private String name;

    private String fileType;

    @Value("${divorce.document.generator.uri}")
    private String divDocumentGeneratorURI;

    @Value("${document.management.store.baseUrl}")
    private String documentManagementURL;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    protected static final String DOCUMENT_URL_KEY = "url";

    protected static final String MIME_TYPE_KEY = "mimeType";

    protected static final String APPLICATION_PDF_MIME_TYPE = "application/pdf";

    static final String X_PATH_TO_URL = "_links.self.href";

    @Test
    public void givenAllTheRightParameters_whenGeneratePDF_thenGeneratedPDFShouldBeStoredInEMStore() throws Exception {
//        System.out.println(authTokenGenerator.generate());
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

    protected Response callDivDocumentGenerator(String requestBody) {
        return given()
            .contentType("application/json")
            .body(requestBody)
            .when()
            .post(divDocumentGeneratorURI)
            .andReturn();
    }

    String loadJSON(final String fileName) throws Exception {
        return ResourceLoader.loadJSON("documentgenerator/" + fileName);
    }

    protected Response readDataFromEvidenceManagement(String uri) {
        return EvidenceManagementUtil.readDataFromEvidenceManagement(uri, "addtoken");
    }
}
