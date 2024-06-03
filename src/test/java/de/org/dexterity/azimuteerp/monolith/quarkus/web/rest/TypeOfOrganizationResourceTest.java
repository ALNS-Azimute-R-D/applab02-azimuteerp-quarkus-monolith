package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import de.org.dexterity.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.TypeOfOrganizationDTO;
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
public class TypeOfOrganizationResourceTest {

    private static final TypeRef<TypeOfOrganizationDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<TypeOfOrganizationDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_ACRONYM = "AAAAAAAAAA";
    private static final String UPDATED_ACRONYM = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_HANDLER_CLAZZ = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_HANDLER_CLAZZ = "BBBBBBBBBB";

    String adminToken;

    TypeOfOrganizationDTO typeOfOrganizationDTO;

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
    public static TypeOfOrganizationDTO createEntity() {
        var typeOfOrganizationDTO = new TypeOfOrganizationDTO();
        typeOfOrganizationDTO.acronym = DEFAULT_ACRONYM;
        typeOfOrganizationDTO.name = DEFAULT_NAME;
        typeOfOrganizationDTO.description = DEFAULT_DESCRIPTION;
        typeOfOrganizationDTO.businessHandlerClazz = DEFAULT_BUSINESS_HANDLER_CLAZZ;
        return typeOfOrganizationDTO;
    }

    @BeforeEach
    public void initTest() {
        typeOfOrganizationDTO = createEntity();
    }

    @Test
    public void createTypeOfOrganization() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the TypeOfOrganization
        typeOfOrganizationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(typeOfOrganizationDTO)
            .when()
            .post("/api/type-of-organizations")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the TypeOfOrganization in the database
        var typeOfOrganizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(typeOfOrganizationDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testTypeOfOrganizationDTO = typeOfOrganizationDTOList
            .stream()
            .filter(it -> typeOfOrganizationDTO.id.equals(it.id))
            .findFirst()
            .get();
        assertThat(testTypeOfOrganizationDTO.acronym).isEqualTo(DEFAULT_ACRONYM);
        assertThat(testTypeOfOrganizationDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testTypeOfOrganizationDTO.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTypeOfOrganizationDTO.businessHandlerClazz).isEqualTo(DEFAULT_BUSINESS_HANDLER_CLAZZ);
    }

    @Test
    public void createTypeOfOrganizationWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the TypeOfOrganization with an existing ID
        typeOfOrganizationDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(typeOfOrganizationDTO)
            .when()
            .post("/api/type-of-organizations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the TypeOfOrganization in the database
        var typeOfOrganizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(typeOfOrganizationDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkAcronymIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        typeOfOrganizationDTO.acronym = null;

        // Create the TypeOfOrganization, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(typeOfOrganizationDTO)
            .when()
            .post("/api/type-of-organizations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the TypeOfOrganization in the database
        var typeOfOrganizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(typeOfOrganizationDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        typeOfOrganizationDTO.name = null;

        // Create the TypeOfOrganization, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(typeOfOrganizationDTO)
            .when()
            .post("/api/type-of-organizations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the TypeOfOrganization in the database
        var typeOfOrganizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(typeOfOrganizationDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateTypeOfOrganization() {
        // Initialize the database
        typeOfOrganizationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(typeOfOrganizationDTO)
            .when()
            .post("/api/type-of-organizations")
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
            .get("/api/type-of-organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the typeOfOrganization
        var updatedTypeOfOrganizationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations/{id}", typeOfOrganizationDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the typeOfOrganization
        updatedTypeOfOrganizationDTO.acronym = UPDATED_ACRONYM;
        updatedTypeOfOrganizationDTO.name = UPDATED_NAME;
        updatedTypeOfOrganizationDTO.description = UPDATED_DESCRIPTION;
        updatedTypeOfOrganizationDTO.businessHandlerClazz = UPDATED_BUSINESS_HANDLER_CLAZZ;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedTypeOfOrganizationDTO)
            .when()
            .put("/api/type-of-organizations/" + typeOfOrganizationDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the TypeOfOrganization in the database
        var typeOfOrganizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(typeOfOrganizationDTOList).hasSize(databaseSizeBeforeUpdate);
        var testTypeOfOrganizationDTO = typeOfOrganizationDTOList
            .stream()
            .filter(it -> updatedTypeOfOrganizationDTO.id.equals(it.id))
            .findFirst()
            .get();
        assertThat(testTypeOfOrganizationDTO.acronym).isEqualTo(UPDATED_ACRONYM);
        assertThat(testTypeOfOrganizationDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testTypeOfOrganizationDTO.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTypeOfOrganizationDTO.businessHandlerClazz).isEqualTo(UPDATED_BUSINESS_HANDLER_CLAZZ);
    }

    @Test
    public void updateNonExistingTypeOfOrganization() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations")
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
            .body(typeOfOrganizationDTO)
            .when()
            .put("/api/type-of-organizations/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the TypeOfOrganization in the database
        var typeOfOrganizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(typeOfOrganizationDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteTypeOfOrganization() {
        // Initialize the database
        typeOfOrganizationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(typeOfOrganizationDTO)
            .when()
            .post("/api/type-of-organizations")
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
            .get("/api/type-of-organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the typeOfOrganization
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/type-of-organizations/{id}", typeOfOrganizationDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var typeOfOrganizationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(typeOfOrganizationDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllTypeOfOrganizations() {
        // Initialize the database
        typeOfOrganizationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(typeOfOrganizationDTO)
            .when()
            .post("/api/type-of-organizations")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the typeOfOrganizationList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(typeOfOrganizationDTO.id.intValue()))
            .body("acronym", hasItem(DEFAULT_ACRONYM))
            .body("name", hasItem(DEFAULT_NAME))
            .body("description", hasItem(DEFAULT_DESCRIPTION.toString()))
            .body("businessHandlerClazz", hasItem(DEFAULT_BUSINESS_HANDLER_CLAZZ));
    }

    @Test
    public void getTypeOfOrganization() {
        // Initialize the database
        typeOfOrganizationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(typeOfOrganizationDTO)
            .when()
            .post("/api/type-of-organizations")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the typeOfOrganization
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/type-of-organizations/{id}", typeOfOrganizationDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the typeOfOrganization
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations/{id}", typeOfOrganizationDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(typeOfOrganizationDTO.id.intValue()))
            .body("acronym", is(DEFAULT_ACRONYM))
            .body("name", is(DEFAULT_NAME))
            .body("description", is(DEFAULT_DESCRIPTION.toString()))
            .body("businessHandlerClazz", is(DEFAULT_BUSINESS_HANDLER_CLAZZ));
    }

    @Test
    public void getNonExistingTypeOfOrganization() {
        // Get the typeOfOrganization
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/type-of-organizations/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
