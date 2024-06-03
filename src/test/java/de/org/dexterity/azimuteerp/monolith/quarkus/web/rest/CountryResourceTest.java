package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.ContinentEnum;
import de.org.dexterity.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.CountryDTO;
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
public class CountryResourceTest {

    private static final TypeRef<CountryDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<CountryDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_ACRONYM = "AAA";
    private static final String UPDATED_ACRONYM = "BBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ContinentEnum DEFAULT_CONTINENT = ContinentEnum.AFRICA;
    private static final ContinentEnum UPDATED_CONTINENT = ContinentEnum.ASIA;

    private static final byte[] DEFAULT_GEO_POLYGON_AREA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_GEO_POLYGON_AREA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_GEO_POLYGON_AREA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_GEO_POLYGON_AREA_CONTENT_TYPE = "image/png";

    String adminToken;

    CountryDTO countryDTO;

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
    public static CountryDTO createEntity() {
        var countryDTO = new CountryDTO();
        countryDTO.acronym = DEFAULT_ACRONYM;
        countryDTO.name = DEFAULT_NAME;
        countryDTO.continent = DEFAULT_CONTINENT;
        countryDTO.geoPolygonArea = DEFAULT_GEO_POLYGON_AREA;
        return countryDTO;
    }

    @BeforeEach
    public void initTest() {
        countryDTO = createEntity();
    }

    @Test
    public void createCountry() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Country
        countryDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(countryDTO)
            .when()
            .post("/api/countries")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Country in the database
        var countryDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(countryDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testCountryDTO = countryDTOList.stream().filter(it -> countryDTO.id.equals(it.id)).findFirst().get();
        assertThat(testCountryDTO.acronym).isEqualTo(DEFAULT_ACRONYM);
        assertThat(testCountryDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testCountryDTO.continent).isEqualTo(DEFAULT_CONTINENT);
        assertThat(testCountryDTO.geoPolygonArea).isEqualTo(DEFAULT_GEO_POLYGON_AREA);
    }

    @Test
    public void createCountryWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Country with an existing ID
        countryDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(countryDTO)
            .when()
            .post("/api/countries")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Country in the database
        var countryDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(countryDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkAcronymIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        countryDTO.acronym = null;

        // Create the Country, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(countryDTO)
            .when()
            .post("/api/countries")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Country in the database
        var countryDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(countryDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        countryDTO.name = null;

        // Create the Country, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(countryDTO)
            .when()
            .post("/api/countries")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Country in the database
        var countryDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(countryDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkContinentIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        countryDTO.continent = null;

        // Create the Country, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(countryDTO)
            .when()
            .post("/api/countries")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Country in the database
        var countryDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(countryDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateCountry() {
        // Initialize the database
        countryDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(countryDTO)
            .when()
            .post("/api/countries")
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
            .get("/api/countries")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the country
        var updatedCountryDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries/{id}", countryDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the country
        updatedCountryDTO.acronym = UPDATED_ACRONYM;
        updatedCountryDTO.name = UPDATED_NAME;
        updatedCountryDTO.continent = UPDATED_CONTINENT;
        updatedCountryDTO.geoPolygonArea = UPDATED_GEO_POLYGON_AREA;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedCountryDTO)
            .when()
            .put("/api/countries/" + countryDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Country in the database
        var countryDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(countryDTOList).hasSize(databaseSizeBeforeUpdate);
        var testCountryDTO = countryDTOList.stream().filter(it -> updatedCountryDTO.id.equals(it.id)).findFirst().get();
        assertThat(testCountryDTO.acronym).isEqualTo(UPDATED_ACRONYM);
        assertThat(testCountryDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testCountryDTO.continent).isEqualTo(UPDATED_CONTINENT);
        assertThat(testCountryDTO.geoPolygonArea).isEqualTo(UPDATED_GEO_POLYGON_AREA);
    }

    @Test
    public void updateNonExistingCountry() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries")
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
            .body(countryDTO)
            .when()
            .put("/api/countries/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Country in the database
        var countryDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(countryDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteCountry() {
        // Initialize the database
        countryDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(countryDTO)
            .when()
            .post("/api/countries")
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
            .get("/api/countries")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the country
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/countries/{id}", countryDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var countryDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(countryDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllCountries() {
        // Initialize the database
        countryDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(countryDTO)
            .when()
            .post("/api/countries")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the countryList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(countryDTO.id.intValue()))
            .body("acronym", hasItem(DEFAULT_ACRONYM))
            .body("name", hasItem(DEFAULT_NAME))
            .body("continent", hasItem(DEFAULT_CONTINENT.toString()))
            .body("geoPolygonArea", hasItem(DEFAULT_GEO_POLYGON_AREA.toString()));
    }

    @Test
    public void getCountry() {
        // Initialize the database
        countryDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(countryDTO)
            .when()
            .post("/api/countries")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the country
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/countries/{id}", countryDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the country
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries/{id}", countryDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(countryDTO.id.intValue()))
            .body("acronym", is(DEFAULT_ACRONYM))
            .body("name", is(DEFAULT_NAME))
            .body("continent", is(DEFAULT_CONTINENT.toString()))
            .body("geoPolygonArea", is(DEFAULT_GEO_POLYGON_AREA.toString()));
    }

    @Test
    public void getNonExistingCountry() {
        // Get the country
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/countries/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
