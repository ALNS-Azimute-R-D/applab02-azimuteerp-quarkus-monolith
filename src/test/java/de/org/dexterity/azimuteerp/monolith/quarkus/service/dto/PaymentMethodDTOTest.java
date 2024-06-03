package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentMethodDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMethodDTO.class);
        PaymentMethodDTO paymentMethodDTO1 = new PaymentMethodDTO();
        paymentMethodDTO1.id = 1L;
        PaymentMethodDTO paymentMethodDTO2 = new PaymentMethodDTO();
        assertThat(paymentMethodDTO1).isNotEqualTo(paymentMethodDTO2);
        paymentMethodDTO2.id = paymentMethodDTO1.id;
        assertThat(paymentMethodDTO1).isEqualTo(paymentMethodDTO2);
        paymentMethodDTO2.id = 2L;
        assertThat(paymentMethodDTO1).isNotEqualTo(paymentMethodDTO2);
        paymentMethodDTO1.id = null;
        assertThat(paymentMethodDTO1).isNotEqualTo(paymentMethodDTO2);
    }
}
