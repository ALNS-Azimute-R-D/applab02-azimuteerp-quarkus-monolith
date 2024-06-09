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
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.OrderStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrderDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class OrderResourceTest {

    private static final TypeRef<OrderDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<OrderDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_BUSINESS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_PLACED_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_PLACED_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final BigDecimal DEFAULT_TOTAL_TAX_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_TAX_VALUE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_DUE_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_DUE_VALUE = new BigDecimal(2);

    private static final OrderStatusEnum DEFAULT_STATUS = OrderStatusEnum.COMPLETED;
    private static final OrderStatusEnum UPDATED_STATUS = OrderStatusEnum.PENDING;

    private static final Instant DEFAULT_ESTIMATED_DELIVERY_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_ESTIMATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final ActivationStatusEnum DEFAULT_ACTIVATION_STATUS = ActivationStatusEnum.INACTIVE;
    private static final ActivationStatusEnum UPDATED_ACTIVATION_STATUS = ActivationStatusEnum.ACTIVE;

    String adminToken;

    OrderDTO orderDTO;

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
    public static OrderDTO createEntity() {
        var orderDTO = new OrderDTO();
        orderDTO.businessCode = DEFAULT_BUSINESS_CODE;
        orderDTO.placedDate = DEFAULT_PLACED_DATE;
        orderDTO.totalTaxValue = DEFAULT_TOTAL_TAX_VALUE;
        orderDTO.totalDueValue = DEFAULT_TOTAL_DUE_VALUE;
        orderDTO.status = DEFAULT_STATUS;
        orderDTO.estimatedDeliveryDate = DEFAULT_ESTIMATED_DELIVERY_DATE;
        orderDTO.activationStatus = DEFAULT_ACTIVATION_STATUS;
        return orderDTO;
    }

    @BeforeEach
    public void initTest() {
        orderDTO = createEntity();
    }

    @Test
    public void createOrder() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Order
        orderDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderDTO)
            .when()
            .post("/api/orders")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Order in the database
        var orderDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testOrderDTO = orderDTOList.stream().filter(it -> orderDTO.id.equals(it.id)).findFirst().get();
        assertThat(testOrderDTO.businessCode).isEqualTo(DEFAULT_BUSINESS_CODE);
        assertThat(testOrderDTO.placedDate).isEqualTo(DEFAULT_PLACED_DATE);
        assertThat(testOrderDTO.totalTaxValue).isEqualByComparingTo(DEFAULT_TOTAL_TAX_VALUE);
        assertThat(testOrderDTO.totalDueValue).isEqualByComparingTo(DEFAULT_TOTAL_DUE_VALUE);
        assertThat(testOrderDTO.status).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrderDTO.estimatedDeliveryDate).isEqualTo(DEFAULT_ESTIMATED_DELIVERY_DATE);
        assertThat(testOrderDTO.activationStatus).isEqualTo(DEFAULT_ACTIVATION_STATUS);
    }

    @Test
    public void createOrderWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Order with an existing ID
        orderDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderDTO)
            .when()
            .post("/api/orders")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Order in the database
        var orderDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkBusinessCodeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        orderDTO.businessCode = null;

        // Create the Order, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderDTO)
            .when()
            .post("/api/orders")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Order in the database
        var orderDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPlacedDateIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        orderDTO.placedDate = null;

        // Create the Order, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderDTO)
            .when()
            .post("/api/orders")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Order in the database
        var orderDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        orderDTO.status = null;

        // Create the Order, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderDTO)
            .when()
            .post("/api/orders")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Order in the database
        var orderDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkActivationStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        orderDTO.activationStatus = null;

        // Create the Order, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderDTO)
            .when()
            .post("/api/orders")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Order in the database
        var orderDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateOrder() {
        // Initialize the database
        orderDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderDTO)
            .when()
            .post("/api/orders")
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
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the order
        var updatedOrderDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders/{id}", orderDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the order
        updatedOrderDTO.businessCode = UPDATED_BUSINESS_CODE;
        updatedOrderDTO.placedDate = UPDATED_PLACED_DATE;
        updatedOrderDTO.totalTaxValue = UPDATED_TOTAL_TAX_VALUE;
        updatedOrderDTO.totalDueValue = UPDATED_TOTAL_DUE_VALUE;
        updatedOrderDTO.status = UPDATED_STATUS;
        updatedOrderDTO.estimatedDeliveryDate = UPDATED_ESTIMATED_DELIVERY_DATE;
        updatedOrderDTO.activationStatus = UPDATED_ACTIVATION_STATUS;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedOrderDTO)
            .when()
            .put("/api/orders/" + orderDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Order in the database
        var orderDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderDTOList).hasSize(databaseSizeBeforeUpdate);
        var testOrderDTO = orderDTOList.stream().filter(it -> updatedOrderDTO.id.equals(it.id)).findFirst().get();
        assertThat(testOrderDTO.businessCode).isEqualTo(UPDATED_BUSINESS_CODE);
        assertThat(testOrderDTO.placedDate).isEqualTo(UPDATED_PLACED_DATE);
        assertThat(testOrderDTO.totalTaxValue).isEqualByComparingTo(UPDATED_TOTAL_TAX_VALUE);
        assertThat(testOrderDTO.totalDueValue).isEqualByComparingTo(UPDATED_TOTAL_DUE_VALUE);
        assertThat(testOrderDTO.status).isEqualTo(UPDATED_STATUS);
        assertThat(testOrderDTO.estimatedDeliveryDate).isEqualTo(UPDATED_ESTIMATED_DELIVERY_DATE);
        assertThat(testOrderDTO.activationStatus).isEqualTo(UPDATED_ACTIVATION_STATUS);
    }

    @Test
    public void updateNonExistingOrder() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
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
            .body(orderDTO)
            .when()
            .put("/api/orders/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Order in the database
        var orderDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteOrder() {
        // Initialize the database
        orderDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderDTO)
            .when()
            .post("/api/orders")
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
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the order
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/orders/{id}", orderDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var orderDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllOrders() {
        // Initialize the database
        orderDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderDTO)
            .when()
            .post("/api/orders")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the orderList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(orderDTO.id.intValue()))
            .body("businessCode", hasItem(DEFAULT_BUSINESS_CODE))
            .body("placedDate", hasItem(TestUtil.formatDateTime(DEFAULT_PLACED_DATE)))
            .body("totalTaxValue", hasItem(DEFAULT_TOTAL_TAX_VALUE.floatValue()))
            .body("totalDueValue", hasItem(DEFAULT_TOTAL_DUE_VALUE.floatValue()))
            .body("status", hasItem(DEFAULT_STATUS.toString()))
            .body("estimatedDeliveryDate", hasItem(TestUtil.formatDateTime(DEFAULT_ESTIMATED_DELIVERY_DATE)))
            .body("activationStatus", hasItem(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getOrder() {
        // Initialize the database
        orderDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderDTO)
            .when()
            .post("/api/orders")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the order
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/orders/{id}", orderDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the order
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders/{id}", orderDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(orderDTO.id.intValue()))
            .body("businessCode", is(DEFAULT_BUSINESS_CODE))
            .body("placedDate", is(TestUtil.formatDateTime(DEFAULT_PLACED_DATE)))
            .body("totalTaxValue", comparesEqualTo(DEFAULT_TOTAL_TAX_VALUE.floatValue()))
            .body("totalDueValue", comparesEqualTo(DEFAULT_TOTAL_DUE_VALUE.floatValue()))
            .body("status", is(DEFAULT_STATUS.toString()))
            .body("estimatedDeliveryDate", is(TestUtil.formatDateTime(DEFAULT_ESTIMATED_DELIVERY_DATE)))
            .body("activationStatus", is(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getNonExistingOrder() {
        // Get the order
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/orders/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
