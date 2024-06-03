package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class OrganizationAttributeAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrganizationAttributeAllPropertiesEquals(OrganizationAttribute expected, OrganizationAttribute actual) {
        assertOrganizationAttributeAutoGeneratedPropertiesEquals(expected, actual);
        assertOrganizationAttributeAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrganizationAttributeAllUpdatablePropertiesEquals(
        OrganizationAttribute expected,
        OrganizationAttribute actual
    ) {
        assertOrganizationAttributeUpdatableFieldsEquals(expected, actual);
        assertOrganizationAttributeUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrganizationAttributeAutoGeneratedPropertiesEquals(
        OrganizationAttribute expected,
        OrganizationAttribute actual
    ) {
        assertThat(expected)
            .as("Verify OrganizationAttribute auto generated properties")
            .satisfies(e -> assertThat(e.id).as("check id").isEqualTo(actual.id));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrganizationAttributeUpdatableFieldsEquals(OrganizationAttribute expected, OrganizationAttribute actual) {
        assertThat(expected)
            .as("Verify OrganizationAttribute relevant properties")
            .satisfies(e -> assertThat(e.attributeName).as("check attributeName").isEqualTo(actual.attributeName))
            .satisfies(e -> assertThat(e.attributeValue).as("check attributeValue").isEqualTo(actual.attributeValue));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrganizationAttributeUpdatableRelationshipsEquals(
        OrganizationAttribute expected,
        OrganizationAttribute actual
    ) {
        assertThat(expected)
            .as("Verify OrganizationAttribute relationships")
            .satisfies(e -> assertThat(e.organization).as("check organization").isEqualTo(actual.organization));
    }
}
