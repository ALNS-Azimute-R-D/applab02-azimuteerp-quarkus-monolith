package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class PaymentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentAllPropertiesEquals(Payment expected, Payment actual) {
        assertPaymentAutoGeneratedPropertiesEquals(expected, actual);
        assertPaymentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentAllUpdatablePropertiesEquals(Payment expected, Payment actual) {
        assertPaymentUpdatableFieldsEquals(expected, actual);
        assertPaymentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentAutoGeneratedPropertiesEquals(Payment expected, Payment actual) {
        assertThat(expected)
            .as("Verify Payment auto generated properties")
            .satisfies(e -> assertThat(e.id).as("check id").isEqualTo(actual.id));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentUpdatableFieldsEquals(Payment expected, Payment actual) {
        assertThat(expected)
            .as("Verify Payment relevant properties")
            .satisfies(e -> assertThat(e.installmentNumber).as("check installmentNumber").isEqualTo(actual.installmentNumber))
            .satisfies(e -> assertThat(e.paymentDueDate).as("check paymentDueDate").isEqualTo(actual.paymentDueDate))
            .satisfies(e -> assertThat(e.paymentPaidDate).as("check paymentPaidDate").isEqualTo(actual.paymentPaidDate))
            .satisfies(
                e ->
                    assertThat(e.paymentAmount)
                        .as("check paymentAmount")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.paymentAmount)
            )
            .satisfies(e -> assertThat(e.typeOfPayment).as("check typeOfPayment").isEqualTo(actual.typeOfPayment))
            .satisfies(e -> assertThat(e.status).as("check status").isEqualTo(actual.status))
            .satisfies(e -> assertThat(e.extraDetails).as("check extraDetails").isEqualTo(actual.extraDetails));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPaymentUpdatableRelationshipsEquals(Payment expected, Payment actual) {
        assertThat(expected)
            .as("Verify Payment relationships")
            .satisfies(e -> assertThat(e.paymentMethod).as("check paymentMethod").isEqualTo(actual.paymentMethod));
    }
}
