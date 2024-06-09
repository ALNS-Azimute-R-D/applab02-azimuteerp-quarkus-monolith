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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import liquibase.Liquibase;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.GenderEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.PersonDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class PersonResourceTest {

    private static final TypeRef<PersonDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<PersonDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_FULLNAME = "AAAAAAAAAA";
    private static final String UPDATED_FULLNAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final GenderEnum DEFAULT_GENDER = GenderEnum.MALE;
    private static final GenderEnum UPDATED_GENDER = GenderEnum.FEMALE;

    private static final String DEFAULT_CODE_BI = "AAAAAAAAAA";
    private static final String UPDATED_CODE_BI = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_NIF = "AAAAAAAAAA";
    private static final String UPDATED_CODE_NIF = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_HOUSE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_HOUSE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBB";

    private static final String DEFAULT_MAIN_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_LAND_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LAND_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_OCCUPATION = "AAAAAAAAAA";
    private static final String UPDATED_OCCUPATION = "BBBBBBBBBB";

    private static final String DEFAULT_PREFERRED_LANGUAGE = "AAAAA";
    private static final String UPDATED_PREFERRED_LANGUAGE = "BBBBB";

    private static final String DEFAULT_USERNAME_IN_O_AUTH_2 = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME_IN_O_AUTH_2 = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID_IN_O_AUTH_2 = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID_IN_O_AUTH_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON = "BBBBBBBBBB";

    private static final ActivationStatusEnum DEFAULT_ACTIVATION_STATUS = ActivationStatusEnum.INACTIVE;
    private static final ActivationStatusEnum UPDATED_ACTIVATION_STATUS = ActivationStatusEnum.ACTIVE;

    private static final byte[] DEFAULT_AVATAR_IMG = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_AVATAR_IMG = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_AVATAR_IMG_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_AVATAR_IMG_CONTENT_TYPE = "image/png";

    String adminToken;

    PersonDTO personDTO;

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
    public static PersonDTO createEntity() {
        var personDTO = new PersonDTO();
        personDTO.firstname = DEFAULT_FIRSTNAME;
        personDTO.lastname = DEFAULT_LASTNAME;
        personDTO.fullname = DEFAULT_FULLNAME;
        personDTO.birthDate = DEFAULT_BIRTH_DATE;
        personDTO.gender = DEFAULT_GENDER;
        personDTO.codeBI = DEFAULT_CODE_BI;
        personDTO.codeNIF = DEFAULT_CODE_NIF;
        personDTO.streetAddress = DEFAULT_STREET_ADDRESS;
        personDTO.houseNumber = DEFAULT_HOUSE_NUMBER;
        personDTO.locationName = DEFAULT_LOCATION_NAME;
        personDTO.postalCode = DEFAULT_POSTAL_CODE;
        personDTO.mainEmail = DEFAULT_MAIN_EMAIL;
        personDTO.landPhoneNumber = DEFAULT_LAND_PHONE_NUMBER;
        personDTO.mobilePhoneNumber = DEFAULT_MOBILE_PHONE_NUMBER;
        personDTO.occupation = DEFAULT_OCCUPATION;
        personDTO.preferredLanguage = DEFAULT_PREFERRED_LANGUAGE;
        personDTO.usernameInOAuth2 = DEFAULT_USERNAME_IN_O_AUTH_2;
        personDTO.userIdInOAuth2 = DEFAULT_USER_ID_IN_O_AUTH_2;
        personDTO.customAttributesDetailsJSON = DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON;
        personDTO.activationStatus = DEFAULT_ACTIVATION_STATUS;
        personDTO.avatarImg = DEFAULT_AVATAR_IMG;
        return personDTO;
    }

    @BeforeEach
    public void initTest() {
        personDTO = createEntity();
    }

    @Test
    public void createPerson() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Person
        personDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testPersonDTO = personDTOList.stream().filter(it -> personDTO.id.equals(it.id)).findFirst().get();
        assertThat(testPersonDTO.firstname).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testPersonDTO.lastname).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testPersonDTO.fullname).isEqualTo(DEFAULT_FULLNAME);
        assertThat(testPersonDTO.birthDate).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testPersonDTO.gender).isEqualTo(DEFAULT_GENDER);
        assertThat(testPersonDTO.codeBI).isEqualTo(DEFAULT_CODE_BI);
        assertThat(testPersonDTO.codeNIF).isEqualTo(DEFAULT_CODE_NIF);
        assertThat(testPersonDTO.streetAddress).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testPersonDTO.houseNumber).isEqualTo(DEFAULT_HOUSE_NUMBER);
        assertThat(testPersonDTO.locationName).isEqualTo(DEFAULT_LOCATION_NAME);
        assertThat(testPersonDTO.postalCode).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testPersonDTO.mainEmail).isEqualTo(DEFAULT_MAIN_EMAIL);
        assertThat(testPersonDTO.landPhoneNumber).isEqualTo(DEFAULT_LAND_PHONE_NUMBER);
        assertThat(testPersonDTO.mobilePhoneNumber).isEqualTo(DEFAULT_MOBILE_PHONE_NUMBER);
        assertThat(testPersonDTO.occupation).isEqualTo(DEFAULT_OCCUPATION);
        assertThat(testPersonDTO.preferredLanguage).isEqualTo(DEFAULT_PREFERRED_LANGUAGE);
        assertThat(testPersonDTO.usernameInOAuth2).isEqualTo(DEFAULT_USERNAME_IN_O_AUTH_2);
        assertThat(testPersonDTO.userIdInOAuth2).isEqualTo(DEFAULT_USER_ID_IN_O_AUTH_2);
        assertThat(testPersonDTO.customAttributesDetailsJSON).isEqualTo(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON);
        assertThat(testPersonDTO.activationStatus).isEqualTo(DEFAULT_ACTIVATION_STATUS);
        assertThat(testPersonDTO.avatarImg).isEqualTo(DEFAULT_AVATAR_IMG);
    }

    @Test
    public void createPersonWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Person with an existing ID
        personDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkFirstnameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        personDTO.firstname = null;

        // Create the Person, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkLastnameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        personDTO.lastname = null;

        // Create the Person, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkBirthDateIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        personDTO.birthDate = null;

        // Create the Person, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkGenderIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        personDTO.gender = null;

        // Create the Person, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStreetAddressIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        personDTO.streetAddress = null;

        // Create the Person, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPostalCodeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        personDTO.postalCode = null;

        // Create the Person, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkMainEmailIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        personDTO.mainEmail = null;

        // Create the Person, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkActivationStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        personDTO.activationStatus = null;

        // Create the Person, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updatePerson() {
        // Initialize the database
        personDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
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
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the person
        var updatedPersonDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people/{id}", personDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the person
        updatedPersonDTO.firstname = UPDATED_FIRSTNAME;
        updatedPersonDTO.lastname = UPDATED_LASTNAME;
        updatedPersonDTO.fullname = UPDATED_FULLNAME;
        updatedPersonDTO.birthDate = UPDATED_BIRTH_DATE;
        updatedPersonDTO.gender = UPDATED_GENDER;
        updatedPersonDTO.codeBI = UPDATED_CODE_BI;
        updatedPersonDTO.codeNIF = UPDATED_CODE_NIF;
        updatedPersonDTO.streetAddress = UPDATED_STREET_ADDRESS;
        updatedPersonDTO.houseNumber = UPDATED_HOUSE_NUMBER;
        updatedPersonDTO.locationName = UPDATED_LOCATION_NAME;
        updatedPersonDTO.postalCode = UPDATED_POSTAL_CODE;
        updatedPersonDTO.mainEmail = UPDATED_MAIN_EMAIL;
        updatedPersonDTO.landPhoneNumber = UPDATED_LAND_PHONE_NUMBER;
        updatedPersonDTO.mobilePhoneNumber = UPDATED_MOBILE_PHONE_NUMBER;
        updatedPersonDTO.occupation = UPDATED_OCCUPATION;
        updatedPersonDTO.preferredLanguage = UPDATED_PREFERRED_LANGUAGE;
        updatedPersonDTO.usernameInOAuth2 = UPDATED_USERNAME_IN_O_AUTH_2;
        updatedPersonDTO.userIdInOAuth2 = UPDATED_USER_ID_IN_O_AUTH_2;
        updatedPersonDTO.customAttributesDetailsJSON = UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON;
        updatedPersonDTO.activationStatus = UPDATED_ACTIVATION_STATUS;
        updatedPersonDTO.avatarImg = UPDATED_AVATAR_IMG;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedPersonDTO)
            .when()
            .put("/api/people/" + personDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeUpdate);
        var testPersonDTO = personDTOList.stream().filter(it -> updatedPersonDTO.id.equals(it.id)).findFirst().get();
        assertThat(testPersonDTO.firstname).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testPersonDTO.lastname).isEqualTo(UPDATED_LASTNAME);
        assertThat(testPersonDTO.fullname).isEqualTo(UPDATED_FULLNAME);
        assertThat(testPersonDTO.birthDate).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testPersonDTO.gender).isEqualTo(UPDATED_GENDER);
        assertThat(testPersonDTO.codeBI).isEqualTo(UPDATED_CODE_BI);
        assertThat(testPersonDTO.codeNIF).isEqualTo(UPDATED_CODE_NIF);
        assertThat(testPersonDTO.streetAddress).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testPersonDTO.houseNumber).isEqualTo(UPDATED_HOUSE_NUMBER);
        assertThat(testPersonDTO.locationName).isEqualTo(UPDATED_LOCATION_NAME);
        assertThat(testPersonDTO.postalCode).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testPersonDTO.mainEmail).isEqualTo(UPDATED_MAIN_EMAIL);
        assertThat(testPersonDTO.landPhoneNumber).isEqualTo(UPDATED_LAND_PHONE_NUMBER);
        assertThat(testPersonDTO.mobilePhoneNumber).isEqualTo(UPDATED_MOBILE_PHONE_NUMBER);
        assertThat(testPersonDTO.occupation).isEqualTo(UPDATED_OCCUPATION);
        assertThat(testPersonDTO.preferredLanguage).isEqualTo(UPDATED_PREFERRED_LANGUAGE);
        assertThat(testPersonDTO.usernameInOAuth2).isEqualTo(UPDATED_USERNAME_IN_O_AUTH_2);
        assertThat(testPersonDTO.userIdInOAuth2).isEqualTo(UPDATED_USER_ID_IN_O_AUTH_2);
        assertThat(testPersonDTO.customAttributesDetailsJSON).isEqualTo(UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON);
        assertThat(testPersonDTO.activationStatus).isEqualTo(UPDATED_ACTIVATION_STATUS);
        assertThat(testPersonDTO.avatarImg).isEqualTo(UPDATED_AVATAR_IMG);
    }

    @Test
    public void updateNonExistingPerson() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
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
            .body(personDTO)
            .when()
            .put("/api/people/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deletePerson() {
        // Initialize the database
        personDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
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
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the person
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/people/{id}", personDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllPeople() {
        // Initialize the database
        personDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the personList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(personDTO.id.intValue()))
            .body("firstname", hasItem(DEFAULT_FIRSTNAME))
            .body("lastname", hasItem(DEFAULT_LASTNAME))
            .body("fullname", hasItem(DEFAULT_FULLNAME))
            .body("birthDate", hasItem(TestUtil.formatDateTime(DEFAULT_BIRTH_DATE)))
            .body("gender", hasItem(DEFAULT_GENDER.toString()))
            .body("codeBI", hasItem(DEFAULT_CODE_BI))
            .body("codeNIF", hasItem(DEFAULT_CODE_NIF))
            .body("streetAddress", hasItem(DEFAULT_STREET_ADDRESS))
            .body("houseNumber", hasItem(DEFAULT_HOUSE_NUMBER))
            .body("locationName", hasItem(DEFAULT_LOCATION_NAME))
            .body("postalCode", hasItem(DEFAULT_POSTAL_CODE))
            .body("mainEmail", hasItem(DEFAULT_MAIN_EMAIL))
            .body("landPhoneNumber", hasItem(DEFAULT_LAND_PHONE_NUMBER))
            .body("mobilePhoneNumber", hasItem(DEFAULT_MOBILE_PHONE_NUMBER))
            .body("occupation", hasItem(DEFAULT_OCCUPATION))
            .body("preferredLanguage", hasItem(DEFAULT_PREFERRED_LANGUAGE))
            .body("usernameInOAuth2", hasItem(DEFAULT_USERNAME_IN_O_AUTH_2))
            .body("userIdInOAuth2", hasItem(DEFAULT_USER_ID_IN_O_AUTH_2))
            .body("customAttributesDetailsJSON", hasItem(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON))
            .body("activationStatus", hasItem(DEFAULT_ACTIVATION_STATUS.toString()))
            .body("avatarImg", hasItem(DEFAULT_AVATAR_IMG.toString()));
    }

    @Test
    public void getPerson() {
        // Initialize the database
        personDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the person
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/people/{id}", personDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the person
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people/{id}", personDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(personDTO.id.intValue()))
            .body("firstname", is(DEFAULT_FIRSTNAME))
            .body("lastname", is(DEFAULT_LASTNAME))
            .body("fullname", is(DEFAULT_FULLNAME))
            .body("birthDate", is(TestUtil.formatDateTime(DEFAULT_BIRTH_DATE)))
            .body("gender", is(DEFAULT_GENDER.toString()))
            .body("codeBI", is(DEFAULT_CODE_BI))
            .body("codeNIF", is(DEFAULT_CODE_NIF))
            .body("streetAddress", is(DEFAULT_STREET_ADDRESS))
            .body("houseNumber", is(DEFAULT_HOUSE_NUMBER))
            .body("locationName", is(DEFAULT_LOCATION_NAME))
            .body("postalCode", is(DEFAULT_POSTAL_CODE))
            .body("mainEmail", is(DEFAULT_MAIN_EMAIL))
            .body("landPhoneNumber", is(DEFAULT_LAND_PHONE_NUMBER))
            .body("mobilePhoneNumber", is(DEFAULT_MOBILE_PHONE_NUMBER))
            .body("occupation", is(DEFAULT_OCCUPATION))
            .body("preferredLanguage", is(DEFAULT_PREFERRED_LANGUAGE))
            .body("usernameInOAuth2", is(DEFAULT_USERNAME_IN_O_AUTH_2))
            .body("userIdInOAuth2", is(DEFAULT_USER_ID_IN_O_AUTH_2))
            .body("customAttributesDetailsJSON", is(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON))
            .body("activationStatus", is(DEFAULT_ACTIVATION_STATUS.toString()))
            .body("avatarImg", is(DEFAULT_AVATAR_IMG.toString()));
    }

    @Test
    public void getNonExistingPerson() {
        // Get the person
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
