package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import de.org.dexterity.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.WarehouseDTO;
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
public class WarehouseResourceTest {

    private static final TypeRef<WarehouseDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<WarehouseDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_ACRONYM = "AAAAAAAAAA";
    private static final String UPDATED_ACRONYM = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_HOUSE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_HOUSE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBB";

    private static final byte[] DEFAULT_POINT_LOCATION = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_POINT_LOCATION = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_POINT_LOCATION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_POINT_LOCATION_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_MAIN_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_LAND_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LAND_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FAX_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_FAX_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRA_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_EXTRA_DETAILS = "BBBBBBBBBB";

    String adminToken;

    WarehouseDTO warehouseDTO;

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
    public static WarehouseDTO createEntity() {
        var warehouseDTO = new WarehouseDTO();
        warehouseDTO.acronym = DEFAULT_ACRONYM;
        warehouseDTO.name = DEFAULT_NAME;
        warehouseDTO.description = DEFAULT_DESCRIPTION;
        warehouseDTO.streetAddress = DEFAULT_STREET_ADDRESS;
        warehouseDTO.houseNumber = DEFAULT_HOUSE_NUMBER;
        warehouseDTO.locationName = DEFAULT_LOCATION_NAME;
        warehouseDTO.postalCode = DEFAULT_POSTAL_CODE;
        warehouseDTO.pointLocation = DEFAULT_POINT_LOCATION;
        warehouseDTO.mainEmail = DEFAULT_MAIN_EMAIL;
        warehouseDTO.landPhoneNumber = DEFAULT_LAND_PHONE_NUMBER;
        warehouseDTO.mobilePhoneNumber = DEFAULT_MOBILE_PHONE_NUMBER;
        warehouseDTO.faxNumber = DEFAULT_FAX_NUMBER;
        warehouseDTO.extraDetails = DEFAULT_EXTRA_DETAILS;
        return warehouseDTO;
    }

    @BeforeEach
    public void initTest() {
        warehouseDTO = createEntity();
    }

