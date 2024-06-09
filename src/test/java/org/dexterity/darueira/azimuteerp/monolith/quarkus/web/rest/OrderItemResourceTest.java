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
import java.util.List;
import liquibase.Liquibase;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.OrderItemStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.OrderItemDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class OrderItemResourceTest {

    private static final TypeRef<OrderItemDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<OrderItemDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(1);

    private static final OrderItemStatusEnum DEFAULT_STATUS = OrderItemStatusEnum.AVAILABLE;
    private static final OrderItemStatusEnum UPDATED_STATUS = OrderItemStatusEnum.OUT_OF_STOCK;

    String adminToken;

    OrderItemDTO orderItemDTO;

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
    public static OrderItemDTO createEntity() {
        var orderItemDTO = new OrderItemDTO();
        orderItemDTO.quantity = DEFAULT_QUANTITY;
        orderItemDTO.totalPrice = DEFAULT_TOTAL_PRICE;
        orderItemDTO.status = DEFAULT_STATUS;
        return orderItemDTO;
    }

    @BeforeEach
    public void initTest() {
        orderItemDTO = createEntity();
    }

    @Test
    public void createOrderItem() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the OrderItem
        orderItemDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderItemDTO)
            .when()
            .post("/api/order-items")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the OrderItem in the database
        var orderItemDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderItemDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testOrderItemDTO = orderItemDTOList.stream().filter(it -> orderItemDTO.id.equals(it.id)).findFirst().get();
        assertThat(testOrderItemDTO.quantity).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrderItemDTO.totalPrice).isEqualByComparingTo(DEFAULT_TOTAL_PRICE);
        assertThat(testOrderItemDTO.status).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    public void createOrderItemWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the OrderItem with an existing ID
        orderItemDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderItemDTO)
            .when()
            .post("/api/order-items")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the OrderItem in the database
        var orderItemDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderItemDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkQuantityIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        orderItemDTO.quantity = null;

        // Create the OrderItem, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderItemDTO)
            .when()
            .post("/api/order-items")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the OrderItem in the database
        var orderItemDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderItemDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTotalPriceIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        orderItemDTO.totalPrice = null;

        // Create the OrderItem, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderItemDTO)
            .when()
            .post("/api/order-items")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the OrderItem in the database
        var orderItemDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderItemDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        orderItemDTO.status = null;

        // Create the OrderItem, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderItemDTO)
            .when()
            .post("/api/order-items")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the OrderItem in the database
        var orderItemDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderItemDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateOrderItem() {
        // Initialize the database
        orderItemDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderItemDTO)
            .when()
            .post("/api/order-items")
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
            .get("/api/order-items")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the orderItem
        var updatedOrderItemDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items/{id}", orderItemDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the orderItem
        updatedOrderItemDTO.quantity = UPDATED_QUANTITY;
        updatedOrderItemDTO.totalPrice = UPDATED_TOTAL_PRICE;
        updatedOrderItemDTO.status = UPDATED_STATUS;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedOrderItemDTO)
            .when()
            .put("/api/order-items/" + orderItemDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the OrderItem in the database
        var orderItemDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderItemDTOList).hasSize(databaseSizeBeforeUpdate);
        var testOrderItemDTO = orderItemDTOList.stream().filter(it -> updatedOrderItemDTO.id.equals(it.id)).findFirst().get();
        assertThat(testOrderItemDTO.quantity).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderItemDTO.totalPrice).isEqualByComparingTo(UPDATED_TOTAL_PRICE);
        assertThat(testOrderItemDTO.status).isEqualTo(UPDATED_STATUS);
    }

    @Test
    public void updateNonExistingOrderItem() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items")
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
            .body(orderItemDTO)
            .when()
            .put("/api/order-items/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the OrderItem in the database
        var orderItemDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderItemDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteOrderItem() {
        // Initialize the database
        orderItemDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderItemDTO)
            .when()
            .post("/api/order-items")
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
            .get("/api/order-items")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the orderItem
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/order-items/{id}", orderItemDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var orderItemDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(orderItemDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllOrderItems() {
        // Initialize the database
        orderItemDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderItemDTO)
            .when()
            .post("/api/order-items")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the orderItemList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(orderItemDTO.id.intValue()))
            .body("quantity", hasItem(DEFAULT_QUANTITY.intValue()))
            .body("totalPrice", hasItem(DEFAULT_TOTAL_PRICE.floatValue()))
            .body("status", hasItem(DEFAULT_STATUS.toString()));
    }

    @Test
    public void getOrderItem() {
        // Initialize the database
        orderItemDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(orderItemDTO)
            .when()
            .post("/api/order-items")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the orderItem
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/order-items/{id}", orderItemDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the orderItem
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items/{id}", orderItemDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(orderItemDTO.id.intValue()))
            .body("quantity", is(DEFAULT_QUANTITY.intValue()))
            .body("totalPrice", comparesEqualTo(DEFAULT_TOTAL_PRICE.floatValue()))
            .body("status", is(DEFAULT_STATUS.toString()));
    }

    @Test
    public void getNonExistingOrderItem() {
        // Get the orderItem
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/order-items/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
