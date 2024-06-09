package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class BusinessUnitDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessUnitDTO.class);
        BusinessUnitDTO businessUnitDTO1 = new BusinessUnitDTO();
        businessUnitDTO1.id = 1L;
        BusinessUnitDTO businessUnitDTO2 = new BusinessUnitDTO();
        assertThat(businessUnitDTO1).isNotEqualTo(businessUnitDTO2);
        businessUnitDTO2.id = businessUnitDTO1.id;
        assertThat(businessUnitDTO1).isEqualTo(businessUnitDTO2);
        businessUnitDTO2.id = 2L;
        assertThat(businessUnitDTO1).isNotEqualTo(businessUnitDTO2);
        businessUnitDTO1.id = null;
        assertThat(businessUnitDTO1).isNotEqualTo(businessUnitDTO2);
    }
}
