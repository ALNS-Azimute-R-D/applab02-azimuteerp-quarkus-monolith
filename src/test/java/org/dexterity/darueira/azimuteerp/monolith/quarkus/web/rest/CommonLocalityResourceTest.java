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
import java.util.List;
import liquibase.Liquibase;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.CommonLocalityDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class CommonLocalityResourceTest {

    private static final TypeRef<CommonLocalityDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<CommonLocalityDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

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

    private static final byte[] DEFAULT_GEO_POLYGON_AREA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_GEO_POLYGON_AREA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_GEO_POLYGON_AREA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_GEO_POLYGON_AREA_CONTENT_TYPE = "image/png";

    String adminToken;

    CommonLocalityDTO commonLocalityDTO;

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
    public static CommonLocalityDTO createEntity() {
        var commonLocalityDTO = new CommonLocalityDTO();
        commonLocalityDTO.acronym = DEFAULT_ACRONYM;
        commonLocalityDTO.name = DEFAULT_NAME;
        commonLocalityDTO.description = DEFAULT_DESCRIPTION;
        commonLocalityDTO.streetAddress = DEFAULT_STREET_ADDRESS;
        commonLocalityDTO.houseNumber = DEFAULT_HOUSE_NUMBER;
        commonLocalityDTO.locationName = DEFAULT_LOCATION_NAME;
        commonLocalityDTO.postalCode = DEFAULT_POSTAL_CODE;
        commonLocalityDTO.geoPolygonArea = DEFAULT_GEO_POLYGON_AREA;
        return commonLocalityDTO;
    }

    @BeforeEach
    public void initTest() {
        commonLocalityDTO = createEntity();
    }

    @Test
    public void createCommonLocality() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the CommonLocality
        commonLocalityDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(commonLocalityDTO)
            .when()
            .post("/api/common-localities")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the CommonLocality in the database
        var commonLocalityDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(commonLocalityDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testCommonLocalityDTO = commonLocalityDTOList.stream().filter(it -> commonLocalityDTO.id.equals(it.id)).findFirst().get();
        assertThat(testCommonLocalityDTO.acronym).isEqualTo(DEFAULT_ACRONYM);
        assertThat(testCommonLocalityDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testCommonLocalityDTO.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCommonLocalityDTO.streetAddress).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testCommonLocalityDTO.houseNumber).isEqualTo(DEFAULT_HOUSE_NUMBER);
        assertThat(testCommonLocalityDTO.locationName).isEqualTo(DEFAULT_LOCATION_NAME);
        assertThat(testCommonLocalityDTO.postalCode).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testCommonLocalityDTO.geoPolygonArea).isEqualTo(DEFAULT_GEO_POLYGON_AREA);
    }

    @Test
    public void createCommonLocalityWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the CommonLocality with an existing ID
        commonLocalityDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(commonLocalityDTO)
            .when()
            .post("/api/common-localities")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the CommonLocality in the database
        var commonLocalityDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(commonLocalityDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkAcronymIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        commonLocalityDTO.acronym = null;

        // Create the CommonLocality, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(commonLocalityDTO)
            .when()
            .post("/api/common-localities")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the CommonLocality in the database
        var commonLocalityDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(commonLocalityDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        commonLocalityDTO.name = null;

        // Create the CommonLocality, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(commonLocalityDTO)
            .when()
            .post("/api/common-localities")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the CommonLocality in the database
        var commonLocalityDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(commonLocalityDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStreetAddressIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        commonLocalityDTO.streetAddress = null;

        // Create the CommonLocality, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(commonLocalityDTO)
            .when()
            .post("/api/common-localities")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the CommonLocality in the database
        var commonLocalityDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(commonLocalityDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPostalCodeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        commonLocalityDTO.postalCode = null;

        // Create the CommonLocality, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(commonLocalityDTO)
            .when()
            .post("/api/common-localities")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the CommonLocality in the database
        var commonLocalityDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(commonLocalityDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateCommonLocality() {
        // Initialize the database
        commonLocalityDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(commonLocalityDTO)
            .when()
            .post("/api/common-localities")
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
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the commonLocality
        var updatedCommonLocalityDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities/{id}", commonLocalityDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the commonLocality
        updatedCommonLocalityDTO.acronym = UPDATED_ACRONYM;
        updatedCommonLocalityDTO.name = UPDATED_NAME;
        updatedCommonLocalityDTO.description = UPDATED_DESCRIPTION;
        updatedCommonLocalityDTO.streetAddress = UPDATED_STREET_ADDRESS;
        updatedCommonLocalityDTO.houseNumber = UPDATED_HOUSE_NUMBER;
        updatedCommonLocalityDTO.locationName = UPDATED_LOCATION_NAME;
        updatedCommonLocalityDTO.postalCode = UPDATED_POSTAL_CODE;
        updatedCommonLocalityDTO.geoPolygonArea = UPDATED_GEO_POLYGON_AREA;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedCommonLocalityDTO)
            .when()
            .put("/api/common-localities/" + commonLocalityDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the CommonLocality in the database
        var commonLocalityDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(commonLocalityDTOList).hasSize(databaseSizeBeforeUpdate);
        var testCommonLocalityDTO = commonLocalityDTOList
            .stream()
            .filter(it -> updatedCommonLocalityDTO.id.equals(it.id))
            .findFirst()
            .get();
        assertThat(testCommonLocalityDTO.acronym).isEqualTo(UPDATED_ACRONYM);
        assertThat(testCommonLocalityDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testCommonLocalityDTO.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCommonLocalityDTO.streetAddress).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testCommonLocalityDTO.houseNumber).isEqualTo(UPDATED_HOUSE_NUMBER);
        assertThat(testCommonLocalityDTO.locationName).isEqualTo(UPDATED_LOCATION_NAME);
        assertThat(testCommonLocalityDTO.postalCode).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testCommonLocalityDTO.geoPolygonArea).isEqualTo(UPDATED_GEO_POLYGON_AREA);
    }

    @Test
    public void updateNonExistingCommonLocality() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
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
            .body(commonLocalityDTO)
            .when()
            .put("/api/common-localities/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the CommonLocality in the database
        var commonLocalityDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(commonLocalityDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCommonLocality() {
        // Initialize the database
        commonLocalityDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(commonLocalityDTO)
            .when()
            .post("/api/common-localities")
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
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the commonLocality
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/common-localities/{id}", commonLocalityDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var commonLocalityDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(commonLocalityDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllCommonLocalities() {
        // Initialize the database
        commonLocalityDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(commonLocalityDTO)
            .when()
            .post("/api/common-localities")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the commonLocalityList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(commonLocalityDTO.id.intValue()))
            .body("acronym", hasItem(DEFAULT_ACRONYM))
            .body("name", hasItem(DEFAULT_NAME))
            .body("description", hasItem(DEFAULT_DESCRIPTION))
            .body("streetAddress", hasItem(DEFAULT_STREET_ADDRESS))
            .body("houseNumber", hasItem(DEFAULT_HOUSE_NUMBER))
            .body("locationName", hasItem(DEFAULT_LOCATION_NAME))
            .body("postalCode", hasItem(DEFAULT_POSTAL_CODE))
            .body("geoPolygonArea", hasItem(DEFAULT_GEO_POLYGON_AREA.toString()));
    }

    @Test
    public void getCommonLocality() {
        // Initialize the database
        commonLocalityDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(commonLocalityDTO)
            .when()
            .post("/api/common-localities")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the commonLocality
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/common-localities/{id}", commonLocalityDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the commonLocality
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities/{id}", commonLocalityDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(commonLocalityDTO.id.intValue()))
            .body("acronym", is(DEFAULT_ACRONYM))
            .body("name", is(DEFAULT_NAME))
            .body("description", is(DEFAULT_DESCRIPTION))
            .body("streetAddress", is(DEFAULT_STREET_ADDRESS))
            .body("houseNumber", is(DEFAULT_HOUSE_NUMBER))
            .body("locationName", is(DEFAULT_LOCATION_NAME))
            .body("postalCode", is(DEFAULT_POSTAL_CODE))
            .body("geoPolygonArea", is(DEFAULT_GEO_POLYGON_AREA.toString()));
    }

    @Test
    public void getNonExistingCommonLocality() {
        // Get the commonLocality
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/common-localities/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
