package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CustomerTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.InvoiceTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrderItemTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrderTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = getOrderSample1();
        Order order2 = new Order();
        assertThat(order1).isNotEqualTo(order2);

        order2.id = order1.id;
        assertThat(order1).isEqualTo(order2);

        order2 = getOrderSample2();
        assertThat(order1).isNotEqualTo(order2);
    }
}
