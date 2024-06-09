package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.InvoiceTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.PaymentGatewayTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.PaymentTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentGatewayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentGateway.class);
        PaymentGateway paymentGateway1 = getPaymentGatewaySample1();
        PaymentGateway paymentGateway2 = new PaymentGateway();
        assertThat(paymentGateway1).isNotEqualTo(paymentGateway2);

        paymentGateway2.id = paymentGateway1.id;
        assertThat(paymentGateway1).isEqualTo(paymentGateway2);

        paymentGateway2 = getPaymentGatewaySample2();
        assertThat(paymentGateway1).isNotEqualTo(paymentGateway2);
    }
}