    @Test
    public void createWarehouse() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Warehouse
        warehouseDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(warehouseDTO)
            .when()
            .post("/api/warehouses")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Warehouse in the database
        var warehouseDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(warehouseDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testWarehouseDTO = warehouseDTOList.stream().filter(it -> warehouseDTO.id.equals(it.id)).findFirst().get();
        assertThat(testWarehouseDTO.acronym).isEqualTo(DEFAULT_ACRONYM);
        assertThat(testWarehouseDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testWarehouseDTO.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWarehouseDTO.streetAddress).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testWarehouseDTO.houseNumber).isEqualTo(DEFAULT_HOUSE_NUMBER);
        assertThat(testWarehouseDTO.locationName).isEqualTo(DEFAULT_LOCATION_NAME);
        assertThat(testWarehouseDTO.postalCode).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testWarehouseDTO.pointLocation).isEqualTo(DEFAULT_POINT_LOCATION);
        assertThat(testWarehouseDTO.mainEmail).isEqualTo(DEFAULT_MAIN_EMAIL);
        assertThat(testWarehouseDTO.landPhoneNumber).isEqualTo(DEFAULT_LAND_PHONE_NUMBER);
        assertThat(testWarehouseDTO.mobilePhoneNumber).isEqualTo(DEFAULT_MOBILE_PHONE_NUMBER);
        assertThat(testWarehouseDTO.faxNumber).isEqualTo(DEFAULT_FAX_NUMBER);
        assertThat(testWarehouseDTO.extraDetails).isEqualTo(DEFAULT_EXTRA_DETAILS);
    }

    @Test
    public void createWarehouseWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Warehouse with an existing ID
        warehouseDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(warehouseDTO)
            .when()
            .post("/api/warehouses")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Warehouse in the database
        var warehouseDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(warehouseDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkAcronymIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        warehouseDTO.acronym = null;

        // Create the Warehouse, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(warehouseDTO)
            .when()
            .post("/api/warehouses")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Warehouse in the database
        var warehouseDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(warehouseDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        warehouseDTO.name = null;

        // Create the Warehouse, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(warehouseDTO)
            .when()
            .post("/api/warehouses")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Warehouse in the database
        var warehouseDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(warehouseDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStreetAddressIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        warehouseDTO.streetAddress = null;

        // Create the Warehouse, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(warehouseDTO)
            .when()
            .post("/api/warehouses")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Warehouse in the database
        var warehouseDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(warehouseDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPostalCodeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        warehouseDTO.postalCode = null;

        // Create the Warehouse, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(warehouseDTO)
            .when()
            .post("/api/warehouses")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Warehouse in the database
        var warehouseDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(warehouseDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateWarehouse() {
        // Initialize the database
        warehouseDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(warehouseDTO)
            .when()
            .post("/api/warehouses")
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
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the warehouse
        var updatedWarehouseDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses/{id}", warehouseDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the warehouse
        updatedWarehouseDTO.acronym = UPDATED_ACRONYM;
        updatedWarehouseDTO.name = UPDATED_NAME;
        updatedWarehouseDTO.description = UPDATED_DESCRIPTION;
        updatedWarehouseDTO.streetAddress = UPDATED_STREET_ADDRESS;
        updatedWarehouseDTO.houseNumber = UPDATED_HOUSE_NUMBER;
        updatedWarehouseDTO.locationName = UPDATED_LOCATION_NAME;
        updatedWarehouseDTO.postalCode = UPDATED_POSTAL_CODE;
        updatedWarehouseDTO.pointLocation = UPDATED_POINT_LOCATION;
        updatedWarehouseDTO.mainEmail = UPDATED_MAIN_EMAIL;
        updatedWarehouseDTO.landPhoneNumber = UPDATED_LAND_PHONE_NUMBER;
        updatedWarehouseDTO.mobilePhoneNumber = UPDATED_MOBILE_PHONE_NUMBER;
        updatedWarehouseDTO.faxNumber = UPDATED_FAX_NUMBER;
        updatedWarehouseDTO.extraDetails = UPDATED_EXTRA_DETAILS;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedWarehouseDTO)
            .when()
            .put("/api/warehouses/" + warehouseDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Warehouse in the database
        var warehouseDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(warehouseDTOList).hasSize(databaseSizeBeforeUpdate);
        var testWarehouseDTO = warehouseDTOList.stream().filter(it -> updatedWarehouseDTO.id.equals(it.id)).findFirst().get();
        assertThat(testWarehouseDTO.acronym).isEqualTo(UPDATED_ACRONYM);
        assertThat(testWarehouseDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testWarehouseDTO.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWarehouseDTO.streetAddress).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testWarehouseDTO.houseNumber).isEqualTo(UPDATED_HOUSE_NUMBER);
        assertThat(testWarehouseDTO.locationName).isEqualTo(UPDATED_LOCATION_NAME);
        assertThat(testWarehouseDTO.postalCode).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testWarehouseDTO.pointLocation).isEqualTo(UPDATED_POINT_LOCATION);
        assertThat(testWarehouseDTO.mainEmail).isEqualTo(UPDATED_MAIN_EMAIL);
        assertThat(testWarehouseDTO.landPhoneNumber).isEqualTo(UPDATED_LAND_PHONE_NUMBER);
        assertThat(testWarehouseDTO.mobilePhoneNumber).isEqualTo(UPDATED_MOBILE_PHONE_NUMBER);
        assertThat(testWarehouseDTO.faxNumber).isEqualTo(UPDATED_FAX_NUMBER);
        assertThat(testWarehouseDTO.extraDetails).isEqualTo(UPDATED_EXTRA_DETAILS);
    }

    @Test
    public void updateNonExistingWarehouse() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
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
            .body(warehouseDTO)
            .when()
            .put("/api/warehouses/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Warehouse in the database
        var warehouseDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(warehouseDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteWarehouse() {
        // Initialize the database
        warehouseDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(warehouseDTO)
            .when()
            .post("/api/warehouses")
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
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the warehouse
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/warehouses/{id}", warehouseDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var warehouseDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(warehouseDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllWarehouses() {
        // Initialize the database
        warehouseDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(warehouseDTO)
            .when()
            .post("/api/warehouses")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the warehouseList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(warehouseDTO.id.intValue()))
            .body("acronym", hasItem(DEFAULT_ACRONYM))
            .body("name", hasItem(DEFAULT_NAME))
            .body("description", hasItem(DEFAULT_DESCRIPTION.toString()))
            .body("streetAddress", hasItem(DEFAULT_STREET_ADDRESS))
            .body("houseNumber", hasItem(DEFAULT_HOUSE_NUMBER))
            .body("locationName", hasItem(DEFAULT_LOCATION_NAME))
            .body("postalCode", hasItem(DEFAULT_POSTAL_CODE))
            .body("pointLocation", hasItem(DEFAULT_POINT_LOCATION.toString()))
            .body("mainEmail", hasItem(DEFAULT_MAIN_EMAIL))
            .body("landPhoneNumber", hasItem(DEFAULT_LAND_PHONE_NUMBER))
            .body("mobilePhoneNumber", hasItem(DEFAULT_MOBILE_PHONE_NUMBER))
            .body("faxNumber", hasItem(DEFAULT_FAX_NUMBER))
            .body("extraDetails", hasItem(DEFAULT_EXTRA_DETAILS.toString()));
    }

    @Test
    public void getWarehouse() {
        // Initialize the database
        warehouseDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(warehouseDTO)
            .when()
            .post("/api/warehouses")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the warehouse
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/warehouses/{id}", warehouseDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the warehouse
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses/{id}", warehouseDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(warehouseDTO.id.intValue()))
            .body("acronym", is(DEFAULT_ACRONYM))
            .body("name", is(DEFAULT_NAME))
            .body("description", is(DEFAULT_DESCRIPTION.toString()))
            .body("streetAddress", is(DEFAULT_STREET_ADDRESS))
            .body("houseNumber", is(DEFAULT_HOUSE_NUMBER))
            .body("locationName", is(DEFAULT_LOCATION_NAME))
            .body("postalCode", is(DEFAULT_POSTAL_CODE))
            .body("pointLocation", is(DEFAULT_POINT_LOCATION.toString()))
            .body("mainEmail", is(DEFAULT_MAIN_EMAIL))
            .body("landPhoneNumber", is(DEFAULT_LAND_PHONE_NUMBER))
            .body("mobilePhoneNumber", is(DEFAULT_MOBILE_PHONE_NUMBER))
            .body("faxNumber", is(DEFAULT_FAX_NUMBER))
            .body("extraDetails", is(DEFAULT_EXTRA_DETAILS.toString()));
    }

    @Test
    public void getNonExistingWarehouse() {
        // Get the warehouse
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/warehouses/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
