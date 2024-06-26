package org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.InjectKeycloakServer;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.KeycloakServerResource;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.utility.IntegrationTestBase;
import org.junit.jupiter.api.Test;

@QuarkusIntegrationTest
@TestHTTPEndpoint(AccountResource.class)
@QuarkusTestResource(value = KeycloakServerResource.class, restrictToAnnotatedClass = true)
public class AccountResourceIT extends IntegrationTestBase {

    @InjectKeycloakServer
    String keycloakServerUrl;

    @Override
    public String getKeycloakServerUrl() {
        return keycloakServerUrl;
    }

    @Test
    public void testGetUnknownAccount() {
        given().auth().oauth2("").accept(APPLICATION_JSON).get("/account").then().statusCode(UNAUTHORIZED.getStatusCode());
    }
}
