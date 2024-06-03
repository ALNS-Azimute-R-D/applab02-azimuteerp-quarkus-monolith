package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import de.org.dexterity.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.PaymentMethodDTO;
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
public class PaymentMethodResourceTest {

    private static final TypeRef<PaymentMethodDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<PaymentMethodDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_HANDLER_CLAZZ = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_HANDLER_CLAZZ = "BBBBBBBBBB";

    String adminToken;

    PaymentMethodDTO paymentMethodDTO;

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
    public static PaymentMethodDTO createEntity() {
        var paymentMethodDTO = new PaymentMethodDTO();
        paymentMethodDTO.code = DEFAULT_CODE;
        paymentMethodDTO.description = DEFAULT_DESCRIPTION;
        paymentMethodDTO.businessHandlerClazz = DEFAULT_BUSINESS_HANDLER_CLAZZ;
        return paymentMethodDTO;
    }

    @BeforeEach
    public void initTest() {
        paymentMethodDTO = createEntity();
    }

    @Test
    public void createPaymentMethod() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the PaymentMethod
        paymentMethodDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentMethodDTO)
            .when()
            .post("/api/payment-methods")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the PaymentMethod in the database
        var paymentMethodDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentMethodDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testPaymentMethodDTO = paymentMethodDTOList.stream().filter(it -> paymentMethodDTO.id.equals(it.id)).findFirst().get();
        assertThat(testPaymentMethodDTO.code).isEqualTo(DEFAULT_CODE);
        assertThat(testPaymentMethodDTO.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPaymentMethodDTO.businessHandlerClazz).isEqualTo(DEFAULT_BUSINESS_HANDLER_CLAZZ);
    }

    @Test
    public void createPaymentMethodWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the PaymentMethod with an existing ID
        paymentMethodDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentMethodDTO)
            .when()
            .post("/api/payment-methods")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the PaymentMethod in the database
        var paymentMethodDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentMethodDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkCodeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        paymentMethodDTO.code = null;

        // Create the PaymentMethod, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentMethodDTO)
            .when()
            .post("/api/payment-methods")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the PaymentMethod in the database
        var paymentMethodDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentMethodDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        paymentMethodDTO.description = null;

        // Create the PaymentMethod, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentMethodDTO)
            .when()
            .post("/api/payment-methods")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the PaymentMethod in the database
        var paymentMethodDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentMethodDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updatePaymentMethod() {
        // Initialize the database
        paymentMethodDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentMethodDTO)
            .when()
            .post("/api/payment-methods")
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
            .get("/api/payment-methods")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the paymentMethod
        var updatedPaymentMethodDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods/{id}", paymentMethodDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the paymentMethod
        updatedPaymentMethodDTO.code = UPDATED_CODE;
        updatedPaymentMethodDTO.description = UPDATED_DESCRIPTION;
        updatedPaymentMethodDTO.businessHandlerClazz = UPDATED_BUSINESS_HANDLER_CLAZZ;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedPaymentMethodDTO)
            .when()
            .put("/api/payment-methods/" + paymentMethodDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the PaymentMethod in the database
        var paymentMethodDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentMethodDTOList).hasSize(databaseSizeBeforeUpdate);
        var testPaymentMethodDTO = paymentMethodDTOList.stream().filter(it -> updatedPaymentMethodDTO.id.equals(it.id)).findFirst().get();
        assertThat(testPaymentMethodDTO.code).isEqualTo(UPDATED_CODE);
        assertThat(testPaymentMethodDTO.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPaymentMethodDTO.businessHandlerClazz).isEqualTo(UPDATED_BUSINESS_HANDLER_CLAZZ);
    }

    @Test
    public void updateNonExistingPaymentMethod() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods")
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
            .body(paymentMethodDTO)
            .when()
            .put("/api/payment-methods/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the PaymentMethod in the database
        var paymentMethodDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentMethodDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deletePaymentMethod() {
        // Initialize the database
        paymentMethodDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentMethodDTO)
            .when()
            .post("/api/payment-methods")
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
            .get("/api/payment-methods")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the paymentMethod
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/payment-methods/{id}", paymentMethodDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var paymentMethodDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentMethodDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllPaymentMethods() {
        // Initialize the database
        paymentMethodDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentMethodDTO)
            .when()
            .post("/api/payment-methods")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the paymentMethodList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(paymentMethodDTO.id.intValue()))
            .body("code", hasItem(DEFAULT_CODE))
            .body("description", hasItem(DEFAULT_DESCRIPTION))
            .body("businessHandlerClazz", hasItem(DEFAULT_BUSINESS_HANDLER_CLAZZ));
    }

    @Test
    public void getPaymentMethod() {
        // Initialize the database
        paymentMethodDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentMethodDTO)
            .when()
            .post("/api/payment-methods")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the paymentMethod
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/payment-methods/{id}", paymentMethodDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the paymentMethod
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods/{id}", paymentMethodDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(paymentMethodDTO.id.intValue()))
            .body("code", is(DEFAULT_CODE))
            .body("description", is(DEFAULT_DESCRIPTION))
            .body("businessHandlerClazz", is(DEFAULT_BUSINESS_HANDLER_CLAZZ));
    }

    @Test
    public void getNonExistingPaymentMethod() {
        // Get the paymentMethod
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payment-methods/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
