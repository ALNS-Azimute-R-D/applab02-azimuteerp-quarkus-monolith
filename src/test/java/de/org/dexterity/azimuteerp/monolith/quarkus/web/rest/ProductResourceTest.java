package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import de.org.dexterity.azimuteerp.monolith.quarkus.infrastructure.MockOidcServerTestResource;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.ProductDTO;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import liquibase.Liquibase;
import org.junit.jupiter.api.*;

@QuarkusTest
@QuarkusTestResource(value = MockOidcServerTestResource.class, restrictToAnnotatedClass = true)
public class ProductResourceTest {

    private static final TypeRef<ProductDTO> ENTITY_TYPE = new TypeRef<>() {};

    private static final TypeRef<List<ProductDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {};

    private static final String DEFAULT_PRODUCT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_SKU = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_STANDARD_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_STANDARD_COST = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LIST_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LIST_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_REORDER_LEVEL = 1;
    private static final Integer UPDATED_REORDER_LEVEL = 2;

    private static final Integer DEFAULT_TARGET_LEVEL = 1;
    private static final Integer UPDATED_TARGET_LEVEL = 2;

    private static final String DEFAULT_QUANTITY_PER_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_QUANTITY_PER_UNIT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DISCONTINUED = false;
    private static final Boolean UPDATED_DISCONTINUED = true;

    private static final Integer DEFAULT_MINIMUM_REORDER_QUANTITY = 1;
    private static final Integer UPDATED_MINIMUM_REORDER_QUANTITY = 2;

    private static final String DEFAULT_SUGGESTED_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_SUGGESTED_CATEGORY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ATTACHMENTS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ATTACHMENTS = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ATTACHMENTS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ATTACHMENTS_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_SUPPLIER_IDS = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER_IDS = "BBBBBBBBBB";

    String adminToken;

    ProductDTO productDTO;

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
    public static ProductDTO createEntity() {
        var productDTO = new ProductDTO();
        productDTO.productSKU = DEFAULT_PRODUCT_SKU;
        productDTO.productName = DEFAULT_PRODUCT_NAME;
        productDTO.description = DEFAULT_DESCRIPTION;
        productDTO.standardCost = DEFAULT_STANDARD_COST;
        productDTO.listPrice = DEFAULT_LIST_PRICE;
        productDTO.reorderLevel = DEFAULT_REORDER_LEVEL;
        productDTO.targetLevel = DEFAULT_TARGET_LEVEL;
        productDTO.quantityPerUnit = DEFAULT_QUANTITY_PER_UNIT;
        productDTO.discontinued = DEFAULT_DISCONTINUED;
        productDTO.minimumReorderQuantity = DEFAULT_MINIMUM_REORDER_QUANTITY;
        productDTO.suggestedCategory = DEFAULT_SUGGESTED_CATEGORY;
        productDTO.attachments = DEFAULT_ATTACHMENTS;
        productDTO.supplierIds = DEFAULT_SUPPLIER_IDS;
        return productDTO;
    }

    @BeforeEach
    public void initTest() {
        productDTO = createEntity();
    }

