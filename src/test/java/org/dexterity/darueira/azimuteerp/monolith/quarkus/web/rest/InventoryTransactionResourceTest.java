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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import liquibase.Liquibase;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.InventoryTransactionDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class InventoryTransactionResourceTest {

    private static final TypeRef<InventoryTransactionDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<InventoryTransactionDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final Long DEFAULT_INVOICE_ID = 1L;
    private static final Long UPDATED_INVOICE_ID = 2L;

    private static final Instant DEFAULT_TRANSACTION_CREATED_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_TRANSACTION_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final Instant DEFAULT_TRANSACTION_MODIFIED_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_TRANSACTION_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String DEFAULT_TRANSACTION_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_COMMENTS = "BBBBBBBBBB";

    private static final ActivationStatusEnum DEFAULT_ACTIVATION_STATUS = ActivationStatusEnum.INACTIVE;
    private static final ActivationStatusEnum UPDATED_ACTIVATION_STATUS = ActivationStatusEnum.ACTIVE;

    String adminToken;

    InventoryTransactionDTO inventoryTransactionDTO;

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
    public static InventoryTransactionDTO createEntity() {
        var inventoryTransactionDTO = new InventoryTransactionDTO();
        inventoryTransactionDTO.invoiceId = DEFAULT_INVOICE_ID;
        inventoryTransactionDTO.transactionCreatedDate = DEFAULT_TRANSACTION_CREATED_DATE;
        inventoryTransactionDTO.transactionModifiedDate = DEFAULT_TRANSACTION_MODIFIED_DATE;
        inventoryTransactionDTO.quantity = DEFAULT_QUANTITY;
        inventoryTransactionDTO.transactionComments = DEFAULT_TRANSACTION_COMMENTS;
        inventoryTransactionDTO.activationStatus = DEFAULT_ACTIVATION_STATUS;
        return inventoryTransactionDTO;
    }

    @BeforeEach
    public void initTest() {
        inventoryTransactionDTO = createEntity();
    }

    @Test
    public void createInventoryTransaction() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the InventoryTransaction
        inventoryTransactionDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(inventoryTransactionDTO)
            .when()
            .post("/api/inventory-transactions")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the InventoryTransaction in the database
        var inventoryTransactionDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(inventoryTransactionDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testInventoryTransactionDTO = inventoryTransactionDTOList
            .stream()
            .filter(it -> inventoryTransactionDTO.id.equals(it.id))
            .findFirst()
            .get();
        assertThat(testInventoryTransactionDTO.invoiceId).isEqualTo(DEFAULT_INVOICE_ID);
        assertThat(testInventoryTransactionDTO.transactionCreatedDate).isEqualTo(DEFAULT_TRANSACTION_CREATED_DATE);
        assertThat(testInventoryTransactionDTO.transactionModifiedDate).isEqualTo(DEFAULT_TRANSACTION_MODIFIED_DATE);
        assertThat(testInventoryTransactionDTO.quantity).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInventoryTransactionDTO.transactionComments).isEqualTo(DEFAULT_TRANSACTION_COMMENTS);
        assertThat(testInventoryTransactionDTO.activationStatus).isEqualTo(DEFAULT_ACTIVATION_STATUS);
    }

    @Test
    public void createInventoryTransactionWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the InventoryTransaction with an existing ID
        inventoryTransactionDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(inventoryTransactionDTO)
            .when()
            .post("/api/inventory-transactions")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the InventoryTransaction in the database
        var inventoryTransactionDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(inventoryTransactionDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkInvoiceIdIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        inventoryTransactionDTO.invoiceId = null;

        // Create the InventoryTransaction, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(inventoryTransactionDTO)
            .when()
            .post("/api/inventory-transactions")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the InventoryTransaction in the database
        var inventoryTransactionDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(inventoryTransactionDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkQuantityIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        inventoryTransactionDTO.quantity = null;

        // Create the InventoryTransaction, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(inventoryTransactionDTO)
            .when()
            .post("/api/inventory-transactions")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the InventoryTransaction in the database
        var inventoryTransactionDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(inventoryTransactionDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkActivationStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        inventoryTransactionDTO.activationStatus = null;

        // Create the InventoryTransaction, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(inventoryTransactionDTO)
            .when()
            .post("/api/inventory-transactions")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the InventoryTransaction in the database
        var inventoryTransactionDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(inventoryTransactionDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateInventoryTransaction() {
        // Initialize the database
        inventoryTransactionDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(inventoryTransactionDTO)
            .when()
            .post("/api/inventory-transactions")
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
            .get("/api/inventory-transactions")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the inventoryTransaction
        var updatedInventoryTransactionDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions/{id}", inventoryTransactionDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the inventoryTransaction
        updatedInventoryTransactionDTO.invoiceId = UPDATED_INVOICE_ID;
        updatedInventoryTransactionDTO.transactionCreatedDate = UPDATED_TRANSACTION_CREATED_DATE;
        updatedInventoryTransactionDTO.transactionModifiedDate = UPDATED_TRANSACTION_MODIFIED_DATE;
        updatedInventoryTransactionDTO.quantity = UPDATED_QUANTITY;
        updatedInventoryTransactionDTO.transactionComments = UPDATED_TRANSACTION_COMMENTS;
        updatedInventoryTransactionDTO.activationStatus = UPDATED_ACTIVATION_STATUS;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedInventoryTransactionDTO)
            .when()
            .put("/api/inventory-transactions/" + inventoryTransactionDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the InventoryTransaction in the database
        var inventoryTransactionDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(inventoryTransactionDTOList).hasSize(databaseSizeBeforeUpdate);
        var testInventoryTransactionDTO = inventoryTransactionDTOList
            .stream()
            .filter(it -> updatedInventoryTransactionDTO.id.equals(it.id))
            .findFirst()
            .get();
        assertThat(testInventoryTransactionDTO.invoiceId).isEqualTo(UPDATED_INVOICE_ID);
        assertThat(testInventoryTransactionDTO.transactionCreatedDate).isEqualTo(UPDATED_TRANSACTION_CREATED_DATE);
        assertThat(testInventoryTransactionDTO.transactionModifiedDate).isEqualTo(UPDATED_TRANSACTION_MODIFIED_DATE);
        assertThat(testInventoryTransactionDTO.quantity).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInventoryTransactionDTO.transactionComments).isEqualTo(UPDATED_TRANSACTION_COMMENTS);
        assertThat(testInventoryTransactionDTO.activationStatus).isEqualTo(UPDATED_ACTIVATION_STATUS);
    }

    @Test
    public void updateNonExistingInventoryTransaction() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions")
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
            .body(inventoryTransactionDTO)
            .when()
            .put("/api/inventory-transactions/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the InventoryTransaction in the database
        var inventoryTransactionDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(inventoryTransactionDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteInventoryTransaction() {
        // Initialize the database
        inventoryTransactionDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(inventoryTransactionDTO)
            .when()
            .post("/api/inventory-transactions")
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
            .get("/api/inventory-transactions")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the inventoryTransaction
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/inventory-transactions/{id}", inventoryTransactionDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var inventoryTransactionDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(inventoryTransactionDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllInventoryTransactions() {
        // Initialize the database
        inventoryTransactionDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(inventoryTransactionDTO)
            .when()
            .post("/api/inventory-transactions")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the inventoryTransactionList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(inventoryTransactionDTO.id.intValue()))
            .body("invoiceId", hasItem(DEFAULT_INVOICE_ID.intValue()))
            .body("transactionCreatedDate", hasItem(TestUtil.formatDateTime(DEFAULT_TRANSACTION_CREATED_DATE)))
            .body("transactionModifiedDate", hasItem(TestUtil.formatDateTime(DEFAULT_TRANSACTION_MODIFIED_DATE)))
            .body("quantity", hasItem(DEFAULT_QUANTITY.intValue()))
            .body("transactionComments", hasItem(DEFAULT_TRANSACTION_COMMENTS))
            .body("activationStatus", hasItem(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getInventoryTransaction() {
        // Initialize the database
        inventoryTransactionDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(inventoryTransactionDTO)
            .when()
            .post("/api/inventory-transactions")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the inventoryTransaction
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/inventory-transactions/{id}", inventoryTransactionDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the inventoryTransaction
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions/{id}", inventoryTransactionDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(inventoryTransactionDTO.id.intValue()))
            .body("invoiceId", is(DEFAULT_INVOICE_ID.intValue()))
            .body("transactionCreatedDate", is(TestUtil.formatDateTime(DEFAULT_TRANSACTION_CREATED_DATE)))
            .body("transactionModifiedDate", is(TestUtil.formatDateTime(DEFAULT_TRANSACTION_MODIFIED_DATE)))
            .body("quantity", is(DEFAULT_QUANTITY.intValue()))
            .body("transactionComments", is(DEFAULT_TRANSACTION_COMMENTS))
            .body("activationStatus", is(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getNonExistingInventoryTransaction() {
        // Get the inventoryTransaction
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/inventory-transactions/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
