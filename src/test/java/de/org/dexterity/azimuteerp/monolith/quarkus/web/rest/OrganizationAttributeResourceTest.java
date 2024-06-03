package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import de.org.dexterity.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrganizationAttributeDTO;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import java.util.List;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class OrganizationAttributeResourceTest {

    private static final TypeRef<OrganizationAttributeDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<OrganizationAttributeDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_ATTRIBUTE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ATTRIBUTE_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_VALUE = "BBBBBBBBBB";

    String adminToken;

    OrganizationAttributeDTO organizationAttributeDTO;

    @Inject
    LiquibaseFactory liquibaseFactory;

    @BeforeAll
    static void jsonMapper() {
        RestAssured.config = RestAssured.config()
            .objectMapperConfig(objectMapperConfig().defaultObjectMapper(TestUtil.jsonbObjectMapper()));
    }

    @BeforeEach
    public void authenticateAdmin() {
        this.adminToken = TestUtil.getAdminToken();
    }

    @BeforeEach
    public void databaseFixture() {
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            liquibase.dropAll();
            liquibase.validate();
            liquibase.update(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationAttributeDTO createEntity() {
        var organizationAttributeDTO = new OrganizationAttributeDTO();
        organizationAttributeDTO.attributeName = DEFAULT_ATTRIBUTE_NAME;
        organizationAttributeDTO.attributeValue = DEFAULT_ATTRIBUTE_VALUE;
        return organizationAttributeDTO;
    }

    @BeforeEach
    public void initTest() {
        organizationAttributeDTO = createEntity();
    }

    @Test
    public void createOrganizationAttribute() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-attributes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the OrganizationAttribute
        organizationAttributeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationAttributeDTO)
            .when()
            .post("/api/organization-attributes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the OrganizationAttribute in the database
        var organizationAttributeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-attributes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationAttributeDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testOrganizationAttributeDTO = organizationAttributeDTOList
            .stream()
            .filter(it -> organizationAttributeDTO.id.equals(it.id))
            .findFirst()
            .get();
        assertThat(testOrganizationAttributeDTO.attributeName).isEqualTo(DEFAULT_ATTRIBUTE_NAME);
        assertThat(testOrganizationAttributeDTO.attributeValue).isEqualTo(DEFAULT_ATTRIBUTE_VALUE);
    }

    @Test
    public void createOrganizationAttributeWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-attributes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the OrganizationAttribute with an existing ID
        organizationAttributeDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationAttributeDTO)
            .when()
            .post("/api/organization-attributes")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the OrganizationAttribute in the database
        var organizationAttributeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-attributes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationAttributeDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void updateOrganizationAttribute() {
        // Initialize the database
        organizationAttributeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationAttributeDTO)
            .when()
            .post("/api/organization-attributes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-attributes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the organizationAttribute
        var updatedOrganizationAttributeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-attributes/{id}", organizationAttributeDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the organizationAttribute
        updatedOrganizationAttributeDTO.attributeName = UPDATED_ATTRIBUTE_NAME;
        updatedOrganizationAttributeDTO.attributeValue = UPDATED_ATTRIBUTE_VALUE;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedOrganizationAttributeDTO)
            .when()
            .put("/api/organization-attributes/" + organizationAttributeDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the OrganizationAttribute in the database
        var organizationAttributeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-attributes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationAttributeDTOList).hasSize(databaseSizeBeforeUpdate);
        var testOrganizationAttributeDTO = organizationAttributeDTOList
            .stream()
            .filter(it -> updatedOrganizationAttributeDTO.id.equals(it.id))
            .findFirst()
            .get();
        assertThat(testOrganizationAttributeDTO.attributeName).isEqualTo(UPDATED_ATTRIBUTE_NAME);
        assertThat(testOrganizationAttributeDTO.attributeValue).isEqualTo(UPDATED_ATTRIBUTE_VALUE);
    }

    @Test
    public void updateNonExistingOrganizationAttribute() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-attributes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationAttributeDTO)
            .when()
            .put("/api/organization-attributes/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the OrganizationAttribute in the database
        var organizationAttributeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-attributes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationAttributeDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteOrganizationAttribute() {
        // Initialize the database
        organizationAttributeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationAttributeDTO)
            .when()
            .post("/api/organization-attributes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-attributes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the organizationAttribute
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/organization-attributes/{id}", organizationAttributeDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var organizationAttributeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-attributes")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationAttributeDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllOrganizationAttributes() {
        // Initialize the database
        organizationAttributeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationAttributeDTO)
            .when()
            .post("/api/organization-attributes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the organizationAttributeList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-attributes?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(organizationAttributeDTO.id.intValue()))
            .body("attributeName", hasItem(DEFAULT_ATTRIBUTE_NAME))
            .body("attributeValue", hasItem(DEFAULT_ATTRIBUTE_VALUE));
    }

    @Test
    public void getOrganizationAttribute() {
        // Initialize the database
        organizationAttributeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationAttributeDTO)
            .when()
            .post("/api/organization-attributes")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the organizationAttribute
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/organization-attributes/{id}", organizationAttributeDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the organizationAttribute
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-attributes/{id}", organizationAttributeDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(organizationAttributeDTO.id.intValue()))
            .body("attributeName", is(DEFAULT_ATTRIBUTE_NAME))
            .body("attributeValue", is(DEFAULT_ATTRIBUTE_VALUE));
    }

    @Test
    public void getNonExistingOrganizationAttribute() {
        // Get the organizationAttribute
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-attributes/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
