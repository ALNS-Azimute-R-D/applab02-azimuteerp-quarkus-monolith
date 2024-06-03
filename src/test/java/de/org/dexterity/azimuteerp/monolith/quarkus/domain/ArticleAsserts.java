package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class ArticleAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertArticleAllPropertiesEquals(Article expected, Article actual) {
        assertArticleAutoGeneratedPropertiesEquals(expected, actual);
        assertArticleAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertArticleAllUpdatablePropertiesEquals(Article expected, Article actual) {
        assertArticleUpdatableFieldsEquals(expected, actual);
        assertArticleUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertArticleAutoGeneratedPropertiesEquals(Article expected, Article actual) {
        assertThat(expected)
            .as("Verify Article auto generated properties")
            .satisfies(e -> assertThat(e.id).as("check id").isEqualTo(actual.id));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertArticleUpdatableFieldsEquals(Article expected, Article actual) {
        assertThat(expected)
            .as("Verify Article relevant properties")
            .satisfies(e -> assertThat(e.inventoryProductId).as("check inventoryProductId").isEqualTo(actual.inventoryProductId))
            .satisfies(e -> assertThat(e.customName).as("check customName").isEqualTo(actual.customName))
            .satisfies(e -> assertThat(e.customDescription).as("check customDescription").isEqualTo(actual.customDescription))
            .satisfies(
                e -> assertThat(e.priceValue).as("check priceValue").usingComparator(bigDecimalCompareTo).isEqualTo(actual.priceValue)
            )
            .satisfies(e -> assertThat(e.itemSize).as("check itemSize").isEqualTo(actual.itemSize))
            .satisfies(e -> assertThat(e.assetsCollectionUUID).as("check assetsCollectionUUID").isEqualTo(actual.assetsCollectionUUID))
            .satisfies(e -> assertThat(e.isEnabled).as("check isEnabled").isEqualTo(actual.isEnabled));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertArticleUpdatableRelationshipsEquals(Article expected, Article actual) {
        assertThat(expected)
            .as("Verify Article relationships")
            .satisfies(e -> assertThat(e.mainCategory).as("check mainCategory").isEqualTo(actual.mainCategory));
    }
}
