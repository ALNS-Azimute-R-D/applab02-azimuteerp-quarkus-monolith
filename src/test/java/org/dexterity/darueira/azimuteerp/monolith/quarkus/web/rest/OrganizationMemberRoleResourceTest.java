package org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import liquibase.Liquibase;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrganizationMemberRoleDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class OrganizationMemberRoleResourceTest {

    private static final TypeRef<OrganizationMemberRoleDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<OrganizationMemberRoleDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final LocalDate DEFAULT_JOINED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_JOINED_AT = LocalDate.now(ZoneId.systemDefault());

    String adminToken;

    OrganizationMemberRoleDTO organizationMemberRoleDTO;

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
    public static OrganizationMemberRoleDTO createEntity() {
        var organizationMemberRoleDTO = new OrganizationMemberRoleDTO();
        organizationMemberRoleDTO.joinedAt = DEFAULT_JOINED_AT;
        return organizationMemberRoleDTO;
    }

    @BeforeEach
    public void initTest() {
        organizationMemberRoleDTO = createEntity();
    }

    @Test
    public void createOrganizationMemberRole() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-member-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the OrganizationMemberRole
        organizationMemberRoleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationMemberRoleDTO)
            .when()
            .post("/api/organization-member-roles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the OrganizationMemberRole in the database
        var organizationMemberRoleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-member-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationMemberRoleDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testOrganizationMemberRoleDTO = organizationMemberRoleDTOList
            .stream()
            .filter(it -> organizationMemberRoleDTO.id.equals(it.id))
            .findFirst()
            .get();
        assertThat(testOrganizationMemberRoleDTO.joinedAt).isEqualTo(DEFAULT_JOINED_AT);
    }

    @Test
    public void createOrganizationMemberRoleWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-member-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the OrganizationMemberRole with an existing ID
        organizationMemberRoleDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationMemberRoleDTO)
            .when()
            .post("/api/organization-member-roles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the OrganizationMemberRole in the database
        var organizationMemberRoleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-member-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationMemberRoleDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkJoinedAtIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-member-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        organizationMemberRoleDTO.joinedAt = null;

        // Create the OrganizationMemberRole, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationMemberRoleDTO)
            .when()
            .post("/api/organization-member-roles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the OrganizationMemberRole in the database
        var organizationMemberRoleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-member-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationMemberRoleDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateOrganizationMemberRole() {
        // Initialize the database
        organizationMemberRoleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationMemberRoleDTO)
            .when()
            .post("/api/organization-member-roles")
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
            .get("/api/organization-member-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the organizationMemberRole
        var updatedOrganizationMemberRoleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-member-roles/{id}", organizationMemberRoleDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the organizationMemberRole
        updatedOrganizationMemberRoleDTO.joinedAt = UPDATED_JOINED_AT;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedOrganizationMemberRoleDTO)
            .when()
            .put("/api/organization-member-roles/" + organizationMemberRoleDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the OrganizationMemberRole in the database
        var organizationMemberRoleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-member-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationMemberRoleDTOList).hasSize(databaseSizeBeforeUpdate);
        var testOrganizationMemberRoleDTO = organizationMemberRoleDTOList
            .stream()
            .filter(it -> updatedOrganizationMemberRoleDTO.id.equals(it.id))
            .findFirst()
            .get();
        assertThat(testOrganizationMemberRoleDTO.joinedAt).isEqualTo(UPDATED_JOINED_AT);
    }

    @Test
    public void updateNonExistingOrganizationMemberRole() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-member-roles")
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
            .body(organizationMemberRoleDTO)
            .when()
            .put("/api/organization-member-roles/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the OrganizationMemberRole in the database
        var organizationMemberRoleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-member-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationMemberRoleDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteOrganizationMemberRole() {
        // Initialize the database
        organizationMemberRoleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationMemberRoleDTO)
            .when()
            .post("/api/organization-member-roles")
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
            .get("/api/organization-member-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the organizationMemberRole
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/organization-member-roles/{id}", organizationMemberRoleDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var organizationMemberRoleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-member-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationMemberRoleDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllOrganizationMemberRoles() {
        // Initialize the database
        organizationMemberRoleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationMemberRoleDTO)
            .when()
            .post("/api/organization-member-roles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the organizationMemberRoleList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-member-roles?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(organizationMemberRoleDTO.id.intValue()))
            .body("joinedAt", hasItem(TestUtil.formatDateTime(DEFAULT_JOINED_AT)));
    }

    @Test
    public void getOrganizationMemberRole() {
        // Initialize the database
        organizationMemberRoleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationMemberRoleDTO)
            .when()
            .post("/api/organization-member-roles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the organizationMemberRole
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/organization-member-roles/{id}", organizationMemberRoleDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the organizationMemberRole
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-member-roles/{id}", organizationMemberRoleDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(organizationMemberRoleDTO.id.intValue()))
            .body("joinedAt", is(TestUtil.formatDateTime(DEFAULT_JOINED_AT)));
    }

    @Test
    public void getNonExistingOrganizationMemberRole() {
        // Get the organizationMemberRole
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-member-roles/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
