package uk.gov.hmcts.reform.divorce;

import io.restassured.RestAssured;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.gov.hmcts.reform.divorce.documentgenerator.category.SmokeTest;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

@Category(SmokeTest.class)
public class DocumentGeneratorSmokeTest extends IntegrationTest {

    @Test
    public void shouldHaveHealthyService() {
        RestAssured.useRelaxedHTTPSValidation();
        
        when().get(divDocumentGeneratorBaseURI + "/health")
        .then()
        .statusCode(200)
        .body("status", is("UP"));
    }

}
