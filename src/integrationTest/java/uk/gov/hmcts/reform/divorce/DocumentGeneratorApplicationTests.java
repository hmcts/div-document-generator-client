package uk.gov.hmcts.reform.divorce;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import uk.gov.hmcts.reform.divorce.documentgenerator.category.SmokeTest;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

@Category(SmokeTest.class)
public class DocumentGeneratorApplicationTests extends IntegrationTest {

    @Test
    public void shouldHaveHealthyService() {
        when().get(divDocumentGeneratorBaseURI + "/health")
            .then().statusCode(200).body("status", is("UP"));
    }

}
