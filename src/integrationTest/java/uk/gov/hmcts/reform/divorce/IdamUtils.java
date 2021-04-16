package uk.gov.hmcts.reform.divorce;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import uk.gov.hmcts.reform.divorce.model.CreateUserRequest;
import uk.gov.hmcts.reform.divorce.model.UserCode;

import java.util.Base64;
import java.util.UUID;

@Slf4j
public class IdamUtils {
    private static final String GENERIC_PASSWORD = "genericPassword123";

    @Value("${auth.idam.client.baseUrl}")
    private String idamUserBaseUrl;

    @Value("${auth.idam.client.redirectUri}")
    private String idamRedirectUri;

    @Value("${auth.idam.client.secret}")
    private String idamSecret;

    private String idamUsername;

    private String idamPassword;

    private String testUserJwtToken;

    private String testCaseworkerJwtToken;

    public String generateNewUserAndReturnToken() {
        String username = "simulate-delivered" + UUID.randomUUID() + "@notifications.service.gov.uk";
        createUserInIdam(username, GENERIC_PASSWORD);
        return generateUserTokenWithNoRoles(username, GENERIC_PASSWORD);
    }

    private void createUserInIdam(String username, String password) {
        CreateUserRequest userRequest = CreateUserRequest.builder()
            .email(username)
            .password(password)
            .forename("Test")
            .surname("User")
            .roles(new UserCode[] { UserCode.builder().code("citizen").build() })
            .build();

        SerenityRest.given()
            .header("Content-Type", "application/json")
            .body(ResourceLoader.objectToJson(userRequest))
            .post(idamCreateUrl());
    }

    private void createUserInIdam() {
        idamUsername = "simulate-delivered" + UUID.randomUUID() + "@notifications.service.gov.uk";

        createUserInIdam(idamUsername, GENERIC_PASSWORD);
    }

    public void createCaseworkerUserInIdam(String username, String password) {
        CreateUserRequest userRequest = CreateUserRequest.builder()
            .email(username)
            .password(password)
            .forename("Test")
            .surname("User")
            .roles(new UserCode[] { UserCode.builder().code("caseworker-divorce-courtadmin").build() })
            .build();

        Response response = SerenityRest.given()
            .header("Content-Type", "application/json")
            .body(ResourceLoader.objectToJson(userRequest))
            .post(idamCreateUrl());

        if (response.statusCode() < 300) {
            log.info("Created test user {}", username);
        } else {
            log.info("Failed to create test user {}, response code {} and body {}", username, response.statusCode(), response.body());
        }
    }

    private String idamCreateUrl() {
        return idamUserBaseUrl + "/testing-support/accounts";
    }

    public String generateUserTokenWithNoRoles(String username, String password) {
        String userLoginDetails = String.join(":", username, password);
        final String authHeader = "Basic " + new String(Base64.getEncoder().encode((userLoginDetails).getBytes()));

        Response response = SerenityRest.given()
            .header("Authorization", authHeader)
            .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .relaxedHTTPSValidation()
            .post(idamCodeUrl());

        if (response.statusCode() >= 300) {
            throw new IllegalStateException("Token generation failed with code: " + response.statusCode()
                + " body: " + response.body().prettyPrint());
        }

        response = SerenityRest.given()
            .relaxedHTTPSValidation()
            .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .post(idamTokenUrl(response.body().path("code")));

        String token = response.body().path("access_token");
        return "Bearer " + token;
    }

    public void deleteTestUser(String username) {
        Response response = SerenityRest.given()
            .relaxedHTTPSValidation()
            .delete(idamDeleteUserUrl(username));

        if (response.statusCode() < 300) {
            log.info("Deleted test user {}", username);
        } else {
            log.error("Failed to delete test user {}", username);
        }
    }

    private String idamDeleteUserUrl(String username) {
        return idamUserBaseUrl + "/testing-support/accounts/" + username;
    }

    private String idamCodeUrl() {
        return idamUserBaseUrl + "/oauth2/authorize"
            + "?response_type=code"
            + "&client_id=divorce"
            + "&redirect_uri=" + idamRedirectUri;
    }

    private String idamTokenUrl(String code) {

        return idamUserBaseUrl + "/oauth2/token"
            + "?code=" + code
            + "&client_id=divorce"
            + "&client_secret=" + idamSecret
            + "&redirect_uri=" + idamRedirectUri
            + "&grant_type=authorization_code";
    }
}
