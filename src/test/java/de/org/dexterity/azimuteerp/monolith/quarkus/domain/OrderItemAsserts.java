package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderItemAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrderItemAllPropertiesEquals(OrderItem expected, OrderItem actual) {
        assertOrderItemAutoGeneratedPropertiesEquals(expected, actual);
        assertOrderItemAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrderItemAllUpdatablePropertiesEquals(OrderItem expected, OrderItem actual) {
        assertOrderItemUpdatableFieldsEquals(expected, actual);
        assertOrderItemUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrderItemAutoGeneratedPropertiesEquals(OrderItem expected, OrderItem actual) {
        assertThat(expected)
            .as("Verify OrderItem auto generated properties")
            .satisfies(e -> assertThat(e.id).as("check id").isEqualTo(actual.id));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrderItemUpdatableFieldsEquals(OrderItem expected, OrderItem actual) {
        assertThat(expected)
            .as("Verify OrderItem relevant properties")
            .satisfies(e -> assertThat(e.quantity).as("check quantity").isEqualTo(actual.quantity))
            .satisfies(
                e -> assertThat(e.totalPrice).as("check totalPrice").usingComparator(bigDecimalCompareTo).isEqualTo(actual.totalPrice)
            )
            .satisfies(e -> assertThat(e.status).as("check status").isEqualTo(actual.status));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertOrderItemUpdatableRelationshipsEquals(OrderItem expected, OrderItem actual) {
        assertThat(expected)
            .as("Verify OrderItem relationships")
            .satisfies(e -> assertThat(e.article).as("check article").isEqualTo(actual.article))
            .satisfies(e -> assertThat(e.order).as("check order").isEqualTo(actual.order));
    }
}
