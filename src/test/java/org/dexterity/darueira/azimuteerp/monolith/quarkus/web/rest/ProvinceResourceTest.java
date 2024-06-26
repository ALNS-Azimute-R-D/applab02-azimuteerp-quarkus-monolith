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
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.ProvinceDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class ProvinceResourceTest {

    private static final TypeRef<ProvinceDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<ProvinceDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_ACRONYM = "AAA";
    private static final String UPDATED_ACRONYM = "BBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_GEO_POLYGON_AREA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_GEO_POLYGON_AREA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_GEO_POLYGON_AREA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_GEO_POLYGON_AREA_CONTENT_TYPE = "image/png";

    String adminToken;

    ProvinceDTO provinceDTO;

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
    public static ProvinceDTO createEntity() {
        var provinceDTO = new ProvinceDTO();
        provinceDTO.acronym = DEFAULT_ACRONYM;
        provinceDTO.name = DEFAULT_NAME;
        provinceDTO.geoPolygonArea = DEFAULT_GEO_POLYGON_AREA;
        return provinceDTO;
    }

    @BeforeEach
    public void initTest() {
        provinceDTO = createEntity();
    }

    @Test
    public void createProvince() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Province
        provinceDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(provinceDTO)
            .when()
            .post("/api/provinces")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Province in the database
        var provinceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(provinceDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testProvinceDTO = provinceDTOList.stream().filter(it -> provinceDTO.id.equals(it.id)).findFirst().get();
        assertThat(testProvinceDTO.acronym).isEqualTo(DEFAULT_ACRONYM);
        assertThat(testProvinceDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testProvinceDTO.geoPolygonArea).isEqualTo(DEFAULT_GEO_POLYGON_AREA);
    }

    @Test
    public void createProvinceWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Province with an existing ID
        provinceDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(provinceDTO)
            .when()
            .post("/api/provinces")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Province in the database
        var provinceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(provinceDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkAcronymIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        provinceDTO.acronym = null;

        // Create the Province, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(provinceDTO)
            .when()
            .post("/api/provinces")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Province in the database
        var provinceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(provinceDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        provinceDTO.name = null;

        // Create the Province, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(provinceDTO)
            .when()
            .post("/api/provinces")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Province in the database
        var provinceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(provinceDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateProvince() {
        // Initialize the database
        provinceDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(provinceDTO)
            .when()
            .post("/api/provinces")
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
            .get("/api/provinces")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the province
        var updatedProvinceDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces/{id}", provinceDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the province
        updatedProvinceDTO.acronym = UPDATED_ACRONYM;
        updatedProvinceDTO.name = UPDATED_NAME;
        updatedProvinceDTO.geoPolygonArea = UPDATED_GEO_POLYGON_AREA;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedProvinceDTO)
            .when()
            .put("/api/provinces/" + provinceDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Province in the database
        var provinceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(provinceDTOList).hasSize(databaseSizeBeforeUpdate);
        var testProvinceDTO = provinceDTOList.stream().filter(it -> updatedProvinceDTO.id.equals(it.id)).findFirst().get();
        assertThat(testProvinceDTO.acronym).isEqualTo(UPDATED_ACRONYM);
        assertThat(testProvinceDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testProvinceDTO.geoPolygonArea).isEqualTo(UPDATED_GEO_POLYGON_AREA);
    }

    @Test
    public void updateNonExistingProvince() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces")
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
            .body(provinceDTO)
            .when()
            .put("/api/provinces/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Province in the database
        var provinceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(provinceDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteProvince() {
        // Initialize the database
        provinceDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(provinceDTO)
            .when()
            .post("/api/provinces")
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
            .get("/api/provinces")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the province
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/provinces/{id}", provinceDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var provinceDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(provinceDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllProvinces() {
        // Initialize the database
        provinceDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(provinceDTO)
            .when()
            .post("/api/provinces")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the provinceList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(provinceDTO.id.intValue()))
            .body("acronym", hasItem(DEFAULT_ACRONYM))
            .body("name", hasItem(DEFAULT_NAME))
            .body("geoPolygonArea", hasItem(DEFAULT_GEO_POLYGON_AREA.toString()));
    }

    @Test
    public void getProvince() {
        // Initialize the database
        provinceDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(provinceDTO)
            .when()
            .post("/api/provinces")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the province
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/provinces/{id}", provinceDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the province
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces/{id}", provinceDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(provinceDTO.id.intValue()))
            .body("acronym", is(DEFAULT_ACRONYM))
            .body("name", is(DEFAULT_NAME))
            .body("geoPolygonArea", is(DEFAULT_GEO_POLYGON_AREA.toString()));
    }

    @Test
    public void getNonExistingProvince() {
        // Get the province
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/provinces/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
