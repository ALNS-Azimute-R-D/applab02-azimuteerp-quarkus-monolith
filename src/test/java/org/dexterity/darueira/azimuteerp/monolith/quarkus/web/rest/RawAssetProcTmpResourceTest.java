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
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.StatusRawProcessingEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.RawAssetProcTmpDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class RawAssetProcTmpResourceTest {

    private static final TypeRef<RawAssetProcTmpDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<RawAssetProcTmpDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final StatusRawProcessingEnum DEFAULT_STATUS_RAW_PROCESSING = StatusRawProcessingEnum.UPLOADING;
    private static final StatusRawProcessingEnum UPDATED_STATUS_RAW_PROCESSING = StatusRawProcessingEnum.UPLOADED;

    private static final String DEFAULT_FULL_FILENAME_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FULL_FILENAME_PATH = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ASSET_RAW_CONTENT_AS_BLOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ASSET_RAW_CONTENT_AS_BLOB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ASSET_RAW_CONTENT_AS_BLOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ASSET_RAW_CONTENT_AS_BLOB_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON = "BBBBBBBBBB";

    String adminToken;

    RawAssetProcTmpDTO rawAssetProcTmpDTO;

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
    public static RawAssetProcTmpDTO createEntity() {
        var rawAssetProcTmpDTO = new RawAssetProcTmpDTO();
        rawAssetProcTmpDTO.name = DEFAULT_NAME;
        rawAssetProcTmpDTO.statusRawProcessing = DEFAULT_STATUS_RAW_PROCESSING;
        rawAssetProcTmpDTO.fullFilenamePath = DEFAULT_FULL_FILENAME_PATH;
        rawAssetProcTmpDTO.assetRawContentAsBlob = DEFAULT_ASSET_RAW_CONTENT_AS_BLOB;
        rawAssetProcTmpDTO.customAttributesDetailsJSON = DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON;
        return rawAssetProcTmpDTO;
    }

    @BeforeEach
    public void initTest() {
        rawAssetProcTmpDTO = createEntity();
    }

    @Test
    public void createRawAssetProcTmp() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the RawAssetProcTmp
        rawAssetProcTmpDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(rawAssetProcTmpDTO)
            .when()
            .post("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the RawAssetProcTmp in the database
        var rawAssetProcTmpDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(rawAssetProcTmpDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testRawAssetProcTmpDTO = rawAssetProcTmpDTOList.stream().filter(it -> rawAssetProcTmpDTO.id.equals(it.id)).findFirst().get();
        assertThat(testRawAssetProcTmpDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testRawAssetProcTmpDTO.statusRawProcessing).isEqualTo(DEFAULT_STATUS_RAW_PROCESSING);
        assertThat(testRawAssetProcTmpDTO.fullFilenamePath).isEqualTo(DEFAULT_FULL_FILENAME_PATH);
        assertThat(testRawAssetProcTmpDTO.assetRawContentAsBlob).isEqualTo(DEFAULT_ASSET_RAW_CONTENT_AS_BLOB);
        assertThat(testRawAssetProcTmpDTO.customAttributesDetailsJSON).isEqualTo(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON);
    }

    @Test
    public void createRawAssetProcTmpWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the RawAssetProcTmp with an existing ID
        rawAssetProcTmpDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(rawAssetProcTmpDTO)
            .when()
            .post("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the RawAssetProcTmp in the database
        var rawAssetProcTmpDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(rawAssetProcTmpDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        rawAssetProcTmpDTO.name = null;

        // Create the RawAssetProcTmp, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(rawAssetProcTmpDTO)
            .when()
            .post("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the RawAssetProcTmp in the database
        var rawAssetProcTmpDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(rawAssetProcTmpDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateRawAssetProcTmp() {
        // Initialize the database
        rawAssetProcTmpDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(rawAssetProcTmpDTO)
            .when()
            .post("/api/raw-asset-proc-tmps")
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
            .get("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the rawAssetProcTmp
        var updatedRawAssetProcTmpDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/raw-asset-proc-tmps/{id}", rawAssetProcTmpDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the rawAssetProcTmp
        updatedRawAssetProcTmpDTO.name = UPDATED_NAME;
        updatedRawAssetProcTmpDTO.statusRawProcessing = UPDATED_STATUS_RAW_PROCESSING;
        updatedRawAssetProcTmpDTO.fullFilenamePath = UPDATED_FULL_FILENAME_PATH;
        updatedRawAssetProcTmpDTO.assetRawContentAsBlob = UPDATED_ASSET_RAW_CONTENT_AS_BLOB;
        updatedRawAssetProcTmpDTO.customAttributesDetailsJSON = UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedRawAssetProcTmpDTO)
            .when()
            .put("/api/raw-asset-proc-tmps/" + rawAssetProcTmpDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the RawAssetProcTmp in the database
        var rawAssetProcTmpDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(rawAssetProcTmpDTOList).hasSize(databaseSizeBeforeUpdate);
        var testRawAssetProcTmpDTO = rawAssetProcTmpDTOList
            .stream()
            .filter(it -> updatedRawAssetProcTmpDTO.id.equals(it.id))
            .findFirst()
            .get();
        assertThat(testRawAssetProcTmpDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testRawAssetProcTmpDTO.statusRawProcessing).isEqualTo(UPDATED_STATUS_RAW_PROCESSING);
        assertThat(testRawAssetProcTmpDTO.fullFilenamePath).isEqualTo(UPDATED_FULL_FILENAME_PATH);
        assertThat(testRawAssetProcTmpDTO.assetRawContentAsBlob).isEqualTo(UPDATED_ASSET_RAW_CONTENT_AS_BLOB);
        assertThat(testRawAssetProcTmpDTO.customAttributesDetailsJSON).isEqualTo(UPDATED_CUSTOM_ATTRIBUTES_DETAILS_JSON);
    }

    @Test
    public void updateNonExistingRawAssetProcTmp() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/raw-asset-proc-tmps")
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
            .body(rawAssetProcTmpDTO)
            .when()
            .put("/api/raw-asset-proc-tmps/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the RawAssetProcTmp in the database
        var rawAssetProcTmpDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(rawAssetProcTmpDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteRawAssetProcTmp() {
        // Initialize the database
        rawAssetProcTmpDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(rawAssetProcTmpDTO)
            .when()
            .post("/api/raw-asset-proc-tmps")
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
            .get("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the rawAssetProcTmp
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/raw-asset-proc-tmps/{id}", rawAssetProcTmpDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var rawAssetProcTmpDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(rawAssetProcTmpDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllRawAssetProcTmps() {
        // Initialize the database
        rawAssetProcTmpDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(rawAssetProcTmpDTO)
            .when()
            .post("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the rawAssetProcTmpList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/raw-asset-proc-tmps?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(rawAssetProcTmpDTO.id.intValue()))
            .body("name", hasItem(DEFAULT_NAME))
            .body("statusRawProcessing", hasItem(DEFAULT_STATUS_RAW_PROCESSING.toString()))
            .body("fullFilenamePath", hasItem(DEFAULT_FULL_FILENAME_PATH))
            .body("assetRawContentAsBlob", hasItem(DEFAULT_ASSET_RAW_CONTENT_AS_BLOB.toString()))
            .body("customAttributesDetailsJSON", hasItem(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON));
    }

    @Test
    public void getRawAssetProcTmp() {
        // Initialize the database
        rawAssetProcTmpDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(rawAssetProcTmpDTO)
            .when()
            .post("/api/raw-asset-proc-tmps")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the rawAssetProcTmp
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/raw-asset-proc-tmps/{id}", rawAssetProcTmpDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the rawAssetProcTmp
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/raw-asset-proc-tmps/{id}", rawAssetProcTmpDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(rawAssetProcTmpDTO.id.intValue()))
            .body("name", is(DEFAULT_NAME))
            .body("statusRawProcessing", is(DEFAULT_STATUS_RAW_PROCESSING.toString()))
            .body("fullFilenamePath", is(DEFAULT_FULL_FILENAME_PATH))
            .body("assetRawContentAsBlob", is(DEFAULT_ASSET_RAW_CONTENT_AS_BLOB.toString()))
            .body("customAttributesDetailsJSON", is(DEFAULT_CUSTOM_ATTRIBUTES_DETAILS_JSON));
    }

    @Test
    public void getNonExistingRawAssetProcTmp() {
        // Get the rawAssetProcTmp
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/raw-asset-proc-tmps/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
