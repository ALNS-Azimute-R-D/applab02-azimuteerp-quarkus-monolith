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
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.OrganizationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrganizationDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class OrganizationResourceTest {

    private static final TypeRef<OrganizationDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<OrganizationDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_ACRONYM = "AAAAAAAAAA";
    private static final String UPDATED_ACRONYM = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_HIERARCHICAL_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_HIERARCHICAL_LEVEL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_HANDLER_CLAZZ = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_HANDLER_CLAZZ = "BBBBBBBBBB";

    private static final String DEFAULT_MAIN_CONTACT_PERSON_DETAILS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_CONTACT_PERSON_DETAILS_JSON = "BBBBBBBBBB";

    private static final String DEFAULT_TECHNICAL_ENVIRONMENTS_DETAILS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_TECHNICAL_ENVIRONMENTS_DETAILS_JSON = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON = "BBBBBBBBBB";

    private static final OrganizationStatusEnum DEFAULT_ORGANIZATION_STATUS = OrganizationStatusEnum.UNDER_EVALUATION;
    private static final OrganizationStatusEnum UPDATED_ORGANIZATION_STATUS = OrganizationStatusEnum.ONBOARDING;

    private static final ActivationStatusEnum DEFAULT_ACTIVATION_STATUS = ActivationStatusEnum.INACTIVE;
    private static final ActivationStatusEnum UPDATED_ACTIVATION_STATUS = ActivationStatusEnum.ACTIVE;

    private static final byte[] DEFAULT_LOGO_IMG = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO_IMG = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_IMG_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_IMG_CONTENT_TYPE = "image/png";

    String adminToken;

    OrganizationDTO organizationDTO;

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
    public static OrganizationDTO createEntity() {
        var organizationDTO = new OrganizationDTO();
        organizationDTO.acronym = DEFAULT_ACRONYM;
        organizationDTO.businessCode = DEFAULT_BUSINESS_CODE;
        organizationDTO.hierarchicalLevel = DEFAULT_HIERARCHICAL_LEVEL;
        organizationDTO.name = DEFAULT_NAME;
        organizationDTO.description = DEFAULT_DESCRIPTION;
        organizationDTO.businessHandlerClazz = DEFAULT_BUSINESS_HANDLER_CLAZZ;
        organizationDTO.mainContactPersonDetailsJSON = DEFAULT_MAIN_CONTACT_PERSON_DETAILS_JSON;
        organizationDTO.technicalEnvironmentsDetailsJSON = DEFAULT_TECHNICAL_ENVIRONMENTS_DETAILS_JSON;
        organizationDTO.customAttributesDetailsJSON = DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON;
        organizationDTO.organizationStatus = DEFAULT_ORGANIZATION_STATUS;
        organizationDTO.activationStatus = DEFAULT_ACTIVATION_STATUS;
        organizationDTO.logoImg = DEFAULT_LOGO_IMG;
        return organizationDTO;
    }

    @BeforeEach
    public void initTest() {
        organizationDTO = createEntity();
    }

    @Test
    public void createOrganization() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Organization
        organizationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationDTO)
            .when()
            .post("/api/organizations")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Organization in the database
        var organizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testOrganizationDTO = organizationDTOList.stream().filter(it -> organizationDTO.id.equals(it.id)).findFirst().get();
        assertThat(testOrganizationDTO.acronym).isEqualTo(DEFAULT_ACRONYM);
        assertThat(testOrganizationDTO.businessCode).isEqualTo(DEFAULT_BUSINESS_CODE);
        assertThat(testOrganizationDTO.hierarchicalLevel).isEqualTo(DEFAULT_HIERARCHICAL_LEVEL);
        assertThat(testOrganizationDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganizationDTO.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOrganizationDTO.businessHandlerClazz).isEqualTo(DEFAULT_BUSINESS_HANDLER_CLAZZ);
        assertThat(testOrganizationDTO.mainContactPersonDetailsJSON).isEqualTo(DEFAULT_MAIN_CONTACT_PERSON_DETAILS_JSON);
        assertThat(testOrganizationDTO.technicalEnvironmentsDetailsJSON).isEqualTo(DEFAULT_TECHNICAL_ENVIRONMENTS_DETAILS_JSON);
        assertThat(testOrganizationDTO.customAttributesDetailsJSON).isEqualTo(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON);
        assertThat(testOrganizationDTO.organizationStatus).isEqualTo(DEFAULT_ORGANIZATION_STATUS);
        assertThat(testOrganizationDTO.activationStatus).isEqualTo(DEFAULT_ACTIVATION_STATUS);
        assertThat(testOrganizationDTO.logoImg).isEqualTo(DEFAULT_LOGO_IMG);
    }

    @Test
    public void createOrganizationWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Organization with an existing ID
        organizationDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationDTO)
            .when()
            .post("/api/organizations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Organization in the database
        var organizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkAcronymIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        organizationDTO.acronym = null;

        // Create the Organization, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationDTO)
            .when()
            .post("/api/organizations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Organization in the database
        var organizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkBusinessCodeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        organizationDTO.businessCode = null;

        // Create the Organization, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationDTO)
            .when()
            .post("/api/organizations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Organization in the database
        var organizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        organizationDTO.name = null;

        // Create the Organization, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationDTO)
            .when()
            .post("/api/organizations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Organization in the database
        var organizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        organizationDTO.description = null;

        // Create the Organization, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationDTO)
            .when()
            .post("/api/organizations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Organization in the database
        var organizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkOrganizationStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        organizationDTO.organizationStatus = null;

        // Create the Organization, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationDTO)
            .when()
            .post("/api/organizations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Organization in the database
        var organizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkActivationStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        organizationDTO.activationStatus = null;

        // Create the Organization, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationDTO)
            .when()
            .post("/api/organizations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Organization in the database
        var organizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateOrganization() {
        // Initialize the database
        organizationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationDTO)
            .when()
            .post("/api/organizations")
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
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the organization
        var updatedOrganizationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations/{id}", organizationDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the organization
        updatedOrganizationDTO.acronym = UPDATED_ACRONYM;
        updatedOrganizationDTO.businessCode = UPDATED_BUSINESS_CODE;
        updatedOrganizationDTO.hierarchicalLevel = UPDATED_HIERARCHICAL_LEVEL;
        updatedOrganizationDTO.name = UPDATED_NAME;
        updatedOrganizationDTO.description = UPDATED_DESCRIPTION;
        updatedOrganizationDTO.businessHandlerClazz = UPDATED_BUSINESS_HANDLER_CLAZZ;
        updatedOrganizationDTO.mainContactPersonDetailsJSON = UPDATED_MAIN_CONTACT_PERSON_DETAILS_JSON;
        updatedOrganizationDTO.technicalEnvironmentsDetailsJSON = UPDATED_TECHNICAL_ENVIRONMENTS_DETAILS_JSON;
        updatedOrganizationDTO.customAttributesDetailsJSON = UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON;
        updatedOrganizationDTO.organizationStatus = UPDATED_ORGANIZATION_STATUS;
        updatedOrganizationDTO.activationStatus = UPDATED_ACTIVATION_STATUS;
        updatedOrganizationDTO.logoImg = UPDATED_LOGO_IMG;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedOrganizationDTO)
            .when()
            .put("/api/organizations/" + organizationDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Organization in the database
        var organizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationDTOList).hasSize(databaseSizeBeforeUpdate);
        var testOrganizationDTO = organizationDTOList.stream().filter(it -> updatedOrganizationDTO.id.equals(it.id)).findFirst().get();
        assertThat(testOrganizationDTO.acronym).isEqualTo(UPDATED_ACRONYM);
        assertThat(testOrganizationDTO.businessCode).isEqualTo(UPDATED_BUSINESS_CODE);
        assertThat(testOrganizationDTO.hierarchicalLevel).isEqualTo(UPDATED_HIERARCHICAL_LEVEL);
        assertThat(testOrganizationDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testOrganizationDTO.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOrganizationDTO.businessHandlerClazz).isEqualTo(UPDATED_BUSINESS_HANDLER_CLAZZ);
        assertThat(testOrganizationDTO.mainContactPersonDetailsJSON).isEqualTo(UPDATED_MAIN_CONTACT_PERSON_DETAILS_JSON);
        assertThat(testOrganizationDTO.technicalEnvironmentsDetailsJSON).isEqualTo(UPDATED_TECHNICAL_ENVIRONMENTS_DETAILS_JSON);
        assertThat(testOrganizationDTO.customAttributesDetailsJSON).isEqualTo(UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON);
        assertThat(testOrganizationDTO.organizationStatus).isEqualTo(UPDATED_ORGANIZATION_STATUS);
        assertThat(testOrganizationDTO.activationStatus).isEqualTo(UPDATED_ACTIVATION_STATUS);
        assertThat(testOrganizationDTO.logoImg).isEqualTo(UPDATED_LOGO_IMG);
    }

    @Test
    public void updateNonExistingOrganization() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
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
            .body(organizationDTO)
            .when()
            .put("/api/organizations/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Organization in the database
        var organizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteOrganization() {
        // Initialize the database
        organizationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationDTO)
            .when()
            .post("/api/organizations")
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
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the organization
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/organizations/{id}", organizationDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var organizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(organizationDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllOrganizations() {
        // Initialize the database
        organizationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationDTO)
            .when()
            .post("/api/organizations")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the organizationList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(organizationDTO.id.intValue()))
            .body("acronym", hasItem(DEFAULT_ACRONYM))
            .body("businessCode", hasItem(DEFAULT_BUSINESS_CODE))
            .body("hierarchicalLevel", hasItem(DEFAULT_HIERARCHICAL_LEVEL))
            .body("name", hasItem(DEFAULT_NAME))
            .body("description", hasItem(DEFAULT_DESCRIPTION))
            .body("businessHandlerClazz", hasItem(DEFAULT_BUSINESS_HANDLER_CLAZZ))
            .body("mainContactPersonDetailsJSON", hasItem(DEFAULT_MAIN_CONTACT_PERSON_DETAILS_JSON))
            .body("technicalEnvironmentsDetailsJSON", hasItem(DEFAULT_TECHNICAL_ENVIRONMENTS_DETAILS_JSON))
            .body("customAttributesDetailsJSON", hasItem(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON))
            .body("organizationStatus", hasItem(DEFAULT_ORGANIZATION_STATUS.toString()))
            .body("activationStatus", hasItem(DEFAULT_ACTIVATION_STATUS.toString()))
            .body("logoImg", hasItem(DEFAULT_LOGO_IMG.toString()));
    }

    @Test
    public void getOrganization() {
        // Initialize the database
        organizationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organizationDTO)
            .when()
            .post("/api/organizations")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the organization
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/organizations/{id}", organizationDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the organization
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations/{id}", organizationDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(organizationDTO.id.intValue()))
            .body("acronym", is(DEFAULT_ACRONYM))
            .body("businessCode", is(DEFAULT_BUSINESS_CODE))
            .body("hierarchicalLevel", is(DEFAULT_HIERARCHICAL_LEVEL))
            .body("name", is(DEFAULT_NAME))
            .body("description", is(DEFAULT_DESCRIPTION))
            .body("businessHandlerClazz", is(DEFAULT_BUSINESS_HANDLER_CLAZZ))
            .body("mainContactPersonDetailsJSON", is(DEFAULT_MAIN_CONTACT_PERSON_DETAILS_JSON))
            .body("technicalEnvironmentsDetailsJSON", is(DEFAULT_TECHNICAL_ENVIRONMENTS_DETAILS_JSON))
            .body("customAttributesDetailsJSON", is(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON))
            .body("organizationStatus", is(DEFAULT_ORGANIZATION_STATUS.toString()))
            .body("activationStatus", is(DEFAULT_ACTIVATION_STATUS.toString()))
            .body("logoImg", is(DEFAULT_LOGO_IMG.toString()));
    }

    @Test
    public void getNonExistingOrganization() {
        // Get the organization
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organizations/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
