package uk.gov.hmcts.reform.divorce;

import static net.serenitybdd.rest.SerenityRest.given;

import java.util.HashMap;
import java.util.Map;

import io.restassured.response.Response;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EvidenceManagementUtil {

    public static Response readDataFromEvidenceManagement(String uri, String caseWorkerToken) {
        Map<String, Object> headers = new HashMap<>();

        headers.put("Authorization", caseWorkerToken);
        headers.put("user-roles", "caseworker-divorce");

        return given()
                .contentType("application/json")
                .headers(headers)
                .when()
                .get(uri)
                .andReturn();
    }
}
