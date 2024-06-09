package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerTypeDTO.class);
        CustomerTypeDTO customerTypeDTO1 = new CustomerTypeDTO();
        customerTypeDTO1.id = 1L;
        CustomerTypeDTO customerTypeDTO2 = new CustomerTypeDTO();
        assertThat(customerTypeDTO1).isNotEqualTo(customerTypeDTO2);
        customerTypeDTO2.id = customerTypeDTO1.id;
        assertThat(customerTypeDTO1).isEqualTo(customerTypeDTO2);
        customerTypeDTO2.id = 2L;
        assertThat(customerTypeDTO1).isNotEqualTo(customerTypeDTO2);
        customerTypeDTO1.id = null;
        assertThat(customerTypeDTO1).isNotEqualTo(customerTypeDTO2);
    }
}
