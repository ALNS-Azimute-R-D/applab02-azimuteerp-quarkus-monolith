package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import de.org.dexterity.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.SupplierDTO;
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
public class SupplierResourceTest {

    private static final TypeRef<SupplierDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<SupplierDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_ACRONYM = "AAAAAAAAAA";
    private static final String UPDATED_ACRONYM = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REPRESENTATIVE_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPRESENTATIVE_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REPRESENTATIVE_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPRESENTATIVE_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_HOUSE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_HOUSE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_REGION = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_WEB_PAGE = "AAAAAAAAAA";
    private static final String UPDATED_WEB_PAGE = "BBBBBBBBBB";

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

    SupplierDTO supplierDTO;

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
    public static SupplierDTO createEntity() {
        var supplierDTO = new SupplierDTO();
        supplierDTO.acronym = DEFAULT_ACRONYM;
        supplierDTO.companyName = DEFAULT_COMPANY_NAME;
        supplierDTO.representativeLastName = DEFAULT_REPRESENTATIVE_LAST_NAME;
        supplierDTO.representativeFirstName = DEFAULT_REPRESENTATIVE_FIRST_NAME;
        supplierDTO.jobTitle = DEFAULT_JOB_TITLE;
        supplierDTO.streetAddress = DEFAULT_STREET_ADDRESS;
        supplierDTO.houseNumber = DEFAULT_HOUSE_NUMBER;
        supplierDTO.locationName = DEFAULT_LOCATION_NAME;
        supplierDTO.city = DEFAULT_CITY;
        supplierDTO.stateProvince = DEFAULT_STATE_PROVINCE;
        supplierDTO.zipPostalCode = DEFAULT_ZIP_POSTAL_CODE;
        supplierDTO.countryRegion = DEFAULT_COUNTRY_REGION;
        supplierDTO.webPage = DEFAULT_WEB_PAGE;
        supplierDTO.pointLocation = DEFAULT_POINT_LOCATION;
        supplierDTO.mainEmail = DEFAULT_MAIN_EMAIL;
        supplierDTO.landPhoneNumber = DEFAULT_LAND_PHONE_NUMBER;
        supplierDTO.mobilePhoneNumber = DEFAULT_MOBILE_PHONE_NUMBER;
        supplierDTO.faxNumber = DEFAULT_FAX_NUMBER;
        supplierDTO.extraDetails = DEFAULT_EXTRA_DETAILS;
        return supplierDTO;
    }

    @BeforeEach
    public void initTest() {
        supplierDTO = createEntity();
    }

