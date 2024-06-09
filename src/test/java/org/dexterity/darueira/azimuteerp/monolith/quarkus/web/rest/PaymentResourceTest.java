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
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.PaymentStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.PaymentTypeEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.PaymentDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class PaymentResourceTest {

    private static final TypeRef<PaymentDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<PaymentDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final Integer DEFAULT_INSTALLMENT_NUMBER = 1;
    private static final Integer UPDATED_INSTALLMENT_NUMBER = 2;

    private static final Instant DEFAULT_PAYMENT_DUE_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_PAYMENT_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final Instant DEFAULT_PAYMENT_PAID_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_PAYMENT_PAID_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final BigDecimal DEFAULT_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_AMOUNT = new BigDecimal(2);

    private static final PaymentTypeEnum DEFAULT_TYPE_OF_PAYMENT = PaymentTypeEnum.CASH;
    private static final PaymentTypeEnum UPDATED_TYPE_OF_PAYMENT = PaymentTypeEnum.CREDIT;

    private static final PaymentStatusEnum DEFAULT_STATUS_PAYMENT = PaymentStatusEnum.OPEN;
    private static final PaymentStatusEnum UPDATED_STATUS_PAYMENT = PaymentStatusEnum.DELAYED;

    private static final String DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON = "BBBBBBBBBB";

    private static final ActivationStatusEnum DEFAULT_ACTIVATION_STATUS = ActivationStatusEnum.INACTIVE;
    private static final ActivationStatusEnum UPDATED_ACTIVATION_STATUS = ActivationStatusEnum.ACTIVE;

    String adminToken;

    PaymentDTO paymentDTO;

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
    public static PaymentDTO createEntity() {
        var paymentDTO = new PaymentDTO();
        paymentDTO.installmentNumber = DEFAULT_INSTALLMENT_NUMBER;
        paymentDTO.paymentDueDate = DEFAULT_PAYMENT_DUE_DATE;
        paymentDTO.paymentPaidDate = DEFAULT_PAYMENT_PAID_DATE;
        paymentDTO.paymentAmount = DEFAULT_PAYMENT_AMOUNT;
        paymentDTO.typeOfPayment = DEFAULT_TYPE_OF_PAYMENT;
        paymentDTO.statusPayment = DEFAULT_STATUS_PAYMENT;
        paymentDTO.customAttributesDetailsJSON = DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON;
        paymentDTO.activationStatus = DEFAULT_ACTIVATION_STATUS;
        return paymentDTO;
    }

    @BeforeEach
    public void initTest() {
        paymentDTO = createEntity();
    }

    @Test
    public void createPayment() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Payment
        paymentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentDTO)
            .when()
            .post("/api/payments")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Payment in the database
        var paymentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testPaymentDTO = paymentDTOList.stream().filter(it -> paymentDTO.id.equals(it.id)).findFirst().get();
        assertThat(testPaymentDTO.installmentNumber).isEqualTo(DEFAULT_INSTALLMENT_NUMBER);
        assertThat(testPaymentDTO.paymentDueDate).isEqualTo(DEFAULT_PAYMENT_DUE_DATE);
        assertThat(testPaymentDTO.paymentPaidDate).isEqualTo(DEFAULT_PAYMENT_PAID_DATE);
        assertThat(testPaymentDTO.paymentAmount).isEqualByComparingTo(DEFAULT_PAYMENT_AMOUNT);
        assertThat(testPaymentDTO.typeOfPayment).isEqualTo(DEFAULT_TYPE_OF_PAYMENT);
        assertThat(testPaymentDTO.statusPayment).isEqualTo(DEFAULT_STATUS_PAYMENT);
        assertThat(testPaymentDTO.customAttributesDetailsJSON).isEqualTo(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON);
        assertThat(testPaymentDTO.activationStatus).isEqualTo(DEFAULT_ACTIVATION_STATUS);
    }

    @Test
    public void createPaymentWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Payment with an existing ID
        paymentDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentDTO)
            .when()
            .post("/api/payments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Payment in the database
        var paymentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkInstallmentNumberIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        paymentDTO.installmentNumber = null;

        // Create the Payment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentDTO)
            .when()
            .post("/api/payments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Payment in the database
        var paymentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPaymentDueDateIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        paymentDTO.paymentDueDate = null;

        // Create the Payment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentDTO)
            .when()
            .post("/api/payments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Payment in the database
        var paymentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPaymentPaidDateIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        paymentDTO.paymentPaidDate = null;

        // Create the Payment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentDTO)
            .when()
            .post("/api/payments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Payment in the database
        var paymentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPaymentAmountIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        paymentDTO.paymentAmount = null;

        // Create the Payment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentDTO)
            .when()
            .post("/api/payments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Payment in the database
        var paymentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTypeOfPaymentIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        paymentDTO.typeOfPayment = null;

        // Create the Payment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentDTO)
            .when()
            .post("/api/payments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Payment in the database
        var paymentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStatusPaymentIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        paymentDTO.statusPayment = null;

        // Create the Payment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentDTO)
            .when()
            .post("/api/payments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Payment in the database
        var paymentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkActivationStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        paymentDTO.activationStatus = null;

        // Create the Payment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentDTO)
            .when()
            .post("/api/payments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Payment in the database
        var paymentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updatePayment() {
        // Initialize the database
        paymentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentDTO)
            .when()
            .post("/api/payments")
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
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the payment
        var updatedPaymentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments/{id}", paymentDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the payment
        updatedPaymentDTO.installmentNumber = UPDATED_INSTALLMENT_NUMBER;
        updatedPaymentDTO.paymentDueDate = UPDATED_PAYMENT_DUE_DATE;
        updatedPaymentDTO.paymentPaidDate = UPDATED_PAYMENT_PAID_DATE;
        updatedPaymentDTO.paymentAmount = UPDATED_PAYMENT_AMOUNT;
        updatedPaymentDTO.typeOfPayment = UPDATED_TYPE_OF_PAYMENT;
        updatedPaymentDTO.statusPayment = UPDATED_STATUS_PAYMENT;
        updatedPaymentDTO.customAttributesDetailsJSON = UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON;
        updatedPaymentDTO.activationStatus = UPDATED_ACTIVATION_STATUS;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedPaymentDTO)
            .when()
            .put("/api/payments/" + paymentDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Payment in the database
        var paymentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentDTOList).hasSize(databaseSizeBeforeUpdate);
        var testPaymentDTO = paymentDTOList.stream().filter(it -> updatedPaymentDTO.id.equals(it.id)).findFirst().get();
        assertThat(testPaymentDTO.installmentNumber).isEqualTo(UPDATED_INSTALLMENT_NUMBER);
        assertThat(testPaymentDTO.paymentDueDate).isEqualTo(UPDATED_PAYMENT_DUE_DATE);
        assertThat(testPaymentDTO.paymentPaidDate).isEqualTo(UPDATED_PAYMENT_PAID_DATE);
        assertThat(testPaymentDTO.paymentAmount).isEqualByComparingTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testPaymentDTO.typeOfPayment).isEqualTo(UPDATED_TYPE_OF_PAYMENT);
        assertThat(testPaymentDTO.statusPayment).isEqualTo(UPDATED_STATUS_PAYMENT);
        assertThat(testPaymentDTO.customAttributesDetailsJSON).isEqualTo(UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON);
        assertThat(testPaymentDTO.activationStatus).isEqualTo(UPDATED_ACTIVATION_STATUS);
    }

    @Test
    public void updateNonExistingPayment() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
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
            .body(paymentDTO)
            .when()
            .put("/api/payments/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Payment in the database
        var paymentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deletePayment() {
        // Initialize the database
        paymentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentDTO)
            .when()
            .post("/api/payments")
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
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the payment
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/payments/{id}", paymentDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var paymentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(paymentDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllPayments() {
        // Initialize the database
        paymentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentDTO)
            .when()
            .post("/api/payments")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the paymentList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(paymentDTO.id.intValue()))
            .body("installmentNumber", hasItem(DEFAULT_INSTALLMENT_NUMBER.intValue()))
            .body("paymentDueDate", hasItem(TestUtil.formatDateTime(DEFAULT_PAYMENT_DUE_DATE)))
            .body("paymentPaidDate", hasItem(TestUtil.formatDateTime(DEFAULT_PAYMENT_PAID_DATE)))
            .body("paymentAmount", hasItem(DEFAULT_PAYMENT_AMOUNT.floatValue()))
            .body("typeOfPayment", hasItem(DEFAULT_TYPE_OF_PAYMENT.toString()))
            .body("statusPayment", hasItem(DEFAULT_STATUS_PAYMENT.toString()))
            .body("customAttributesDetailsJSON", hasItem(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON))
            .body("activationStatus", hasItem(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getPayment() {
        // Initialize the database
        paymentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(paymentDTO)
            .when()
            .post("/api/payments")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the payment
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/payments/{id}", paymentDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the payment
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments/{id}", paymentDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(paymentDTO.id.intValue()))
            .body("installmentNumber", is(DEFAULT_INSTALLMENT_NUMBER.intValue()))
            .body("paymentDueDate", is(TestUtil.formatDateTime(DEFAULT_PAYMENT_DUE_DATE)))
            .body("paymentPaidDate", is(TestUtil.formatDateTime(DEFAULT_PAYMENT_PAID_DATE)))
            .body("paymentAmount", comparesEqualTo(DEFAULT_PAYMENT_AMOUNT.floatValue()))
            .body("typeOfPayment", is(DEFAULT_TYPE_OF_PAYMENT.toString()))
            .body("statusPayment", is(DEFAULT_STATUS_PAYMENT.toString()))
            .body("customAttributesDetailsJSON", is(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON))
            .body("activationStatus", is(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getNonExistingPayment() {
        // Get the payment
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/payments/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