    @Test
    public void createProduct() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Product
        productDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(productDTO)
            .when()
            .post("/api/products")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Validate the Product in the database
        var productDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(productDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testProductDTO = productDTOList.stream().filter(it -> productDTO.id.equals(it.id)).findFirst().get();
        assertThat(testProductDTO.productSKU).isEqualTo(DEFAULT_PRODUCT_SKU);
        assertThat(testProductDTO.productName).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testProductDTO.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProductDTO.standardCost).isEqualByComparingTo(DEFAULT_STANDARD_COST);
        assertThat(testProductDTO.listPrice).isEqualByComparingTo(DEFAULT_LIST_PRICE);
        assertThat(testProductDTO.reorderLevel).isEqualTo(DEFAULT_REORDER_LEVEL);
        assertThat(testProductDTO.targetLevel).isEqualTo(DEFAULT_TARGET_LEVEL);
        assertThat(testProductDTO.quantityPerUnit).isEqualTo(DEFAULT_QUANTITY_PER_UNIT);
        assertThat(testProductDTO.discontinued).isEqualTo(DEFAULT_DISCONTINUED);
        assertThat(testProductDTO.minimumReorderQuantity).isEqualTo(DEFAULT_MINIMUM_REORDER_QUANTITY);
        assertThat(testProductDTO.suggestedCategory).isEqualTo(DEFAULT_SUGGESTED_CATEGORY);
        assertThat(testProductDTO.attachments).isEqualTo(DEFAULT_ATTACHMENTS);
        assertThat(testProductDTO.supplierIds).isEqualTo(DEFAULT_SUPPLIER_IDS);
    }

    @Test
    public void createProductWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Product with an existing ID
        productDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(productDTO)
            .when()
            .post("/api/products")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Product in the database
        var productDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(productDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkListPriceIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        productDTO.listPrice = null;

        // Create the Product, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(productDTO)
            .when()
            .post("/api/products")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Product in the database
        var productDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(productDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDiscontinuedIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        productDTO.discontinued = null;

        // Create the Product, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(productDTO)
            .when()
            .post("/api/products")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Product in the database
        var productDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(productDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateProduct() {
        // Initialize the database
        productDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(productDTO)
            .when()
            .post("/api/products")
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
            .get("/api/products")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the product
        var updatedProductDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products/{id}", productDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .body()
            .as(ENTITY_TYPE);

        // Update the product
        updatedProductDTO.productSKU = UPDATED_PRODUCT_SKU;
        updatedProductDTO.productName = UPDATED_PRODUCT_NAME;
        updatedProductDTO.description = UPDATED_DESCRIPTION;
        updatedProductDTO.standardCost = UPDATED_STANDARD_COST;
        updatedProductDTO.listPrice = UPDATED_LIST_PRICE;
        updatedProductDTO.reorderLevel = UPDATED_REORDER_LEVEL;
        updatedProductDTO.targetLevel = UPDATED_TARGET_LEVEL;
        updatedProductDTO.quantityPerUnit = UPDATED_QUANTITY_PER_UNIT;
        updatedProductDTO.discontinued = UPDATED_DISCONTINUED;
        updatedProductDTO.minimumReorderQuantity = UPDATED_MINIMUM_REORDER_QUANTITY;
        updatedProductDTO.suggestedCategory = UPDATED_SUGGESTED_CATEGORY;
        updatedProductDTO.attachments = UPDATED_ATTACHMENTS;
        updatedProductDTO.supplierIds = UPDATED_SUPPLIER_IDS;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedProductDTO)
            .when()
            .put("/api/products/" + productDTO.id)
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Product in the database
        var productDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(productDTOList).hasSize(databaseSizeBeforeUpdate);
        var testProductDTO = productDTOList.stream().filter(it -> updatedProductDTO.id.equals(it.id)).findFirst().get();
        assertThat(testProductDTO.productSKU).isEqualTo(UPDATED_PRODUCT_SKU);
        assertThat(testProductDTO.productName).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testProductDTO.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProductDTO.standardCost).isEqualByComparingTo(UPDATED_STANDARD_COST);
        assertThat(testProductDTO.listPrice).isEqualByComparingTo(UPDATED_LIST_PRICE);
        assertThat(testProductDTO.reorderLevel).isEqualTo(UPDATED_REORDER_LEVEL);
        assertThat(testProductDTO.targetLevel).isEqualTo(UPDATED_TARGET_LEVEL);
        assertThat(testProductDTO.quantityPerUnit).isEqualTo(UPDATED_QUANTITY_PER_UNIT);
        assertThat(testProductDTO.discontinued).isEqualTo(UPDATED_DISCONTINUED);
        assertThat(testProductDTO.minimumReorderQuantity).isEqualTo(UPDATED_MINIMUM_REORDER_QUANTITY);
        assertThat(testProductDTO.suggestedCategory).isEqualTo(UPDATED_SUGGESTED_CATEGORY);
        assertThat(testProductDTO.attachments).isEqualTo(UPDATED_ATTACHMENTS);
        assertThat(testProductDTO.supplierIds).isEqualTo(UPDATED_SUPPLIER_IDS);
    }

    @Test
    public void updateNonExistingProduct() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products")
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
            .body(productDTO)
            .when()
            .put("/api/products/" + Long.MAX_VALUE)
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Product in the database
        var productDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(productDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteProduct() {
        // Initialize the database
        productDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(productDTO)
            .when()
            .post("/api/products")
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
            .get("/api/products")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the product
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/products/{id}", productDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var productDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract()
            .as(LIST_OF_ENTITY_TYPE);

        assertThat(productDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllProducts() {
        // Initialize the database
        productDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(productDTO)
            .when()
            .post("/api/products")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        // Get all the productList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(productDTO.id.intValue()))
            .body("productSKU", hasItem(DEFAULT_PRODUCT_SKU))
            .body("productName", hasItem(DEFAULT_PRODUCT_NAME))
            .body("description", hasItem(DEFAULT_DESCRIPTION.toString()))
            .body("standardCost", hasItem(DEFAULT_STANDARD_COST.floatValue()))
            .body("listPrice", hasItem(DEFAULT_LIST_PRICE.floatValue()))
            .body("reorderLevel", hasItem(DEFAULT_REORDER_LEVEL.intValue()))
            .body("targetLevel", hasItem(DEFAULT_TARGET_LEVEL.intValue()))
            .body("quantityPerUnit", hasItem(DEFAULT_QUANTITY_PER_UNIT))
            .body("discontinued", hasItem(DEFAULT_DISCONTINUED.booleanValue()))
            .body("minimumReorderQuantity", hasItem(DEFAULT_MINIMUM_REORDER_QUANTITY.intValue()))
            .body("suggestedCategory", hasItem(DEFAULT_SUGGESTED_CATEGORY))
            .body("attachments", hasItem(DEFAULT_ATTACHMENTS.toString()))
            .body("supplierIds", hasItem(DEFAULT_SUPPLIER_IDS.toString()));
    }

    @Test
    public void getProduct() {
        // Initialize the database
        productDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(productDTO)
            .when()
            .post("/api/products")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .as(ENTITY_TYPE);

        var response = // Get the product
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/products/{id}", productDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract()
                .as(ENTITY_TYPE);

        // Get the product
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products/{id}", productDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(productDTO.id.intValue()))
            .body("productSKU", is(DEFAULT_PRODUCT_SKU))
            .body("productName", is(DEFAULT_PRODUCT_NAME))
            .body("description", is(DEFAULT_DESCRIPTION.toString()))
            .body("standardCost", comparesEqualTo(DEFAULT_STANDARD_COST.floatValue()))
            .body("listPrice", comparesEqualTo(DEFAULT_LIST_PRICE.floatValue()))
            .body("reorderLevel", is(DEFAULT_REORDER_LEVEL.intValue()))
            .body("targetLevel", is(DEFAULT_TARGET_LEVEL.intValue()))
            .body("quantityPerUnit", is(DEFAULT_QUANTITY_PER_UNIT))
            .body("discontinued", is(DEFAULT_DISCONTINUED.booleanValue()))
            .body("minimumReorderQuantity", is(DEFAULT_MINIMUM_REORDER_QUANTITY.intValue()))
            .body("suggestedCategory", is(DEFAULT_SUGGESTED_CATEGORY))
            .body("attachments", is(DEFAULT_ATTACHMENTS.toString()))
            .body("supplierIds", is(DEFAULT_SUPPLIER_IDS.toString()));
    }

    @Test
    public void getNonExistingProduct() {
        // Get the product
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/products/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