    @Test
    public void createSupplier() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Supplier
        supplierDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(supplierDTO)
            .when()
            .post("/api/suppliers")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Supplier in the database
        var supplierDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(supplierDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testSupplierDTO = supplierDTOList.stream().filter(it -> supplierDTO.id.equals(it.id)).findFirst().get();
        assertThat(testSupplierDTO.acronym).isEqualTo(DEFAULT_ACRONYM);
        assertThat(testSupplierDTO.companyName).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testSupplierDTO.representativeLastName).isEqualTo(DEFAULT_REPRESENTATIVE_LAST_NAME);
        assertThat(testSupplierDTO.representativeFirstName).isEqualTo(DEFAULT_REPRESENTATIVE_FIRST_NAME);
        assertThat(testSupplierDTO.jobTitle).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testSupplierDTO.streetAddress).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testSupplierDTO.houseNumber).isEqualTo(DEFAULT_HOUSE_NUMBER);
        assertThat(testSupplierDTO.locationName).isEqualTo(DEFAULT_LOCATION_NAME);
        assertThat(testSupplierDTO.city).isEqualTo(DEFAULT_CITY);
        assertThat(testSupplierDTO.stateProvince).isEqualTo(DEFAULT_STATE_PROVINCE);
        assertThat(testSupplierDTO.zipPostalCode).isEqualTo(DEFAULT_ZIP_POSTAL_CODE);
        assertThat(testSupplierDTO.countryRegion).isEqualTo(DEFAULT_COUNTRY_REGION);
        assertThat(testSupplierDTO.webPage).isEqualTo(DEFAULT_WEB_PAGE);
        assertThat(testSupplierDTO.pointLocation).isEqualTo(DEFAULT_POINT_LOCATION);
        assertThat(testSupplierDTO.mainEmail).isEqualTo(DEFAULT_MAIN_EMAIL);
        assertThat(testSupplierDTO.landPhoneNumber).isEqualTo(DEFAULT_LAND_PHONE_NUMBER);
        assertThat(testSupplierDTO.mobilePhoneNumber).isEqualTo(DEFAULT_MOBILE_PHONE_NUMBER);
        assertThat(testSupplierDTO.faxNumber).isEqualTo(DEFAULT_FAX_NUMBER);
        assertThat(testSupplierDTO.extraDetails).isEqualTo(DEFAULT_EXTRA_DETAILS);
    }

    @Test
    public void createSupplierWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Supplier with an existing ID
        supplierDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(supplierDTO)
            .when()
            .post("/api/suppliers")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Supplier in the database
        var supplierDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(supplierDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkAcronymIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        supplierDTO.acronym = null;

        // Create the Supplier, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(supplierDTO)
            .when()
            .post("/api/suppliers")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Supplier in the database
        var supplierDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(supplierDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCompanyNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        supplierDTO.companyName = null;

        // Create the Supplier, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(supplierDTO)
            .when()
            .post("/api/suppliers")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Supplier in the database
        var supplierDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(supplierDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStreetAddressIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        supplierDTO.streetAddress = null;

        // Create the Supplier, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(supplierDTO)
            .when()
            .post("/api/suppliers")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Supplier in the database
        var supplierDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(supplierDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateSupplier() {
        // Initialize the database
        supplierDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(supplierDTO)
            .when()
            .post("/api/suppliers")
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
            .get("/api/suppliers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the supplier
        var updatedSupplierDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers/{id}", supplierDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the supplier
        updatedSupplierDTO.acronym = UPDATED_ACRONYM;
        updatedSupplierDTO.companyName = UPDATED_COMPANY_NAME;
        updatedSupplierDTO.representativeLastName = UPDATED_REPRESENTATIVE_LAST_NAME;
        updatedSupplierDTO.representativeFirstName = UPDATED_REPRESENTATIVE_FIRST_NAME;
        updatedSupplierDTO.jobTitle = UPDATED_JOB_TITLE;
        updatedSupplierDTO.streetAddress = UPDATED_STREET_ADDRESS;
        updatedSupplierDTO.houseNumber = UPDATED_HOUSE_NUMBER;
        updatedSupplierDTO.locationName = UPDATED_LOCATION_NAME;
        updatedSupplierDTO.city = UPDATED_CITY;
        updatedSupplierDTO.stateProvince = UPDATED_STATE_PROVINCE;
        updatedSupplierDTO.zipPostalCode = UPDATED_ZIP_POSTAL_CODE;
        updatedSupplierDTO.countryRegion = UPDATED_COUNTRY_REGION;
        updatedSupplierDTO.webPage = UPDATED_WEB_PAGE;
        updatedSupplierDTO.pointLocation = UPDATED_POINT_LOCATION;
        updatedSupplierDTO.mainEmail = UPDATED_MAIN_EMAIL;
        updatedSupplierDTO.landPhoneNumber = UPDATED_LAND_PHONE_NUMBER;
        updatedSupplierDTO.mobilePhoneNumber = UPDATED_MOBILE_PHONE_NUMBER;
        updatedSupplierDTO.faxNumber = UPDATED_FAX_NUMBER;
        updatedSupplierDTO.extraDetails = UPDATED_EXTRA_DETAILS;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedSupplierDTO)
            .when()
            .put("/api/suppliers/" + supplierDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Supplier in the database
        var supplierDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(supplierDTOList).hasSize(databaseSizeBeforeUpdate);
        var testSupplierDTO = supplierDTOList.stream().filter(it -> updatedSupplierDTO.id.equals(it.id)).findFirst().get();
        assertThat(testSupplierDTO.acronym).isEqualTo(UPDATED_ACRONYM);
        assertThat(testSupplierDTO.companyName).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testSupplierDTO.representativeLastName).isEqualTo(UPDATED_REPRESENTATIVE_LAST_NAME);
        assertThat(testSupplierDTO.representativeFirstName).isEqualTo(UPDATED_REPRESENTATIVE_FIRST_NAME);
        assertThat(testSupplierDTO.jobTitle).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testSupplierDTO.streetAddress).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testSupplierDTO.houseNumber).isEqualTo(UPDATED_HOUSE_NUMBER);
        assertThat(testSupplierDTO.locationName).isEqualTo(UPDATED_LOCATION_NAME);
        assertThat(testSupplierDTO.city).isEqualTo(UPDATED_CITY);
        assertThat(testSupplierDTO.stateProvince).isEqualTo(UPDATED_STATE_PROVINCE);
        assertThat(testSupplierDTO.zipPostalCode).isEqualTo(UPDATED_ZIP_POSTAL_CODE);
        assertThat(testSupplierDTO.countryRegion).isEqualTo(UPDATED_COUNTRY_REGION);
        assertThat(testSupplierDTO.webPage).isEqualTo(UPDATED_WEB_PAGE);
        assertThat(testSupplierDTO.pointLocation).isEqualTo(UPDATED_POINT_LOCATION);
        assertThat(testSupplierDTO.mainEmail).isEqualTo(UPDATED_MAIN_EMAIL);
        assertThat(testSupplierDTO.landPhoneNumber).isEqualTo(UPDATED_LAND_PHONE_NUMBER);
        assertThat(testSupplierDTO.mobilePhoneNumber).isEqualTo(UPDATED_MOBILE_PHONE_NUMBER);
        assertThat(testSupplierDTO.faxNumber).isEqualTo(UPDATED_FAX_NUMBER);
        assertThat(testSupplierDTO.extraDetails).isEqualTo(UPDATED_EXTRA_DETAILS);
    }

    @Test
    public void updateNonExistingSupplier() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers")
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
            .body(supplierDTO)
            .when()
            .put("/api/suppliers/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Supplier in the database
        var supplierDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(supplierDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteSupplier() {
        // Initialize the database
        supplierDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(supplierDTO)
            .when()
            .post("/api/suppliers")
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
            .get("/api/suppliers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the supplier
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/suppliers/{id}", supplierDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var supplierDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(supplierDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllSuppliers() {
        // Initialize the database
        supplierDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(supplierDTO)
            .when()
            .post("/api/suppliers")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the supplierList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(supplierDTO.id.intValue()))
            .body("acronym", hasItem(DEFAULT_ACRONYM))
            .body("companyName", hasItem(DEFAULT_COMPANY_NAME))
            .body("representativeLastName", hasItem(DEFAULT_REPRESENTATIVE_LAST_NAME))
            .body("representativeFirstName", hasItem(DEFAULT_REPRESENTATIVE_FIRST_NAME))
            .body("jobTitle", hasItem(DEFAULT_JOB_TITLE))
            .body("streetAddress", hasItem(DEFAULT_STREET_ADDRESS))
            .body("houseNumber", hasItem(DEFAULT_HOUSE_NUMBER))
            .body("locationName", hasItem(DEFAULT_LOCATION_NAME))
            .body("city", hasItem(DEFAULT_CITY))
            .body("stateProvince", hasItem(DEFAULT_STATE_PROVINCE))
            .body("zipPostalCode", hasItem(DEFAULT_ZIP_POSTAL_CODE))
            .body("countryRegion", hasItem(DEFAULT_COUNTRY_REGION))
            .body("webPage", hasItem(DEFAULT_WEB_PAGE.toString()))
            .body("pointLocation", hasItem(DEFAULT_POINT_LOCATION.toString()))
            .body("mainEmail", hasItem(DEFAULT_MAIN_EMAIL))
            .body("landPhoneNumber", hasItem(DEFAULT_LAND_PHONE_NUMBER))
            .body("mobilePhoneNumber", hasItem(DEFAULT_MOBILE_PHONE_NUMBER))
            .body("faxNumber", hasItem(DEFAULT_FAX_NUMBER))
            .body("extraDetails", hasItem(DEFAULT_EXTRA_DETAILS.toString()));
    }

    @Test
    public void getSupplier() {
        // Initialize the database
        supplierDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(supplierDTO)
            .when()
            .post("/api/suppliers")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the supplier
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/suppliers/{id}", supplierDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the supplier
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers/{id}", supplierDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(supplierDTO.id.intValue()))
            .body("acronym", is(DEFAULT_ACRONYM))
            .body("companyName", is(DEFAULT_COMPANY_NAME))
            .body("representativeLastName", is(DEFAULT_REPRESENTATIVE_LAST_NAME))
            .body("representativeFirstName", is(DEFAULT_REPRESENTATIVE_FIRST_NAME))
            .body("jobTitle", is(DEFAULT_JOB_TITLE))
            .body("streetAddress", is(DEFAULT_STREET_ADDRESS))
            .body("houseNumber", is(DEFAULT_HOUSE_NUMBER))
            .body("locationName", is(DEFAULT_LOCATION_NAME))
            .body("city", is(DEFAULT_CITY))
            .body("stateProvince", is(DEFAULT_STATE_PROVINCE))
            .body("zipPostalCode", is(DEFAULT_ZIP_POSTAL_CODE))
            .body("countryRegion", is(DEFAULT_COUNTRY_REGION))
            .body("webPage", is(DEFAULT_WEB_PAGE.toString()))
            .body("pointLocation", is(DEFAULT_POINT_LOCATION.toString()))
            .body("mainEmail", is(DEFAULT_MAIN_EMAIL))
            .body("landPhoneNumber", is(DEFAULT_LAND_PHONE_NUMBER))
            .body("mobilePhoneNumber", is(DEFAULT_MOBILE_PHONE_NUMBER))
            .body("faxNumber", is(DEFAULT_FAX_NUMBER))
            .body("extraDetails", is(DEFAULT_EXTRA_DETAILS.toString()));
    }

    @Test
    public void getNonExistingSupplier() {
        // Get the supplier
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/suppliers/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
