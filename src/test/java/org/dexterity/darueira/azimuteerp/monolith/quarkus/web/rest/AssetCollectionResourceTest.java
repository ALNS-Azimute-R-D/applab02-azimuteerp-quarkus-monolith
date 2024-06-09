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
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.AssetCollectionDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class AssetCollectionResourceTest {

    private static final TypeRef<AssetCollectionDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<AssetCollectionDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_FILENAME_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FULL_FILENAME_PATH = "BBBBBBBBBB";

    private static final ActivationStatusEnum DEFAULT_ACTIVATION_STATUS = ActivationStatusEnum.INACTIVE;
    private static final ActivationStatusEnum UPDATED_ACTIVATION_STATUS = ActivationStatusEnum.ACTIVE;

    String adminToken;

    AssetCollectionDTO assetCollectionDTO;

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
    public static AssetCollectionDTO createEntity() {
        var assetCollectionDTO = new AssetCollectionDTO();
        assetCollectionDTO.name = DEFAULT_NAME;
        assetCollectionDTO.fullFilenamePath = DEFAULT_FULL_FILENAME_PATH;
        assetCollectionDTO.activationStatus = DEFAULT_ACTIVATION_STATUS;
        return assetCollectionDTO;
    }

    @BeforeEach
    public void initTest() {
        assetCollectionDTO = createEntity();
    }

    @Test
    public void createAssetCollection() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the AssetCollection
        assetCollectionDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assetCollectionDTO)
            .when()
            .post("/api/asset-collections")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the AssetCollection in the database
        var assetCollectionDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(assetCollectionDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testAssetCollectionDTO = assetCollectionDTOList.stream().filter(it -> assetCollectionDTO.id.equals(it.id)).findFirst().get();
        assertThat(testAssetCollectionDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testAssetCollectionDTO.fullFilenamePath).isEqualTo(DEFAULT_FULL_FILENAME_PATH);
        assertThat(testAssetCollectionDTO.activationStatus).isEqualTo(DEFAULT_ACTIVATION_STATUS);
    }

    @Test
    public void createAssetCollectionWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the AssetCollection with an existing ID
        assetCollectionDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assetCollectionDTO)
            .when()
            .post("/api/asset-collections")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the AssetCollection in the database
        var assetCollectionDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(assetCollectionDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        assetCollectionDTO.name = null;

        // Create the AssetCollection, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assetCollectionDTO)
            .when()
            .post("/api/asset-collections")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the AssetCollection in the database
        var assetCollectionDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(assetCollectionDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkActivationStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        assetCollectionDTO.activationStatus = null;

        // Create the AssetCollection, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assetCollectionDTO)
            .when()
            .post("/api/asset-collections")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the AssetCollection in the database
        var assetCollectionDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(assetCollectionDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateAssetCollection() {
        // Initialize the database
        assetCollectionDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assetCollectionDTO)
            .when()
            .post("/api/asset-collections")
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
            .get("/api/asset-collections")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the assetCollection
        var updatedAssetCollectionDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections/{id}", assetCollectionDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the assetCollection
        updatedAssetCollectionDTO.name = UPDATED_NAME;
        updatedAssetCollectionDTO.fullFilenamePath = UPDATED_FULL_FILENAME_PATH;
        updatedAssetCollectionDTO.activationStatus = UPDATED_ACTIVATION_STATUS;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedAssetCollectionDTO)
            .when()
            .put("/api/asset-collections/" + assetCollectionDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the AssetCollection in the database
        var assetCollectionDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(assetCollectionDTOList).hasSize(databaseSizeBeforeUpdate);
        var testAssetCollectionDTO = assetCollectionDTOList
            .stream()
            .filter(it -> updatedAssetCollectionDTO.id.equals(it.id))
            .findFirst()
            .get();
        assertThat(testAssetCollectionDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testAssetCollectionDTO.fullFilenamePath).isEqualTo(UPDATED_FULL_FILENAME_PATH);
        assertThat(testAssetCollectionDTO.activationStatus).isEqualTo(UPDATED_ACTIVATION_STATUS);
    }

    @Test
    public void updateNonExistingAssetCollection() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections")
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
            .body(assetCollectionDTO)
            .when()
            .put("/api/asset-collections/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the AssetCollection in the database
        var assetCollectionDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(assetCollectionDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteAssetCollection() {
        // Initialize the database
        assetCollectionDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assetCollectionDTO)
            .when()
            .post("/api/asset-collections")
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
            .get("/api/asset-collections")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the assetCollection
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/asset-collections/{id}", assetCollectionDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var assetCollectionDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(assetCollectionDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllAssetCollections() {
        // Initialize the database
        assetCollectionDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assetCollectionDTO)
            .when()
            .post("/api/asset-collections")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the assetCollectionList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(assetCollectionDTO.id.intValue()))
            .body("name", hasItem(DEFAULT_NAME))
            .body("fullFilenamePath", hasItem(DEFAULT_FULL_FILENAME_PATH))
            .body("activationStatus", hasItem(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getAssetCollection() {
        // Initialize the database
        assetCollectionDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assetCollectionDTO)
            .when()
            .post("/api/asset-collections")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the assetCollection
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/asset-collections/{id}", assetCollectionDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the assetCollection
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections/{id}", assetCollectionDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(assetCollectionDTO.id.intValue()))
            .body("name", is(DEFAULT_NAME))
            .body("fullFilenamePath", is(DEFAULT_FULL_FILENAME_PATH))
            .body("activationStatus", is(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getNonExistingAssetCollection() {
        // Get the assetCollection
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/asset-collections/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
