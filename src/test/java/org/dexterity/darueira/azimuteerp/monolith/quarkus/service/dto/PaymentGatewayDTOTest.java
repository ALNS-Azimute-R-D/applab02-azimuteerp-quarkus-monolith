package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentGatewayDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentGatewayDTO.class);
        PaymentGatewayDTO paymentGatewayDTO1 = new PaymentGatewayDTO();
        paymentGatewayDTO1.id = 1L;
        PaymentGatewayDTO paymentGatewayDTO2 = new PaymentGatewayDTO();
        assertThat(paymentGatewayDTO1).isNotEqualTo(paymentGatewayDTO2);
        paymentGatewayDTO2.id = paymentGatewayDTO1.id;
        assertThat(paymentGatewayDTO1).isEqualTo(paymentGatewayDTO2);
        paymentGatewayDTO2.id = 2L;
        assertThat(paymentGatewayDTO1).isNotEqualTo(paymentGatewayDTO2);
        paymentGatewayDTO1.id = null;
        assertThat(paymentGatewayDTO1).isNotEqualTo(paymentGatewayDTO2);
    }
}
