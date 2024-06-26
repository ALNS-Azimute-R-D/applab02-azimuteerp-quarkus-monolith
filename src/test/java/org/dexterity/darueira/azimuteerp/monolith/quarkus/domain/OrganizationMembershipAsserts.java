package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class OrganizationMembershipAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrganizationMembershipAllPropertiesEquals(OrganizationMembership expected, OrganizationMembership actual) {
        assertOrganizationMembershipAutoGeneratedPropertiesEquals(expected, actual);
        assertOrganizationMembershipAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrganizationMembershipAllUpdatablePropertiesEquals(
        OrganizationMembership expected,
        OrganizationMembership actual
    ) {
        assertOrganizationMembershipUpdatableFieldsEquals(expected, actual);
        assertOrganizationMembershipUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrganizationMembershipAutoGeneratedPropertiesEquals(
        OrganizationMembership expected,
        OrganizationMembership actual
    ) {
        assertThat(expected)
            .as("Verify OrganizationMembership auto generated properties")
            .satisfies(e -> assertThat(e.id).as("check id").isEqualTo(actual.id));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrganizationMembershipUpdatableFieldsEquals(OrganizationMembership expected, OrganizationMembership actual) {
        assertThat(expected)
            .as("Verify OrganizationMembership relevant properties")
            .satisfies(e -> assertThat(e.joinedAt).as("check joinedAt").isEqualTo(actual.joinedAt))
            .satisfies(e -> assertThat(e.activationStatus).as("check activationStatus").isEqualTo(actual.activationStatus));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrganizationMembershipUpdatableRelationshipsEquals(
        OrganizationMembership expected,
        OrganizationMembership actual
    ) {
        assertThat(expected)
            .as("Verify OrganizationMembership relationships")
            .satisfies(e -> assertThat(e.organization).as("check organization").isEqualTo(actual.organization))
            .satisfies(e -> assertThat(e.person).as("check person").isEqualTo(actual.person));
    }
}
