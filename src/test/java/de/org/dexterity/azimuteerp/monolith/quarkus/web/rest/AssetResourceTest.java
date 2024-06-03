package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.PreferredPurposeEnum;
import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.StatusAssetEnum;
import de.org.dexterity.azimuteerp.monolith.quarkus.domain.enumeration.StorageTypeEnum;
import de.org.dexterity.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.AssetDTO;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class AssetResourceTest {

    private static final TypeRef<AssetDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<AssetDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final UUID DEFAULT_UID = UUID.randomUUID();
    private static final UUID UPDATED_UID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final StorageTypeEnum DEFAULT_STORAGE_TYPE_USED = StorageTypeEnum.BLOB_IN_DB;
    private static final StorageTypeEnum UPDATED_STORAGE_TYPE_USED = StorageTypeEnum.LOCAL_FILESYSTEM;

    private static final String DEFAULT_FULL_FILENAME_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FULL_FILENAME_PATH = "BBBBBBBBBB";

    private static final StatusAssetEnum DEFAULT_STATUS = StatusAssetEnum.ENABLED;
    private static final StatusAssetEnum UPDATED_STATUS = StatusAssetEnum.DISABLED;

    private static final PreferredPurposeEnum DEFAULT_PREFERRED_PURPOSE = PreferredPurposeEnum.USER_AVATAR;
    private static final PreferredPurposeEnum UPDATED_PREFERRED_PURPOSE = PreferredPurposeEnum.LOGO_IMG;

    private static final byte[] DEFAULT_ASSET_CONTENT_AS_BLOB = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ASSET_CONTENT_AS_BLOB = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ASSET_CONTENT_AS_BLOB_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ASSET_CONTENT_AS_BLOB_CONTENT_TYPE = "image/png";

    String adminToken;

    AssetDTO assetDTO;

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
    public static AssetDTO createEntity() {
        var assetDTO = new AssetDTO();
        assetDTO.uid = DEFAULT_UID;
        assetDTO.name = DEFAULT_NAME;
        assetDTO.storageTypeUsed = DEFAULT_STORAGE_TYPE_USED;
        assetDTO.fullFilenamePath = DEFAULT_FULL_FILENAME_PATH;
        assetDTO.status = DEFAULT_STATUS;
        assetDTO.preferredPurpose = DEFAULT_PREFERRED_PURPOSE;
        assetDTO.assetContentAsBlob = DEFAULT_ASSET_CONTENT_AS_BLOB;
        return assetDTO;
    }

    @BeforeEach
    public void initTest() {
        assetDTO = createEntity();
    }

    @Test
    public void createAsset() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assets")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Asset
        assetDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assetDTO)
            .when()
            .post("/api/assets")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Asset in the database
        var assetDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assets")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(assetDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testAssetDTO = assetDTOList.stream().filter(it -> assetDTO.id.equals(it.id)).findFirst().get();
        assertThat(testAssetDTO.uid).isEqualTo(DEFAULT_UID);
        assertThat(testAssetDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testAssetDTO.storageTypeUsed).isEqualTo(DEFAULT_STORAGE_TYPE_USED);
        assertThat(testAssetDTO.fullFilenamePath).isEqualTo(DEFAULT_FULL_FILENAME_PATH);
        assertThat(testAssetDTO.status).isEqualTo(DEFAULT_STATUS);
        assertThat(testAssetDTO.preferredPurpose).isEqualTo(DEFAULT_PREFERRED_PURPOSE);
        assertThat(testAssetDTO.assetContentAsBlob).isEqualTo(DEFAULT_ASSET_CONTENT_AS_BLOB);
    }

    @Test
    public void createAssetWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assets")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Asset with an existing ID
        assetDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assetDTO)
            .when()
            .post("/api/assets")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Asset in the database
        var assetDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assets")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(assetDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assets")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        assetDTO.name = null;

        // Create the Asset, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assetDTO)
            .when()
            .post("/api/assets")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Asset in the database
        var assetDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assets")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(assetDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateAsset() {
        // Initialize the database
        assetDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assetDTO)
            .when()
            .post("/api/assets")
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
            .get("/api/assets")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the asset
        var updatedAssetDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assets/{id}", assetDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the asset
        updatedAssetDTO.uid = UPDATED_UID;
        updatedAssetDTO.name = UPDATED_NAME;
        updatedAssetDTO.storageTypeUsed = UPDATED_STORAGE_TYPE_USED;
        updatedAssetDTO.fullFilenamePath = UPDATED_FULL_FILENAME_PATH;
        updatedAssetDTO.status = UPDATED_STATUS;
        updatedAssetDTO.preferredPurpose = UPDATED_PREFERRED_PURPOSE;
        updatedAssetDTO.assetContentAsBlob = UPDATED_ASSET_CONTENT_AS_BLOB;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedAssetDTO)
            .when()
            .put("/api/assets/" + assetDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Asset in the database
        var assetDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assets")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(assetDTOList).hasSize(databaseSizeBeforeUpdate);
        var testAssetDTO = assetDTOList.stream().filter(it -> updatedAssetDTO.id.equals(it.id)).findFirst().get();
        assertThat(testAssetDTO.uid).isEqualTo(UPDATED_UID);
        assertThat(testAssetDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testAssetDTO.storageTypeUsed).isEqualTo(UPDATED_STORAGE_TYPE_USED);
        assertThat(testAssetDTO.fullFilenamePath).isEqualTo(UPDATED_FULL_FILENAME_PATH);
        assertThat(testAssetDTO.status).isEqualTo(UPDATED_STATUS);
        assertThat(testAssetDTO.preferredPurpose).isEqualTo(UPDATED_PREFERRED_PURPOSE);
        assertThat(testAssetDTO.assetContentAsBlob).isEqualTo(UPDATED_ASSET_CONTENT_AS_BLOB);
    }

    @Test
    public void updateNonExistingAsset() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assets")
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
            .body(assetDTO)
            .when()
            .put("/api/assets/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Asset in the database
        var assetDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assets")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(assetDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteAsset() {
        // Initialize the database
        assetDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assetDTO)
            .when()
            .post("/api/assets")
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
            .get("/api/assets")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the asset
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/assets/{id}", assetDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var assetDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assets")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(assetDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllAssets() {
        // Initialize the database
        assetDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assetDTO)
            .when()
            .post("/api/assets")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the assetList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assets?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(assetDTO.id.intValue()))
            .body("uid", hasItem(DEFAULT_UID.toString()))
            .body("name", hasItem(DEFAULT_NAME))
            .body("storageTypeUsed", hasItem(DEFAULT_STORAGE_TYPE_USED.toString()))
            .body("fullFilenamePath", hasItem(DEFAULT_FULL_FILENAME_PATH))
            .body("status", hasItem(DEFAULT_STATUS.toString()))
            .body("preferredPurpose", hasItem(DEFAULT_PREFERRED_PURPOSE.toString()))
            .body("assetContentAsBlob", hasItem(DEFAULT_ASSET_CONTENT_AS_BLOB.toString()));
    }

    @Test
    public void getAsset() {
        // Initialize the database
        assetDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assetDTO)
            .when()
            .post("/api/assets")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the asset
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/assets/{id}", assetDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the asset
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assets/{id}", assetDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(assetDTO.id.intValue()))
            .body("uid", is(DEFAULT_UID.toString()))
            .body("name", is(DEFAULT_NAME))
            .body("storageTypeUsed", is(DEFAULT_STORAGE_TYPE_USED.toString()))
            .body("fullFilenamePath", is(DEFAULT_FULL_FILENAME_PATH))
            .body("status", is(DEFAULT_STATUS.toString()))
            .body("preferredPurpose", is(DEFAULT_PREFERRED_PURPOSE.toString()))
            .body("assetContentAsBlob", is(DEFAULT_ASSET_CONTENT_AS_BLOB.toString()));
    }

    @Test
    public void getNonExistingAsset() {
        // Get the asset
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assets/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
