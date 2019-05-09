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

import static net.serenitybdd.rest.SerenityRest.given;

@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServiceContextConfiguration.class})
public abstract class IntegrationTest {
    private static final String GENERIC_PASSWORD = "genericPassword123";

    @Value("${divorce.document.generator.uri}")
    private String divDocumentGeneratorURI;

    @Value("${document.management.store.baseUrl}")
    private String documentManagementURL;

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

    IntegrationTest() {
        this.springMethodIntegration = new SpringIntegrationMethodRule();
    }

    @PostConstruct
    public void init() {
        if (!Strings.isNullOrEmpty(httpProxy)) {
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

    Response readDataFromEvidenceManagement(String uri) {
        getUserToken();
        return EvidenceManagementUtil.readDataFromEvidenceManagement(
            uri, authTokenGenerator.generate(), username);
    }

    Response callDivDocumentGenerator(String requestBody) {
        return given()
            .contentType("application/json")
            .header("Authorization", getUserToken())
            .body(requestBody)
            .when()
            .post(divDocumentGeneratorURI)
            .andReturn();
    }

    //this is a hack to make this work with the docker container
    String getDocumentStoreURI(String uri) {
        if (uri.contains("document-management-store:8080")) {
            return uri.replace("http://document-management-store:8080", documentManagementURL);
        }

        return uri;
    }

    private synchronized String getUserToken() {
        username = "simulate-delivered" + UUID.randomUUID() + "@notifications.service.gov.uk";

        if (userToken == null) {
            idamTestSupportUtil.createCaseworkerUserInIdam(username, GENERIC_PASSWORD);

            userToken = idamTestSupportUtil.generateUserTokenWithNoRoles(username, GENERIC_PASSWORD);
        }

        return userToken;
    }
}
