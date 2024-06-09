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
import java.util.List;
import liquibase.Liquibase;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrganizationRoleDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class OrganizationRoleResourceTest {

    private static final TypeRef<OrganizationRoleDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<OrganizationRoleDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_ROLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROLE_NAME = "BBBBBBBBBB";

    private static final ActivationStatusEnum DEFAULT_ACTIVATION_STATUS = ActivationStatusEnum.INACTIVE;
    private static final ActivationStatusEnum UPDATED_ACTIVATION_STATUS = ActivationStatusEnum.ACTIVE;

    String adminToken;

    OrganizationRoleDTO organizationRoleDTO;

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
    public static OrganizationRoleDTO createEntity() {
        var organizationRoleDTO = new OrganizationRoleDTO();
        organizationRoleDTO.roleName = DEFAULT_ROLE_NAME;
        organizationRoleDTO.activationStatus = DEFAULT_ACTIVATION_STATUS;
        return organizationRoleDTO;
    }

    @BeforeEach
    public void initTest() {
        organizationRoleDTO = createEntity();
    }

    @Test
    public void createOrganizationRole() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the OrganizationRole
        organizationRoleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationRoleDTO)
            .when()
            .post("/api/organization-roles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the OrganizationRole in the database
        var organizationRoleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationRoleDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testOrganizationRoleDTO = organizationRoleDTOList.stream().filter(it -> organizationRoleDTO.id.equals(it.id)).findFirst().get();
        assertThat(testOrganizationRoleDTO.roleName).isEqualTo(DEFAULT_ROLE_NAME);
        assertThat(testOrganizationRoleDTO.activationStatus).isEqualTo(DEFAULT_ACTIVATION_STATUS);
    }

    @Test
    public void createOrganizationRoleWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the OrganizationRole with an existing ID
        organizationRoleDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationRoleDTO)
            .when()
            .post("/api/organization-roles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the OrganizationRole in the database
        var organizationRoleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationRoleDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkRoleNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        organizationRoleDTO.roleName = null;

        // Create the OrganizationRole, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationRoleDTO)
            .when()
            .post("/api/organization-roles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the OrganizationRole in the database
        var organizationRoleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationRoleDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkActivationStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        organizationRoleDTO.activationStatus = null;

        // Create the OrganizationRole, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationRoleDTO)
            .when()
            .post("/api/organization-roles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the OrganizationRole in the database
        var organizationRoleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationRoleDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateOrganizationRole() {
        // Initialize the database
        organizationRoleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationRoleDTO)
            .when()
            .post("/api/organization-roles")
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
            .get("/api/organization-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the organizationRole
        var updatedOrganizationRoleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles/{id}", organizationRoleDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the organizationRole
        updatedOrganizationRoleDTO.roleName = UPDATED_ROLE_NAME;
        updatedOrganizationRoleDTO.activationStatus = UPDATED_ACTIVATION_STATUS;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedOrganizationRoleDTO)
            .when()
            .put("/api/organization-roles/" + organizationRoleDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the OrganizationRole in the database
        var organizationRoleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationRoleDTOList).hasSize(databaseSizeBeforeUpdate);
        var testOrganizationRoleDTO = organizationRoleDTOList
            .stream()
            .filter(it -> updatedOrganizationRoleDTO.id.equals(it.id))
            .findFirst()
            .get();
        assertThat(testOrganizationRoleDTO.roleName).isEqualTo(UPDATED_ROLE_NAME);
        assertThat(testOrganizationRoleDTO.activationStatus).isEqualTo(UPDATED_ACTIVATION_STATUS);
    }

    @Test
    public void updateNonExistingOrganizationRole() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles")
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
            .body(organizationRoleDTO)
            .when()
            .put("/api/organization-roles/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the OrganizationRole in the database
        var organizationRoleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationRoleDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteOrganizationRole() {
        // Initialize the database
        organizationRoleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationRoleDTO)
            .when()
            .post("/api/organization-roles")
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
            .get("/api/organization-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the organizationRole
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/organization-roles/{id}", organizationRoleDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var organizationRoleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationRoleDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllOrganizationRoles() {
        // Initialize the database
        organizationRoleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationRoleDTO)
            .when()
            .post("/api/organization-roles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the organizationRoleList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(organizationRoleDTO.id.intValue()))
            .body("roleName", hasItem(DEFAULT_ROLE_NAME))
            .body("activationStatus", hasItem(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getOrganizationRole() {
        // Initialize the database
        organizationRoleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationRoleDTO)
            .when()
            .post("/api/organization-roles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the organizationRole
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/organization-roles/{id}", organizationRoleDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the organizationRole
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles/{id}", organizationRoleDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(organizationRoleDTO.id.intValue()))
            .body("roleName", is(DEFAULT_ROLE_NAME))
            .body("activationStatus", is(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getNonExistingOrganizationRole() {
        // Get the organizationRole
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organization-roles/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
