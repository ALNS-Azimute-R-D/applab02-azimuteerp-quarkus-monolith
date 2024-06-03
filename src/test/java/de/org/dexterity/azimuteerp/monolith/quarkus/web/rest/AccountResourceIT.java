package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;

import de.org.dexterity.azimuteerp.monolith.quarkus.infrastructure.InjectKeycloakServer;
import de.org.dexterity.azimuteerp.monolith.quarkus.infrastructure.KeycloakServerResource;
import de.org.dexterity.azimuteerp.monolith.quarkus.utility.IntegrationTestBase;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusIntegrationTest;
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
