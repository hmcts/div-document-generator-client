package uk.gov.hmcts.reform.divorce;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule;
import org.assertj.core.util.Strings;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.UUID;
import javax.annotation.PostConstruct;

@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServiceContextConfiguration.class})
public abstract class IntegrationTest {
    private static final String GENERIC_PASSWORD = "genericPassword123";

    @Value("${divorce.document.generator.uri}")
    protected String divDocumentGeneratorURI;

    @Value("${document.management.store.baseUrl}")
    protected String documentManagementURL;

    @Value("${http.proxy:#{null}}")
    protected String httpProxy;

    @Autowired
    private AuthTokenGenerator authTokenGenerator;

    @Autowired
    private IdamUtils idamTestSupportUtil;

    @Rule
    public SpringIntegrationMethodRule springMethodIntegration;

    private static String userToken = null;
    private String username;

    protected static boolean featureToggleRespSolicitor;

    @Value("${feature-toggle.toggle.feature_resp_solicitor_details}")
    public void setFeatureToggleRespSolicitor(String toggle) {
        IntegrationTest.featureToggleRespSolicitor = Boolean.valueOf(toggle);
    }

    IntegrationTest() {
        this.springMethodIntegration = new SpringIntegrationMethodRule();
    }

    @PostConstruct
    public void init() {
        if (!Strings.isNullOrEmpty(httpProxy)) {
            configProxyHost();
        }
    }

    Response readDataFromEvidenceManagement(String uri) {
        getUserToken();
        return EvidenceManagementUtil.readDataFromEvidenceManagement(
            uri, authTokenGenerator.generate(), username);
    }

    Response callDivDocumentGenerator(String requestBody) {
        return DocumentGeneratorUtil.generatePDF(requestBody,
                                                divDocumentGeneratorURI,
                                                getUserToken());
    }


    private synchronized String getUserToken() {
        username = "simulate-delivered" + UUID.randomUUID() + "@notifications.service.gov.uk";

        if (userToken == null) {
            idamTestSupportUtil.createCaseworkerUserInIdam(username, GENERIC_PASSWORD);

            userToken = idamTestSupportUtil.generateUserTokenWithNoRoles(username, GENERIC_PASSWORD);
        }

        return userToken;
    }

    private void configProxyHost() {
        try {
            URL proxy = new URL(httpProxy);
            if (InetAddress.getByName(proxy.getHost()).isReachable(2000)) {
                System.setProperty("http.proxyHost", proxy.getHost());
                System.setProperty("http.proxyPort", Integer.toString(proxy.getPort()));
                System.setProperty("https.proxyHost", proxy.getHost());
                System.setProperty("https.proxyPort", Integer.toString(proxy.getPort()));
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            log.error("Error setting up proxy - are you connected to the VPN?", e);
            throw new RuntimeException(e);
        }
    }
}
