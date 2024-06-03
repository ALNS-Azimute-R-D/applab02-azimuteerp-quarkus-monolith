package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class InvoiceAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInvoiceAllPropertiesEquals(Invoice expected, Invoice actual) {
        assertInvoiceAutoGeneratedPropertiesEquals(expected, actual);
        assertInvoiceAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInvoiceAllUpdatablePropertiesEquals(Invoice expected, Invoice actual) {
        assertInvoiceUpdatableFieldsEquals(expected, actual);
        assertInvoiceUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInvoiceAutoGeneratedPropertiesEquals(Invoice expected, Invoice actual) {
        assertThat(expected)
            .as("Verify Invoice auto generated properties")
            .satisfies(e -> assertThat(e.id).as("check id").isEqualTo(actual.id));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInvoiceUpdatableFieldsEquals(Invoice expected, Invoice actual) {
        assertThat(expected)
            .as("Verify Invoice relevant properties")
            .satisfies(e -> assertThat(e.businessCode).as("check businessCode").isEqualTo(actual.businessCode))
            .satisfies(e -> assertThat(e.originalOrderId).as("check originalOrderId").isEqualTo(actual.originalOrderId))
            .satisfies(e -> assertThat(e.invoiceDate).as("check invoiceDate").isEqualTo(actual.invoiceDate))
            .satisfies(e -> assertThat(e.dueDate).as("check dueDate").isEqualTo(actual.dueDate))
            .satisfies(e -> assertThat(e.description).as("check description").isEqualTo(actual.description))
            .satisfies(e -> assertThat(e.taxValue).as("check taxValue").usingComparator(bigDecimalCompareTo).isEqualTo(actual.taxValue))
            .satisfies(
                e ->
                    assertThat(e.shippingValue)
                        .as("check shippingValue")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.shippingValue)
            )
            .satisfies(
                e ->
                    assertThat(e.amountDueValue)
                        .as("check amountDueValue")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.amountDueValue)
            )
            .satisfies(
                e ->
                    assertThat(e.numberOfInstallmentsOriginal)
                        .as("check numberOfInstallmentsOriginal")
                        .isEqualTo(actual.numberOfInstallmentsOriginal)
            )
            .satisfies(
                e -> assertThat(e.numberOfInstallmentsPaid).as("check numberOfInstallmentsPaid").isEqualTo(actual.numberOfInstallmentsPaid)
            )
            .satisfies(
                e ->
                    assertThat(e.amountPaidValue)
                        .as("check amountPaidValue")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.amountPaidValue)
            )
            .satisfies(e -> assertThat(e.status).as("check status").isEqualTo(actual.status))
            .satisfies(e -> assertThat(e.extraDetails).as("check extraDetails").isEqualTo(actual.extraDetails));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertInvoiceUpdatableRelationshipsEquals(Invoice expected, Invoice actual) {
        assertThat(expected)
            .as("Verify Invoice relationships")
            .satisfies(
                e -> assertThat(e.preferrablePaymentMethod).as("check preferrablePaymentMethod").isEqualTo(actual.preferrablePaymentMethod)
            );
    }
}