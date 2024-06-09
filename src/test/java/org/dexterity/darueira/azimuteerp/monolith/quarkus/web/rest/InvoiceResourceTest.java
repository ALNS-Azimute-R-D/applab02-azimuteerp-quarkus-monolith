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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import liquibase.Liquibase;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.InvoiceStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.InvoiceDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class InvoiceResourceTest {

    private static final TypeRef<InvoiceDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<InvoiceDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_BUSINESS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_INVOICE_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_INVOICE_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TAX_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX_VALUE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SHIPPING_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SHIPPING_VALUE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_AMOUNT_DUE_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT_DUE_VALUE = new BigDecimal(2);

    private static final Integer DEFAULT_NUMBER_OF_INSTALLMENTS_ORIGINAL = 1;
    private static final Integer UPDATED_NUMBER_OF_INSTALLMENTS_ORIGINAL = 2;

    private static final Integer DEFAULT_NUMBER_OF_INSTALLMENTS_PAID = 1;
    private static final Integer UPDATED_NUMBER_OF_INSTALLMENTS_PAID = 2;

    private static final BigDecimal DEFAULT_AMOUNT_PAID_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT_PAID_VALUE = new BigDecimal(2);

    private static final InvoiceStatusEnum DEFAULT_STATUS = InvoiceStatusEnum.PAID_ONCE;
    private static final InvoiceStatusEnum UPDATED_STATUS = InvoiceStatusEnum.PAYING_IN_INSTALLMENTS;

    private static final String DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON = "BBBBBBBBBB";

    private static final ActivationStatusEnum DEFAULT_ACTIVATION_STATUS = ActivationStatusEnum.INACTIVE;
    private static final ActivationStatusEnum UPDATED_ACTIVATION_STATUS = ActivationStatusEnum.ACTIVE;

    String adminToken;

    InvoiceDTO invoiceDTO;

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
    public static InvoiceDTO createEntity() {
        var invoiceDTO = new InvoiceDTO();
        invoiceDTO.businessCode = DEFAULT_BUSINESS_CODE;
        invoiceDTO.invoiceDate = DEFAULT_INVOICE_DATE;
        invoiceDTO.dueDate = DEFAULT_DUE_DATE;
        invoiceDTO.description = DEFAULT_DESCRIPTION;
        invoiceDTO.taxValue = DEFAULT_TAX_VALUE;
        invoiceDTO.shippingValue = DEFAULT_SHIPPING_VALUE;
        invoiceDTO.amountDueValue = DEFAULT_AMOUNT_DUE_VALUE;
        invoiceDTO.numberOfInstallmentsOriginal = DEFAULT_NUMBER_OF_INSTALLMENTS_ORIGINAL;
        invoiceDTO.numberOfInstallmentsPaid = DEFAULT_NUMBER_OF_INSTALLMENTS_PAID;
        invoiceDTO.amountPaidValue = DEFAULT_AMOUNT_PAID_VALUE;
        invoiceDTO.status = DEFAULT_STATUS;
        invoiceDTO.customAttributesDetailsJSON = DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON;
        invoiceDTO.activationStatus = DEFAULT_ACTIVATION_STATUS;
        return invoiceDTO;
    }

    @BeforeEach
    public void initTest() {
        invoiceDTO = createEntity();
    }

    @Test
    public void createInvoice() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Invoice
        invoiceDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(invoiceDTO)
            .when()
            .post("/api/invoices")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Invoice in the database
        var invoiceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(invoiceDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testInvoiceDTO = invoiceDTOList.stream().filter(it -> invoiceDTO.id.equals(it.id)).findFirst().get();
        assertThat(testInvoiceDTO.businessCode).isEqualTo(DEFAULT_BUSINESS_CODE);
        assertThat(testInvoiceDTO.invoiceDate).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testInvoiceDTO.dueDate).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testInvoiceDTO.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInvoiceDTO.taxValue).isEqualByComparingTo(DEFAULT_TAX_VALUE);
        assertThat(testInvoiceDTO.shippingValue).isEqualByComparingTo(DEFAULT_SHIPPING_VALUE);
        assertThat(testInvoiceDTO.amountDueValue).isEqualByComparingTo(DEFAULT_AMOUNT_DUE_VALUE);
        assertThat(testInvoiceDTO.numberOfInstallmentsOriginal).isEqualTo(DEFAULT_NUMBER_OF_INSTALLMENTS_ORIGINAL);
        assertThat(testInvoiceDTO.numberOfInstallmentsPaid).isEqualTo(DEFAULT_NUMBER_OF_INSTALLMENTS_PAID);
        assertThat(testInvoiceDTO.amountPaidValue).isEqualByComparingTo(DEFAULT_AMOUNT_PAID_VALUE);
        assertThat(testInvoiceDTO.status).isEqualTo(DEFAULT_STATUS);
        assertThat(testInvoiceDTO.customAttributesDetailsJSON).isEqualTo(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON);
        assertThat(testInvoiceDTO.activationStatus).isEqualTo(DEFAULT_ACTIVATION_STATUS);
    }

    @Test
    public void createInvoiceWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Invoice with an existing ID
        invoiceDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(invoiceDTO)
            .when()
            .post("/api/invoices")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Invoice in the database
        var invoiceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(invoiceDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkBusinessCodeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        invoiceDTO.businessCode = null;

        // Create the Invoice, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(invoiceDTO)
            .when()
            .post("/api/invoices")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Invoice in the database
        var invoiceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(invoiceDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        invoiceDTO.description = null;

        // Create the Invoice, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(invoiceDTO)
            .when()
            .post("/api/invoices")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Invoice in the database
        var invoiceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(invoiceDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNumberOfInstallmentsOriginalIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        invoiceDTO.numberOfInstallmentsOriginal = null;

        // Create the Invoice, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(invoiceDTO)
            .when()
            .post("/api/invoices")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Invoice in the database
        var invoiceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(invoiceDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        invoiceDTO.status = null;

        // Create the Invoice, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(invoiceDTO)
            .when()
            .post("/api/invoices")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Invoice in the database
        var invoiceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(invoiceDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkActivationStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        invoiceDTO.activationStatus = null;

        // Create the Invoice, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(invoiceDTO)
            .when()
            .post("/api/invoices")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Invoice in the database
        var invoiceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(invoiceDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateInvoice() {
        // Initialize the database
        invoiceDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(invoiceDTO)
            .when()
            .post("/api/invoices")
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
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the invoice
        var updatedInvoiceDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices/{id}", invoiceDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the invoice
        updatedInvoiceDTO.businessCode = UPDATED_BUSINESS_CODE;
        updatedInvoiceDTO.invoiceDate = UPDATED_INVOICE_DATE;
        updatedInvoiceDTO.dueDate = UPDATED_DUE_DATE;
        updatedInvoiceDTO.description = UPDATED_DESCRIPTION;
        updatedInvoiceDTO.taxValue = UPDATED_TAX_VALUE;
        updatedInvoiceDTO.shippingValue = UPDATED_SHIPPING_VALUE;
        updatedInvoiceDTO.amountDueValue = UPDATED_AMOUNT_DUE_VALUE;
        updatedInvoiceDTO.numberOfInstallmentsOriginal = UPDATED_NUMBER_OF_INSTALLMENTS_ORIGINAL;
        updatedInvoiceDTO.numberOfInstallmentsPaid = UPDATED_NUMBER_OF_INSTALLMENTS_PAID;
        updatedInvoiceDTO.amountPaidValue = UPDATED_AMOUNT_PAID_VALUE;
        updatedInvoiceDTO.status = UPDATED_STATUS;
        updatedInvoiceDTO.customAttributesDetailsJSON = UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON;
        updatedInvoiceDTO.activationStatus = UPDATED_ACTIVATION_STATUS;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedInvoiceDTO)
            .when()
            .put("/api/invoices/" + invoiceDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Invoice in the database
        var invoiceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(invoiceDTOList).hasSize(databaseSizeBeforeUpdate);
        var testInvoiceDTO = invoiceDTOList.stream().filter(it -> updatedInvoiceDTO.id.equals(it.id)).findFirst().get();
        assertThat(testInvoiceDTO.businessCode).isEqualTo(UPDATED_BUSINESS_CODE);
        assertThat(testInvoiceDTO.invoiceDate).isEqualTo(UPDATED_INVOICE_DATE);
        assertThat(testInvoiceDTO.dueDate).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testInvoiceDTO.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInvoiceDTO.taxValue).isEqualByComparingTo(UPDATED_TAX_VALUE);
        assertThat(testInvoiceDTO.shippingValue).isEqualByComparingTo(UPDATED_SHIPPING_VALUE);
        assertThat(testInvoiceDTO.amountDueValue).isEqualByComparingTo(UPDATED_AMOUNT_DUE_VALUE);
        assertThat(testInvoiceDTO.numberOfInstallmentsOriginal).isEqualTo(UPDATED_NUMBER_OF_INSTALLMENTS_ORIGINAL);
        assertThat(testInvoiceDTO.numberOfInstallmentsPaid).isEqualTo(UPDATED_NUMBER_OF_INSTALLMENTS_PAID);
        assertThat(testInvoiceDTO.amountPaidValue).isEqualByComparingTo(UPDATED_AMOUNT_PAID_VALUE);
        assertThat(testInvoiceDTO.status).isEqualTo(UPDATED_STATUS);
        assertThat(testInvoiceDTO.customAttributesDetailsJSON).isEqualTo(UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON);
        assertThat(testInvoiceDTO.activationStatus).isEqualTo(UPDATED_ACTIVATION_STATUS);
    }

    @Test
    public void updateNonExistingInvoice() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
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
            .body(invoiceDTO)
            .when()
            .put("/api/invoices/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Invoice in the database
        var invoiceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(invoiceDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteInvoice() {
        // Initialize the database
        invoiceDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(invoiceDTO)
            .when()
            .post("/api/invoices")
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
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the invoice
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/invoices/{id}", invoiceDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var invoiceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(invoiceDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllInvoices() {
        // Initialize the database
        invoiceDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(invoiceDTO)
            .when()
            .post("/api/invoices")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the invoiceList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(invoiceDTO.id.intValue()))
            .body("businessCode", hasItem(DEFAULT_BUSINESS_CODE))
            .body("invoiceDate", hasItem(TestUtil.formatDateTime(DEFAULT_INVOICE_DATE)))
            .body("dueDate", hasItem(TestUtil.formatDateTime(DEFAULT_DUE_DATE)))
            .body("description", hasItem(DEFAULT_DESCRIPTION))
            .body("taxValue", hasItem(DEFAULT_TAX_VALUE.floatValue()))
            .body("shippingValue", hasItem(DEFAULT_SHIPPING_VALUE.floatValue()))
            .body("amountDueValue", hasItem(DEFAULT_AMOUNT_DUE_VALUE.floatValue()))
            .body("numberOfInstallmentsOriginal", hasItem(DEFAULT_NUMBER_OF_INSTALLMENTS_ORIGINAL.intValue()))
            .body("numberOfInstallmentsPaid", hasItem(DEFAULT_NUMBER_OF_INSTALLMENTS_PAID.intValue()))
            .body("amountPaidValue", hasItem(DEFAULT_AMOUNT_PAID_VALUE.floatValue()))
            .body("status", hasItem(DEFAULT_STATUS.toString()))
            .body("customAttributesDetailsJSON", hasItem(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON))
            .body("activationStatus", hasItem(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getInvoice() {
        // Initialize the database
        invoiceDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(invoiceDTO)
            .when()
            .post("/api/invoices")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the invoice
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/invoices/{id}", invoiceDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the invoice
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices/{id}", invoiceDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(invoiceDTO.id.intValue()))
            .body("businessCode", is(DEFAULT_BUSINESS_CODE))
            .body("invoiceDate", is(TestUtil.formatDateTime(DEFAULT_INVOICE_DATE)))
            .body("dueDate", is(TestUtil.formatDateTime(DEFAULT_DUE_DATE)))
            .body("description", is(DEFAULT_DESCRIPTION))
            .body("taxValue", comparesEqualTo(DEFAULT_TAX_VALUE.floatValue()))
            .body("shippingValue", comparesEqualTo(DEFAULT_SHIPPING_VALUE.floatValue()))
            .body("amountDueValue", comparesEqualTo(DEFAULT_AMOUNT_DUE_VALUE.floatValue()))
            .body("numberOfInstallmentsOriginal", is(DEFAULT_NUMBER_OF_INSTALLMENTS_ORIGINAL.intValue()))
            .body("numberOfInstallmentsPaid", is(DEFAULT_NUMBER_OF_INSTALLMENTS_PAID.intValue()))
            .body("amountPaidValue", comparesEqualTo(DEFAULT_AMOUNT_PAID_VALUE.floatValue()))
            .body("status", is(DEFAULT_STATUS.toString()))
            .body("customAttributesDetailsJSON", is(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON))
            .body("activationStatus", is(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getNonExistingInvoice() {
        // Get the invoice
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/invoices/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
