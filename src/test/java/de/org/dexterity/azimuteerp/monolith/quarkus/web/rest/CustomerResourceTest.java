package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.CustomerStatusEnum;
import de.org.dexterity.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.CustomerDTO;
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
public class CustomerResourceTest {

    private static final TypeRef<CustomerDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<CustomerDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_CUSTOMER_BUSINESS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_BUSINESS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBB";

    private static final String DEFAULT_KEYCLOAK_GROUP_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_KEYCLOAK_GROUP_DETAILS = "BBBBBBBBBB";

    private static final CustomerStatusEnum DEFAULT_STATUS = CustomerStatusEnum.UNDER_EVALUATION;
    private static final CustomerStatusEnum UPDATED_STATUS = CustomerStatusEnum.ONBOARDING;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    String adminToken;

    CustomerDTO customerDTO;

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
    public static CustomerDTO createEntity() {
        var customerDTO = new CustomerDTO();
        customerDTO.customerBusinessCode = DEFAULT_CUSTOMER_BUSINESS_CODE;
        customerDTO.name = DEFAULT_NAME;
        customerDTO.description = DEFAULT_DESCRIPTION;
        customerDTO.email = DEFAULT_EMAIL;
        customerDTO.addressDetails = DEFAULT_ADDRESS_DETAILS;
        customerDTO.zipCode = DEFAULT_ZIP_CODE;
        customerDTO.keycloakGroupDetails = DEFAULT_KEYCLOAK_GROUP_DETAILS;
        customerDTO.status = DEFAULT_STATUS;
        customerDTO.active = DEFAULT_ACTIVE;
        return customerDTO;
    }

    @BeforeEach
    public void initTest() {
        customerDTO = createEntity();
    }

    @Test
    public void createCustomer() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Customer
        customerDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerDTO)
            .when()
            .post("/api/customers")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Customer in the database
        var customerDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testCustomerDTO = customerDTOList.stream().filter(it -> customerDTO.id.equals(it.id)).findFirst().get();
        assertThat(testCustomerDTO.customerBusinessCode).isEqualTo(DEFAULT_CUSTOMER_BUSINESS_CODE);
        assertThat(testCustomerDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomerDTO.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCustomerDTO.email).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomerDTO.addressDetails).isEqualTo(DEFAULT_ADDRESS_DETAILS);
        assertThat(testCustomerDTO.zipCode).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testCustomerDTO.keycloakGroupDetails).isEqualTo(DEFAULT_KEYCLOAK_GROUP_DETAILS);
        assertThat(testCustomerDTO.status).isEqualTo(DEFAULT_STATUS);
        assertThat(testCustomerDTO.active).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    public void createCustomerWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Customer with an existing ID
        customerDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerDTO)
            .when()
            .post("/api/customers")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Customer in the database
        var customerDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkCustomerBusinessCodeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        customerDTO.customerBusinessCode = null;

        // Create the Customer, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerDTO)
            .when()
            .post("/api/customers")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Customer in the database
        var customerDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        customerDTO.name = null;

        // Create the Customer, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerDTO)
            .when()
            .post("/api/customers")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Customer in the database
        var customerDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkEmailIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        customerDTO.email = null;

        // Create the Customer, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerDTO)
            .when()
            .post("/api/customers")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Customer in the database
        var customerDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        customerDTO.status = null;

        // Create the Customer, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerDTO)
            .when()
            .post("/api/customers")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Customer in the database
        var customerDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkActiveIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        customerDTO.active = null;

        // Create the Customer, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerDTO)
            .when()
            .post("/api/customers")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Customer in the database
        var customerDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateCustomer() {
        // Initialize the database
        customerDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerDTO)
            .when()
            .post("/api/customers")
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
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the customer
        var updatedCustomerDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers/{id}", customerDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the customer
        updatedCustomerDTO.customerBusinessCode = UPDATED_CUSTOMER_BUSINESS_CODE;
        updatedCustomerDTO.name = UPDATED_NAME;
        updatedCustomerDTO.description = UPDATED_DESCRIPTION;
        updatedCustomerDTO.email = UPDATED_EMAIL;
        updatedCustomerDTO.addressDetails = UPDATED_ADDRESS_DETAILS;
        updatedCustomerDTO.zipCode = UPDATED_ZIP_CODE;
        updatedCustomerDTO.keycloakGroupDetails = UPDATED_KEYCLOAK_GROUP_DETAILS;
        updatedCustomerDTO.status = UPDATED_STATUS;
        updatedCustomerDTO.active = UPDATED_ACTIVE;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedCustomerDTO)
            .when()
            .put("/api/customers/" + customerDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Customer in the database
        var customerDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerDTOList).hasSize(databaseSizeBeforeUpdate);
        var testCustomerDTO = customerDTOList.stream().filter(it -> updatedCustomerDTO.id.equals(it.id)).findFirst().get();
        assertThat(testCustomerDTO.customerBusinessCode).isEqualTo(UPDATED_CUSTOMER_BUSINESS_CODE);
        assertThat(testCustomerDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testCustomerDTO.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCustomerDTO.email).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomerDTO.addressDetails).isEqualTo(UPDATED_ADDRESS_DETAILS);
        assertThat(testCustomerDTO.zipCode).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testCustomerDTO.keycloakGroupDetails).isEqualTo(UPDATED_KEYCLOAK_GROUP_DETAILS);
        assertThat(testCustomerDTO.status).isEqualTo(UPDATED_STATUS);
        assertThat(testCustomerDTO.active).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    public void updateNonExistingCustomer() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
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
            .body(customerDTO)
            .when()
            .put("/api/customers/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Customer in the database
        var customerDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCustomer() {
        // Initialize the database
        customerDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerDTO)
            .when()
            .post("/api/customers")
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
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the customer
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/customers/{id}", customerDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var customerDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllCustomers() {
        // Initialize the database
        customerDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerDTO)
            .when()
            .post("/api/customers")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the customerList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(customerDTO.id.intValue()))
            .body("customerBusinessCode", hasItem(DEFAULT_CUSTOMER_BUSINESS_CODE))
            .body("name", hasItem(DEFAULT_NAME))
            .body("description", hasItem(DEFAULT_DESCRIPTION.toString()))
            .body("email", hasItem(DEFAULT_EMAIL))
            .body("addressDetails", hasItem(DEFAULT_ADDRESS_DETAILS))
            .body("zipCode", hasItem(DEFAULT_ZIP_CODE))
            .body("keycloakGroupDetails", hasItem(DEFAULT_KEYCLOAK_GROUP_DETAILS))
            .body("status", hasItem(DEFAULT_STATUS.toString()))
            .body("active", hasItem(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    public void getCustomer() {
        // Initialize the database
        customerDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerDTO)
            .when()
            .post("/api/customers")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the customer
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/customers/{id}", customerDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the customer
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers/{id}", customerDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(customerDTO.id.intValue()))
            .body("customerBusinessCode", is(DEFAULT_CUSTOMER_BUSINESS_CODE))
            .body("name", is(DEFAULT_NAME))
            .body("description", is(DEFAULT_DESCRIPTION.toString()))
            .body("email", is(DEFAULT_EMAIL))
            .body("addressDetails", is(DEFAULT_ADDRESS_DETAILS))
            .body("zipCode", is(DEFAULT_ZIP_CODE))
            .body("keycloakGroupDetails", is(DEFAULT_KEYCLOAK_GROUP_DETAILS))
            .body("status", is(DEFAULT_STATUS.toString()))
            .body("active", is(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    public void getNonExistingCustomer() {
        // Get the customer
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customers/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
