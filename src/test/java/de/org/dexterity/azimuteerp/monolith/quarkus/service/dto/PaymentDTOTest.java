package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentDTO.class);
        PaymentDTO paymentDTO1 = new PaymentDTO();
        paymentDTO1.id = 1L;
        PaymentDTO paymentDTO2 = new PaymentDTO();
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
        paymentDTO2.id = paymentDTO1.id;
        assertThat(paymentDTO1).isEqualTo(paymentDTO2);
        paymentDTO2.id = 2L;
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
        paymentDTO1.id = null;
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
    }
}
