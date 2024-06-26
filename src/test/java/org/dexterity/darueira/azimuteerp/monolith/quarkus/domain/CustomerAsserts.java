package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerAllPropertiesEquals(Customer expected, Customer actual) {
        assertCustomerAutoGeneratedPropertiesEquals(expected, actual);
        assertCustomerAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerAllUpdatablePropertiesEquals(Customer expected, Customer actual) {
        assertCustomerUpdatableFieldsEquals(expected, actual);
        assertCustomerUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerAutoGeneratedPropertiesEquals(Customer expected, Customer actual) {
        assertThat(expected)
            .as("Verify Customer auto generated properties")
            .satisfies(e -> assertThat(e.id).as("check id").isEqualTo(actual.id));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerUpdatableFieldsEquals(Customer expected, Customer actual) {
        assertThat(expected)
            .as("Verify Customer relevant properties")
            .satisfies(e -> assertThat(e.customerBusinessCode).as("check customerBusinessCode").isEqualTo(actual.customerBusinessCode))
            .satisfies(e -> assertThat(e.fullname).as("check fullname").isEqualTo(actual.fullname))
            .satisfies(
                e ->
                    assertThat(e.customAttributesDetailsJSON)
                        .as("check customAttributesDetailsJSON")
                        .isEqualTo(actual.customAttributesDetailsJSON)
            )
            .satisfies(e -> assertThat(e.customerStatus).as("check customerStatus").isEqualTo(actual.customerStatus))
            .satisfies(e -> assertThat(e.activationStatus).as("check activationStatus").isEqualTo(actual.activationStatus));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerUpdatableRelationshipsEquals(Customer expected, Customer actual) {
        assertThat(expected)
            .as("Verify Customer relationships")
            .satisfies(e -> assertThat(e.buyerPerson).as("check buyerPerson").isEqualTo(actual.buyerPerson))
            .satisfies(e -> assertThat(e.customerType).as("check customerType").isEqualTo(actual.customerType))
            .satisfies(e -> assertThat(e.district).as("check district").isEqualTo(actual.district));
    }
}
