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
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.TenantDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class TenantResourceTest {

    private static final TypeRef<TenantDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<TenantDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_ACRONYM = "AAAAAAAAAA";
    private static final String UPDATED_ACRONYM = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_BUSINESS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_BUSINESS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_HANDLER_CLAZZ = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_HANDLER_CLAZZ = "BBBBBBBBBB";

    private static final String DEFAULT_MAIN_CONTACT_PERSON_DETAILS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_CONTACT_PERSON_DETAILS_JSON = "BBBBBBBBBB";

    private static final String DEFAULT_BILLING_DETAILS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_BILLING_DETAILS_JSON = "BBBBBBBBBB";

    private static final String DEFAULT_TECHNICAL_ENVIRONMENTS_DETAILS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_TECHNICAL_ENVIRONMENTS_DETAILS_JSON = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO_IMG = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO_IMG = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_IMG_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_IMG_CONTENT_TYPE = "image/png";

    private static final ActivationStatusEnum DEFAULT_ACTIVATION_STATUS = ActivationStatusEnum.INACTIVE;
    private static final ActivationStatusEnum UPDATED_ACTIVATION_STATUS = ActivationStatusEnum.ACTIVE;

    String adminToken;

    TenantDTO tenantDTO;

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
    public static TenantDTO createEntity() {
        var tenantDTO = new TenantDTO();
        tenantDTO.acronym = DEFAULT_ACRONYM;
        tenantDTO.name = DEFAULT_NAME;
        tenantDTO.description = DEFAULT_DESCRIPTION;
        tenantDTO.customerBusinessCode = DEFAULT_CUSTOMER_BUSINESS_CODE;
        tenantDTO.businessHandlerClazz = DEFAULT_BUSINESS_HANDLER_CLAZZ;
        tenantDTO.mainContactPersonDetailsJSON = DEFAULT_MAIN_CONTACT_PERSON_DETAILS_JSON;
        tenantDTO.billingDetailsJSON = DEFAULT_BILLING_DETAILS_JSON;
        tenantDTO.technicalEnvironmentsDetailsJSON = DEFAULT_TECHNICAL_ENVIRONMENTS_DETAILS_JSON;
        tenantDTO.customAttributesDetailsJSON = DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON;
        tenantDTO.logoImg = DEFAULT_LOGO_IMG;
        tenantDTO.activationStatus = DEFAULT_ACTIVATION_STATUS;
        return tenantDTO;
    }

    @BeforeEach
    public void initTest() {
        tenantDTO = createEntity();
    }

    @Test
    public void createTenant() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Tenant
        tenantDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(tenantDTO)
            .when()
            .post("/api/tenants")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Tenant in the database
        var tenantDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tenantDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testTenantDTO = tenantDTOList.stream().filter(it -> tenantDTO.id.equals(it.id)).findFirst().get();
        assertThat(testTenantDTO.acronym).isEqualTo(DEFAULT_ACRONYM);
        assertThat(testTenantDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testTenantDTO.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTenantDTO.customerBusinessCode).isEqualTo(DEFAULT_CUSTOMER_BUSINESS_CODE);
        assertThat(testTenantDTO.businessHandlerClazz).isEqualTo(DEFAULT_BUSINESS_HANDLER_CLAZZ);
        assertThat(testTenantDTO.mainContactPersonDetailsJSON).isEqualTo(DEFAULT_MAIN_CONTACT_PERSON_DETAILS_JSON);
        assertThat(testTenantDTO.billingDetailsJSON).isEqualTo(DEFAULT_BILLING_DETAILS_JSON);
        assertThat(testTenantDTO.technicalEnvironmentsDetailsJSON).isEqualTo(DEFAULT_TECHNICAL_ENVIRONMENTS_DETAILS_JSON);
        assertThat(testTenantDTO.customAttributesDetailsJSON).isEqualTo(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON);
        assertThat(testTenantDTO.logoImg).isEqualTo(DEFAULT_LOGO_IMG);
        assertThat(testTenantDTO.activationStatus).isEqualTo(DEFAULT_ACTIVATION_STATUS);
    }

    @Test
    public void createTenantWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Tenant with an existing ID
        tenantDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(tenantDTO)
            .when()
            .post("/api/tenants")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Tenant in the database
        var tenantDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tenantDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkAcronymIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        tenantDTO.acronym = null;

        // Create the Tenant, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(tenantDTO)
            .when()
            .post("/api/tenants")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Tenant in the database
        var tenantDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tenantDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        tenantDTO.name = null;

        // Create the Tenant, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(tenantDTO)
            .when()
            .post("/api/tenants")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Tenant in the database
        var tenantDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tenantDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        tenantDTO.description = null;

        // Create the Tenant, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(tenantDTO)
            .when()
            .post("/api/tenants")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Tenant in the database
        var tenantDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tenantDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCustomerBusinessCodeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        tenantDTO.customerBusinessCode = null;

        // Create the Tenant, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(tenantDTO)
            .when()
            .post("/api/tenants")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Tenant in the database
        var tenantDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tenantDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkActivationStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        tenantDTO.activationStatus = null;

        // Create the Tenant, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(tenantDTO)
            .when()
            .post("/api/tenants")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Tenant in the database
        var tenantDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tenantDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateTenant() {
        // Initialize the database
        tenantDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(tenantDTO)
            .when()
            .post("/api/tenants")
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
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the tenant
        var updatedTenantDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants/{id}", tenantDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the tenant
        updatedTenantDTO.acronym = UPDATED_ACRONYM;
        updatedTenantDTO.name = UPDATED_NAME;
        updatedTenantDTO.description = UPDATED_DESCRIPTION;
        updatedTenantDTO.customerBusinessCode = UPDATED_CUSTOMER_BUSINESS_CODE;
        updatedTenantDTO.businessHandlerClazz = UPDATED_BUSINESS_HANDLER_CLAZZ;
        updatedTenantDTO.mainContactPersonDetailsJSON = UPDATED_MAIN_CONTACT_PERSON_DETAILS_JSON;
        updatedTenantDTO.billingDetailsJSON = UPDATED_BILLING_DETAILS_JSON;
        updatedTenantDTO.technicalEnvironmentsDetailsJSON = UPDATED_TECHNICAL_ENVIRONMENTS_DETAILS_JSON;
        updatedTenantDTO.customAttributesDetailsJSON = UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON;
        updatedTenantDTO.logoImg = UPDATED_LOGO_IMG;
        updatedTenantDTO.activationStatus = UPDATED_ACTIVATION_STATUS;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedTenantDTO)
            .when()
            .put("/api/tenants/" + tenantDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Tenant in the database
        var tenantDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tenantDTOList).hasSize(databaseSizeBeforeUpdate);
        var testTenantDTO = tenantDTOList.stream().filter(it -> updatedTenantDTO.id.equals(it.id)).findFirst().get();
        assertThat(testTenantDTO.acronym).isEqualTo(UPDATED_ACRONYM);
        assertThat(testTenantDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testTenantDTO.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTenantDTO.customerBusinessCode).isEqualTo(UPDATED_CUSTOMER_BUSINESS_CODE);
        assertThat(testTenantDTO.businessHandlerClazz).isEqualTo(UPDATED_BUSINESS_HANDLER_CLAZZ);
        assertThat(testTenantDTO.mainContactPersonDetailsJSON).isEqualTo(UPDATED_MAIN_CONTACT_PERSON_DETAILS_JSON);
        assertThat(testTenantDTO.billingDetailsJSON).isEqualTo(UPDATED_BILLING_DETAILS_JSON);
        assertThat(testTenantDTO.technicalEnvironmentsDetailsJSON).isEqualTo(UPDATED_TECHNICAL_ENVIRONMENTS_DETAILS_JSON);
        assertThat(testTenantDTO.customAttributesDetailsJSON).isEqualTo(UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON);
        assertThat(testTenantDTO.logoImg).isEqualTo(UPDATED_LOGO_IMG);
        assertThat(testTenantDTO.activationStatus).isEqualTo(UPDATED_ACTIVATION_STATUS);
    }

    @Test
    public void updateNonExistingTenant() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
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
            .body(tenantDTO)
            .when()
            .put("/api/tenants/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Tenant in the database
        var tenantDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tenantDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteTenant() {
        // Initialize the database
        tenantDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(tenantDTO)
            .when()
            .post("/api/tenants")
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
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the tenant
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/tenants/{id}", tenantDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var tenantDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(tenantDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllTenants() {
        // Initialize the database
        tenantDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(tenantDTO)
            .when()
            .post("/api/tenants")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the tenantList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(tenantDTO.id.intValue()))
            .body("acronym", hasItem(DEFAULT_ACRONYM))
            .body("name", hasItem(DEFAULT_NAME))
            .body("description", hasItem(DEFAULT_DESCRIPTION))
            .body("customerBusinessCode", hasItem(DEFAULT_CUSTOMER_BUSINESS_CODE))
            .body("businessHandlerClazz", hasItem(DEFAULT_BUSINESS_HANDLER_CLAZZ))
            .body("mainContactPersonDetailsJSON", hasItem(DEFAULT_MAIN_CONTACT_PERSON_DETAILS_JSON))
            .body("billingDetailsJSON", hasItem(DEFAULT_BILLING_DETAILS_JSON))
            .body("technicalEnvironmentsDetailsJSON", hasItem(DEFAULT_TECHNICAL_ENVIRONMENTS_DETAILS_JSON))
            .body("customAttributesDetailsJSON", hasItem(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON))
            .body("logoImg", hasItem(DEFAULT_LOGO_IMG.toString()))
            .body("activationStatus", hasItem(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getTenant() {
        // Initialize the database
        tenantDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(tenantDTO)
            .when()
            .post("/api/tenants")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the tenant
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/tenants/{id}", tenantDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the tenant
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants/{id}", tenantDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(tenantDTO.id.intValue()))
            .body("acronym", is(DEFAULT_ACRONYM))
            .body("name", is(DEFAULT_NAME))
            .body("description", is(DEFAULT_DESCRIPTION))
            .body("customerBusinessCode", is(DEFAULT_CUSTOMER_BUSINESS_CODE))
            .body("businessHandlerClazz", is(DEFAULT_BUSINESS_HANDLER_CLAZZ))
            .body("mainContactPersonDetailsJSON", is(DEFAULT_MAIN_CONTACT_PERSON_DETAILS_JSON))
            .body("billingDetailsJSON", is(DEFAULT_BILLING_DETAILS_JSON))
            .body("technicalEnvironmentsDetailsJSON", is(DEFAULT_TECHNICAL_ENVIRONMENTS_DETAILS_JSON))
            .body("customAttributesDetailsJSON", is(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON))
            .body("logoImg", is(DEFAULT_LOGO_IMG.toString()))
            .body("activationStatus", is(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getNonExistingTenant() {
        // Get the tenant
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/tenants/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
