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
import java.math.BigDecimal;
import java.util.List;
import liquibase.Liquibase;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.ActivationStatusEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.enumeration.SizeOptionEnum;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.ArticleDTO;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class ArticleResourceTest {

    private static final TypeRef<ArticleDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<ArticleDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final Long DEFAULT_INVENTORY_PRODUCT_ID = 1L;
    private static final Long UPDATED_INVENTORY_PRODUCT_ID = 2L;

    private static final String DEFAULT_SKU_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SKU_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOM_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOM_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE_VALUE = new BigDecimal(2);

    private static final SizeOptionEnum DEFAULT_ITEM_SIZE = SizeOptionEnum.S;
    private static final SizeOptionEnum UPDATED_ITEM_SIZE = SizeOptionEnum.M;

    private static final ActivationStatusEnum DEFAULT_ACTIVATION_STATUS = ActivationStatusEnum.INACTIVE;
    private static final ActivationStatusEnum UPDATED_ACTIVATION_STATUS = ActivationStatusEnum.ACTIVE;

    String adminToken;

    ArticleDTO articleDTO;

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
    public static ArticleDTO createEntity() {
        var articleDTO = new ArticleDTO();
        articleDTO.inventoryProductId = DEFAULT_INVENTORY_PRODUCT_ID;
        articleDTO.skuCode = DEFAULT_SKU_CODE;
        articleDTO.customName = DEFAULT_CUSTOM_NAME;
        articleDTO.customDescription = DEFAULT_CUSTOM_DESCRIPTION;
        articleDTO.priceValue = DEFAULT_PRICE_VALUE;
        articleDTO.itemSize = DEFAULT_ITEM_SIZE;
        articleDTO.activationStatus = DEFAULT_ACTIVATION_STATUS;
        return articleDTO;
    }

    @BeforeEach
    public void initTest() {
        articleDTO = createEntity();
    }

    @Test
    public void createArticle() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Article
        articleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(articleDTO)
            .when()
            .post("/api/articles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Article in the database
        var articleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(articleDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testArticleDTO = articleDTOList.stream().filter(it -> articleDTO.id.equals(it.id)).findFirst().get();
        assertThat(testArticleDTO.inventoryProductId).isEqualTo(DEFAULT_INVENTORY_PRODUCT_ID);
        assertThat(testArticleDTO.skuCode).isEqualTo(DEFAULT_SKU_CODE);
        assertThat(testArticleDTO.customName).isEqualTo(DEFAULT_CUSTOM_NAME);
        assertThat(testArticleDTO.customDescription).isEqualTo(DEFAULT_CUSTOM_DESCRIPTION);
        assertThat(testArticleDTO.priceValue).isEqualByComparingTo(DEFAULT_PRICE_VALUE);
        assertThat(testArticleDTO.itemSize).isEqualTo(DEFAULT_ITEM_SIZE);
        assertThat(testArticleDTO.activationStatus).isEqualTo(DEFAULT_ACTIVATION_STATUS);
    }

    @Test
    public void createArticleWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Article with an existing ID
        articleDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(articleDTO)
            .when()
            .post("/api/articles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Article in the database
        var articleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(articleDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkInventoryProductIdIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        articleDTO.inventoryProductId = null;

        // Create the Article, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(articleDTO)
            .when()
            .post("/api/articles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Article in the database
        var articleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(articleDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkItemSizeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        articleDTO.itemSize = null;

        // Create the Article, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(articleDTO)
            .when()
            .post("/api/articles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Article in the database
        var articleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(articleDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkActivationStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        articleDTO.activationStatus = null;

        // Create the Article, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(articleDTO)
            .when()
            .post("/api/articles")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Article in the database
        var articleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(articleDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateArticle() {
        // Initialize the database
        articleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(articleDTO)
            .when()
            .post("/api/articles")
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
            .get("/api/articles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the article
        var updatedArticleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles/{id}", articleDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the article
        updatedArticleDTO.inventoryProductId = UPDATED_INVENTORY_PRODUCT_ID;
        updatedArticleDTO.skuCode = UPDATED_SKU_CODE;
        updatedArticleDTO.customName = UPDATED_CUSTOM_NAME;
        updatedArticleDTO.customDescription = UPDATED_CUSTOM_DESCRIPTION;
        updatedArticleDTO.priceValue = UPDATED_PRICE_VALUE;
        updatedArticleDTO.itemSize = UPDATED_ITEM_SIZE;
        updatedArticleDTO.activationStatus = UPDATED_ACTIVATION_STATUS;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedArticleDTO)
            .when()
            .put("/api/articles/" + articleDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Article in the database
        var articleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(articleDTOList).hasSize(databaseSizeBeforeUpdate);
        var testArticleDTO = articleDTOList.stream().filter(it -> updatedArticleDTO.id.equals(it.id)).findFirst().get();
        assertThat(testArticleDTO.inventoryProductId).isEqualTo(UPDATED_INVENTORY_PRODUCT_ID);
        assertThat(testArticleDTO.skuCode).isEqualTo(UPDATED_SKU_CODE);
        assertThat(testArticleDTO.customName).isEqualTo(UPDATED_CUSTOM_NAME);
        assertThat(testArticleDTO.customDescription).isEqualTo(UPDATED_CUSTOM_DESCRIPTION);
        assertThat(testArticleDTO.priceValue).isEqualByComparingTo(UPDATED_PRICE_VALUE);
        assertThat(testArticleDTO.itemSize).isEqualTo(UPDATED_ITEM_SIZE);
        assertThat(testArticleDTO.activationStatus).isEqualTo(UPDATED_ACTIVATION_STATUS);
    }

    @Test
    public void updateNonExistingArticle() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles")
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
            .body(articleDTO)
            .when()
            .put("/api/articles/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Article in the database
        var articleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(articleDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteArticle() {
        // Initialize the database
        articleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(articleDTO)
            .when()
            .post("/api/articles")
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
            .get("/api/articles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the article
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/articles/{id}", articleDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var articleDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(articleDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllArticles() {
        // Initialize the database
        articleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(articleDTO)
            .when()
            .post("/api/articles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the articleList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(articleDTO.id.intValue()))
            .body("inventoryProductId", hasItem(DEFAULT_INVENTORY_PRODUCT_ID.intValue()))
            .body("skuCode", hasItem(DEFAULT_SKU_CODE))
            .body("customName", hasItem(DEFAULT_CUSTOM_NAME))
            .body("customDescription", hasItem(DEFAULT_CUSTOM_DESCRIPTION))
            .body("priceValue", hasItem(DEFAULT_PRICE_VALUE.floatValue()))
            .body("itemSize", hasItem(DEFAULT_ITEM_SIZE.toString()))
            .body("activationStatus", hasItem(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getArticle() {
        // Initialize the database
        articleDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(articleDTO)
            .when()
            .post("/api/articles")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the article
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/articles/{id}", articleDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the article
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles/{id}", articleDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(articleDTO.id.intValue()))
            .body("inventoryProductId", is(DEFAULT_INVENTORY_PRODUCT_ID.intValue()))
            .body("skuCode", is(DEFAULT_SKU_CODE))
            .body("customName", is(DEFAULT_CUSTOM_NAME))
            .body("customDescription", is(DEFAULT_CUSTOM_DESCRIPTION))
            .body("priceValue", comparesEqualTo(DEFAULT_PRICE_VALUE.floatValue()))
            .body("itemSize", is(DEFAULT_ITEM_SIZE.toString()))
            .body("activationStatus", is(DEFAULT_ACTIVATION_STATUS.toString()));
    }

    @Test
    public void getNonExistingArticle() {
        // Get the article
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/articles/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
