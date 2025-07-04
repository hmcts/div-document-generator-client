package uk.gov.hmcts.reform.divorce.documentgenerator.management.monitoring.health;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.divorce.documentgenerator.DocumentGeneratorApplication;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(
        classes = {DocumentGeneratorApplication.class, HealthCheckITest.LocalRibbonClientConfiguration.class})
@PropertySource(value = "classpath:application.properties")
@TestPropertySource(properties = {
        "management.endpoint.health.cache.time-to-live=0"
    })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class HealthCheckITest {

    private static final String HEALTH_UP_RESPONSE = "{ \"status\": \"UP\"}";
    private static final String HEALTH_DOWN_RESPONSE = "{ \"status\": \"DOWN\"}";

    @LocalServerPort
    private int port;

    @Value("${service.evidence-management-client-api.health.uri}")
    private String evidenceManagementClientHealthUrl;

    @Value("${service.pdf-service.health.uri}")
    private String pdfServiceHealthUrl;

    @Value("${service.service-auth-provider.health.uri}")
    private String serviceAuthHealthUrl;

    @Value("${service.service-auth-provider.health.context-path}")
    private String serviceAuthHealthContextPath;

    @ClassRule
    public static WireMockClassRule serviceAuthServer = new WireMockClassRule(4502);

    @Autowired
    @Qualifier("healthCheckRestTemplate")
    private RestTemplate restTemplate;

    private String healthUrl;
    private MockRestServiceServer mockRestServiceServer;
    private ClientHttpRequestFactory originalRequestFactory;
    private final HttpClient httpClient = HttpClients.createMinimal();

    private HttpResponse getHealth() throws Exception {
        final HttpGet request = new HttpGet(healthUrl);
        request.addHeader("Accept", "application/json;charset=UTF-8");

        return httpClient.execute(request);
    }

    @Before
    public void setUp() {
        healthUrl = "http://localhost:" + String.valueOf(port) + "/health";
        originalRequestFactory = restTemplate.getRequestFactory();
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @After
    public void tearDown() {
        restTemplate.setRequestFactory(originalRequestFactory);
    }

    @Test
    public void givenAllDependenciesAreUp_whenCheckHealth_thenReturnStatusUp() throws Exception {
        mockEndpointAndResponse(evidenceManagementClientHealthUrl, true);
        mockEndpointAndResponse(pdfServiceHealthUrl, true);
        mockEndpointAndResponse(serviceAuthHealthUrl, true);
        mockServiceAuthFeignHealthCheck();

        HttpResponse response = getHealth();
        String body = EntityUtils.toString(response.getEntity());

        assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
        assertThat(JsonPath.read(body, "$.status").toString(),
            equalTo("UP"));
        assertThat(JsonPath.read(body, "$.components.evidenceManagementClientHealthCheck.status").toString(),
            equalTo("UP"));
        assertThat(JsonPath.read(body, "$.components.PDFServiceHealthCheck.status").toString(),
            equalTo("UP"));
        assertThat(JsonPath.read(body, "$.components.serviceAuthProviderHealthCheck.status").toString(),
            equalTo("UP"));
        assertThat(JsonPath.read(body, "$.components.diskSpace.status").toString(),
            equalTo("UP"));
    }

    @Test
    public void givenAllDependenciesAreDown_whenCheckHealth_thenReturnStatusDown() throws Exception {
        mockEndpointAndResponse(evidenceManagementClientHealthUrl, false);
        mockEndpointAndResponse(pdfServiceHealthUrl, false);
        mockEndpointAndResponse(serviceAuthHealthUrl, false);

        HttpResponse response = getHealth();
        String body = EntityUtils.toString(response.getEntity());

        assertThat(response.getStatusLine().getStatusCode(),
            equalTo(503));
        assertThat(JsonPath.read(body, "$.status").toString(), equalTo("DOWN"));
        assertThat(JsonPath.read(body, "$.components.evidenceManagementClientHealthCheck.status").toString(),
            equalTo("DOWN"));
        assertThat(JsonPath.read(body, "$.components.PDFServiceHealthCheck.status").toString(),
            equalTo("DOWN"));
        assertThat(JsonPath.read(body, "$.components.serviceAuthProviderHealthCheck.status").toString(),
            equalTo("DOWN"));
        assertThat(JsonPath.read(body, "$.components.diskSpace.status").toString(),
            equalTo("UP"));
    }

    @Test
    public void givenPDFServiceIsDown_whenCheckHealth_thenReturnStatusDown() throws Exception {
        mockEndpointAndResponse(evidenceManagementClientHealthUrl, true);
        mockEndpointAndResponse(pdfServiceHealthUrl, false);
        mockEndpointAndResponse(serviceAuthHealthUrl, true);

        HttpResponse response = getHealth();
        String body = EntityUtils.toString(response.getEntity());

        assertThat(response.getStatusLine().getStatusCode(),
            equalTo(503));
        assertThat(JsonPath.read(body, "$.status").toString(),
            equalTo("DOWN"));
        assertThat(JsonPath.read(body, "$.components.evidenceManagementClientHealthCheck.status").toString(),
            equalTo("UP"));
        assertThat(JsonPath.read(body, "$.components.PDFServiceHealthCheck.status").toString(),
            equalTo("DOWN"));
        assertThat(JsonPath.read(body, "$.components.serviceAuthProviderHealthCheck.status").toString(),
            equalTo("UP"));
        assertThat(JsonPath.read(body, "$.components.diskSpace.status").toString(),
            equalTo("UP"));
    }

    @Test
    public void givenEvidenceManagementClientIsDown_whenCheckHealth_thenReturnStatusDown() throws Exception {
        mockEndpointAndResponse(evidenceManagementClientHealthUrl, false);
        mockEndpointAndResponse(pdfServiceHealthUrl, true);
        mockEndpointAndResponse(serviceAuthHealthUrl, true);

        HttpResponse response = getHealth();
        String body = EntityUtils.toString(response.getEntity());

        assertThat(response.getStatusLine().getStatusCode(), equalTo(503));
        assertThat(JsonPath.read(body, "$.status").toString(),
            equalTo("DOWN"));
        assertThat(JsonPath.read(body, "$.components.evidenceManagementClientHealthCheck.status").toString(),
            equalTo("DOWN"));
        assertThat(JsonPath.read(body, "$.components.PDFServiceHealthCheck.status").toString(),
            equalTo("UP"));
        assertThat(JsonPath.read(body, "$.components.serviceAuthProviderHealthCheck.status").toString(),
            equalTo("UP"));
        assertThat(JsonPath.read(body, "$.components.diskSpace.status").toString(),
            equalTo("UP"));
    }

    @Test
    public void givenAuthServiceIsDown_whenCheckHealth_thenReturnStatusDown() throws Exception {
        mockEndpointAndResponse(evidenceManagementClientHealthUrl, true);
        mockEndpointAndResponse(pdfServiceHealthUrl, true);
        mockEndpointAndResponse(serviceAuthHealthUrl, false);

        HttpResponse response = getHealth();
        String body = EntityUtils.toString(response.getEntity());

        assertThat(response.getStatusLine().getStatusCode(), equalTo(503));
        assertThat(JsonPath.read(body, "$.status").toString(),
            equalTo("DOWN"));
        assertThat(JsonPath.read(body, "$.components.evidenceManagementClientHealthCheck.status").toString(),
            equalTo("UP"));
        assertThat(JsonPath.read(body, "$.components.PDFServiceHealthCheck.status").toString(),
            equalTo("UP"));
        assertThat(JsonPath.read(body, "$.components.serviceAuthProviderHealthCheck.status").toString(),
            equalTo("DOWN"));
        assertThat(JsonPath.read(body, "$.components.diskSpace.status").toString(),
            equalTo("UP"));
    }

    private void mockEndpointAndResponse(String requestUrl, boolean serviceUp) {
        mockRestServiceServer.expect(once(), requestTo(requestUrl)).andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(serviceUp ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE)
                        .body(serviceUp ? HEALTH_UP_RESPONSE : HEALTH_DOWN_RESPONSE)
                        .contentType(MediaType.APPLICATION_JSON));
    }

    private void mockServiceAuthFeignHealthCheck() {
        serviceAuthServer.stubFor(get(serviceAuthHealthContextPath)
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(HEALTH_UP_RESPONSE)));
    }

    @TestConfiguration
    public static class LocalRibbonClientConfiguration {

    }
}
