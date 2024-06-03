package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import de.org.dexterity.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.CustomerTypeDTO;
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
public class CustomerTypeResourceTest {

    private static final TypeRef<CustomerTypeDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<CustomerTypeDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_HANDLER_CLAZZ = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_HANDLER_CLAZZ = "BBBBBBBBBB";

    String adminToken;

    CustomerTypeDTO customerTypeDTO;

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
    public static CustomerTypeDTO createEntity() {
        var customerTypeDTO = new CustomerTypeDTO();
        customerTypeDTO.name = DEFAULT_NAME;
        customerTypeDTO.description = DEFAULT_DESCRIPTION;
        customerTypeDTO.businessHandlerClazz = DEFAULT_BUSINESS_HANDLER_CLAZZ;
        return customerTypeDTO;
    }

    @BeforeEach
    public void initTest() {
        customerTypeDTO = createEntity();
    }

    @Test
    public void createCustomerType() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customer-types")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the CustomerType
        customerTypeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerTypeDTO)
            .when()
            .post("/api/customer-types")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the CustomerType in the database
        var customerTypeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customer-types")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerTypeDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testCustomerTypeDTO = customerTypeDTOList.stream().filter(it -> customerTypeDTO.id.equals(it.id)).findFirst().get();
        assertThat(testCustomerTypeDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomerTypeDTO.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCustomerTypeDTO.businessHandlerClazz).isEqualTo(DEFAULT_BUSINESS_HANDLER_CLAZZ);
    }

    @Test
    public void createCustomerTypeWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customer-types")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the CustomerType with an existing ID
        customerTypeDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerTypeDTO)
            .when()
            .post("/api/customer-types")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the CustomerType in the database
        var customerTypeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customer-types")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerTypeDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customer-types")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        customerTypeDTO.name = null;

        // Create the CustomerType, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerTypeDTO)
            .when()
            .post("/api/customer-types")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the CustomerType in the database
        var customerTypeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customer-types")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerTypeDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateCustomerType() {
        // Initialize the database
        customerTypeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerTypeDTO)
            .when()
            .post("/api/customer-types")
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
            .get("/api/customer-types")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the customerType
        var updatedCustomerTypeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customer-types/{id}", customerTypeDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the customerType
        updatedCustomerTypeDTO.name = UPDATED_NAME;
        updatedCustomerTypeDTO.description = UPDATED_DESCRIPTION;
        updatedCustomerTypeDTO.businessHandlerClazz = UPDATED_BUSINESS_HANDLER_CLAZZ;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedCustomerTypeDTO)
            .when()
            .put("/api/customer-types/" + customerTypeDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the CustomerType in the database
        var customerTypeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customer-types")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerTypeDTOList).hasSize(databaseSizeBeforeUpdate);
        var testCustomerTypeDTO = customerTypeDTOList.stream().filter(it -> updatedCustomerTypeDTO.id.equals(it.id)).findFirst().get();
        assertThat(testCustomerTypeDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testCustomerTypeDTO.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCustomerTypeDTO.businessHandlerClazz).isEqualTo(UPDATED_BUSINESS_HANDLER_CLAZZ);
    }

    @Test
    public void updateNonExistingCustomerType() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customer-types")
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
            .body(customerTypeDTO)
            .when()
            .put("/api/customer-types/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the CustomerType in the database
        var customerTypeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customer-types")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerTypeDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCustomerType() {
        // Initialize the database
        customerTypeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerTypeDTO)
            .when()
            .post("/api/customer-types")
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
            .get("/api/customer-types")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the customerType
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/customer-types/{id}", customerTypeDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var customerTypeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customer-types")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(customerTypeDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllCustomerTypes() {
        // Initialize the database
        customerTypeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerTypeDTO)
            .when()
            .post("/api/customer-types")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the customerTypeList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customer-types?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(customerTypeDTO.id.intValue()))
            .body("name", hasItem(DEFAULT_NAME))
            .body("description", hasItem(DEFAULT_DESCRIPTION))
            .body("businessHandlerClazz", hasItem(DEFAULT_BUSINESS_HANDLER_CLAZZ));
    }

    @Test
    public void getCustomerType() {
        // Initialize the database
        customerTypeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(customerTypeDTO)
            .when()
            .post("/api/customer-types")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the customerType
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/customer-types/{id}", customerTypeDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the customerType
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customer-types/{id}", customerTypeDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(customerTypeDTO.id.intValue()))
            .body("name", is(DEFAULT_NAME))
            .body("description", is(DEFAULT_DESCRIPTION))
            .body("businessHandlerClazz", is(DEFAULT_BUSINESS_HANDLER_CLAZZ));
    }

    @Test
    public void getNonExistingCustomerType() {
        // Get the customerType
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/customer-types/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
