package uk.gov.hmcts.reform.divorce;

import io.restassured.response.Response;
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServiceContextConfiguration.class})
public abstract class IntegrationTest {

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Autowired
    private IDAMUtils idamTestSupportUtil;

    @Rule
    public SpringIntegrationMethodRule springMethodIntegration;

    public IntegrationTest() {
        this.springMethodIntegration = new SpringIntegrationMethodRule();
    }

    Response readDataFromEvidenceManagement(String uri) {
        idamTestSupportUtil.createDivorceCaseworkerUserInIdam("CaseWorkerTest", "password");
        return EvidenceManagementUtil.readDataFromEvidenceManagement(uri, authTokenGenerator.generate(), "CaseWorkerTest");
    }
}
