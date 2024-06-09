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
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.StockLevelDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class StockLevelResourceTest {

    private static final TypeRef<StockLevelDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<StockLevelDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final Integer DEFAULT_REMAINING_QUANTITY = 1;
    private static final Integer UPDATED_REMAINING_QUANTITY = 2;

    private static final String DEFAULT_COMMON_ATTRIBUTES_DETAILS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_COMMON_ATTRIBUTES_DETAILS_JSON = "BBBBBBBBBB";

    String adminToken;

    StockLevelDTO stockLevelDTO;

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
    public static StockLevelDTO createEntity() {
        var stockLevelDTO = new StockLevelDTO();
        stockLevelDTO.lastModifiedDate = DEFAULT_LAST_MODIFIED_DATE;
        stockLevelDTO.remainingQuantity = DEFAULT_REMAINING_QUANTITY;
        stockLevelDTO.commonAttributesDetailsJSON = DEFAULT_COMMON_ATTRIBUTES_DETAILS_JSON;
        return stockLevelDTO;
    }

    @BeforeEach
    public void initTest() {
        stockLevelDTO = createEntity();
    }

    @Test
    public void createStockLevel() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the StockLevel
        stockLevelDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(stockLevelDTO)
            .when()
            .post("/api/stock-levels")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the StockLevel in the database
        var stockLevelDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(stockLevelDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testStockLevelDTO = stockLevelDTOList.stream().filter(it -> stockLevelDTO.id.equals(it.id)).findFirst().get();
        assertThat(testStockLevelDTO.lastModifiedDate).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testStockLevelDTO.remainingQuantity).isEqualTo(DEFAULT_REMAINING_QUANTITY);
        assertThat(testStockLevelDTO.commonAttributesDetailsJSON).isEqualTo(DEFAULT_COMMON_ATTRIBUTES_DETAILS_JSON);
    }

    @Test
    public void createStockLevelWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the StockLevel with an existing ID
        stockLevelDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(stockLevelDTO)
            .when()
            .post("/api/stock-levels")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the StockLevel in the database
        var stockLevelDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(stockLevelDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkLastModifiedDateIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        stockLevelDTO.lastModifiedDate = null;

        // Create the StockLevel, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(stockLevelDTO)
            .when()
            .post("/api/stock-levels")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the StockLevel in the database
        var stockLevelDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(stockLevelDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkRemainingQuantityIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        stockLevelDTO.remainingQuantity = null;

        // Create the StockLevel, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(stockLevelDTO)
            .when()
            .post("/api/stock-levels")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the StockLevel in the database
        var stockLevelDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(stockLevelDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateStockLevel() {
        // Initialize the database
        stockLevelDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(stockLevelDTO)
            .when()
            .post("/api/stock-levels")
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
            .get("/api/stock-levels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the stockLevel
        var updatedStockLevelDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels/{id}", stockLevelDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the stockLevel
        updatedStockLevelDTO.lastModifiedDate = UPDATED_LAST_MODIFIED_DATE;
        updatedStockLevelDTO.remainingQuantity = UPDATED_REMAINING_QUANTITY;
        updatedStockLevelDTO.commonAttributesDetailsJSON = UPDATED_COMMON_ATTRIBUTES_DETAILS_JSON;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedStockLevelDTO)
            .when()
            .put("/api/stock-levels/" + stockLevelDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the StockLevel in the database
        var stockLevelDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(stockLevelDTOList).hasSize(databaseSizeBeforeUpdate);
        var testStockLevelDTO = stockLevelDTOList.stream().filter(it -> updatedStockLevelDTO.id.equals(it.id)).findFirst().get();
        assertThat(testStockLevelDTO.lastModifiedDate).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testStockLevelDTO.remainingQuantity).isEqualTo(UPDATED_REMAINING_QUANTITY);
        assertThat(testStockLevelDTO.commonAttributesDetailsJSON).isEqualTo(UPDATED_COMMON_ATTRIBUTES_DETAILS_JSON);
    }

    @Test
    public void updateNonExistingStockLevel() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels")
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
            .body(stockLevelDTO)
            .when()
            .put("/api/stock-levels/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the StockLevel in the database
        var stockLevelDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(stockLevelDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteStockLevel() {
        // Initialize the database
        stockLevelDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(stockLevelDTO)
            .when()
            .post("/api/stock-levels")
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
            .get("/api/stock-levels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the stockLevel
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/stock-levels/{id}", stockLevelDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var stockLevelDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(stockLevelDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllStockLevels() {
        // Initialize the database
        stockLevelDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(stockLevelDTO)
            .when()
            .post("/api/stock-levels")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the stockLevelList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(stockLevelDTO.id.intValue()))
            .body("lastModifiedDate", hasItem(TestUtil.formatDateTime(DEFAULT_LAST_MODIFIED_DATE)))
            .body("remainingQuantity", hasItem(DEFAULT_REMAINING_QUANTITY.intValue()))
            .body("commonAttributesDetailsJSON", hasItem(DEFAULT_COMMON_ATTRIBUTES_DETAILS_JSON));
    }

    @Test
    public void getStockLevel() {
        // Initialize the database
        stockLevelDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(stockLevelDTO)
            .when()
            .post("/api/stock-levels")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the stockLevel
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/stock-levels/{id}", stockLevelDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the stockLevel
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels/{id}", stockLevelDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(stockLevelDTO.id.intValue()))
            .body("lastModifiedDate", is(TestUtil.formatDateTime(DEFAULT_LAST_MODIFIED_DATE)))
            .body("remainingQuantity", is(DEFAULT_REMAINING_QUANTITY.intValue()))
            .body("commonAttributesDetailsJSON", is(DEFAULT_COMMON_ATTRIBUTES_DETAILS_JSON));
    }

    @Test
    public void getNonExistingStockLevel() {
        // Get the stockLevel
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/stock-levels/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
