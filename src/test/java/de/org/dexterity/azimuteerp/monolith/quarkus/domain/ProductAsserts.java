package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductAllPropertiesEquals(Product expected, Product actual) {
        assertProductAutoGeneratedPropertiesEquals(expected, actual);
        assertProductAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductAllUpdatablePropertiesEquals(Product expected, Product actual) {
        assertProductUpdatableFieldsEquals(expected, actual);
        assertProductUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductAutoGeneratedPropertiesEquals(Product expected, Product actual) {
        assertThat(expected)
            .as("Verify Product auto generated properties")
            .satisfies(e -> assertThat(e.id).as("check id").isEqualTo(actual.id));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductUpdatableFieldsEquals(Product expected, Product actual) {
        assertThat(expected)
            .as("Verify Product relevant properties")
            .satisfies(e -> assertThat(e.productSKU).as("check productSKU").isEqualTo(actual.productSKU))
            .satisfies(e -> assertThat(e.productName).as("check productName").isEqualTo(actual.productName))
            .satisfies(e -> assertThat(e.description).as("check description").isEqualTo(actual.description))
            .satisfies(
                e -> assertThat(e.standardCost).as("check standardCost").usingComparator(bigDecimalCompareTo).isEqualTo(actual.standardCost)
            )
            .satisfies(e -> assertThat(e.listPrice).as("check listPrice").usingComparator(bigDecimalCompareTo).isEqualTo(actual.listPrice))
            .satisfies(e -> assertThat(e.reorderLevel).as("check reorderLevel").isEqualTo(actual.reorderLevel))
            .satisfies(e -> assertThat(e.targetLevel).as("check targetLevel").isEqualTo(actual.targetLevel))
            .satisfies(e -> assertThat(e.quantityPerUnit).as("check quantityPerUnit").isEqualTo(actual.quantityPerUnit))
            .satisfies(e -> assertThat(e.discontinued).as("check discontinued").isEqualTo(actual.discontinued))
            .satisfies(
                e -> assertThat(e.minimumReorderQuantity).as("check minimumReorderQuantity").isEqualTo(actual.minimumReorderQuantity)
            )
            .satisfies(e -> assertThat(e.suggestedCategory).as("check suggestedCategory").isEqualTo(actual.suggestedCategory))
            .satisfies(e -> assertThat(e.attachments).as("check attachments").isEqualTo(actual.attachments))
            .satisfies(
                e -> assertThat(e.attachmentsContentType).as("check attachments contenty type").isEqualTo(actual.attachmentsContentType)
            )
            .satisfies(e -> assertThat(e.supplierIds).as("check supplierIds").isEqualTo(actual.supplierIds));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProductUpdatableRelationshipsEquals(Product expected, Product actual) {
        assertThat(expected)
            .as("Verify Product relationships")
            .satisfies(e -> assertThat(e.brand).as("check brand").isEqualTo(actual.brand));
    }
}