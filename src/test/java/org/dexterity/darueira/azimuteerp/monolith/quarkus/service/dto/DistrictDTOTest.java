package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class DistrictDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DistrictDTO.class);
        DistrictDTO districtDTO1 = new DistrictDTO();
        districtDTO1.id = 1L;
        DistrictDTO districtDTO2 = new DistrictDTO();
        assertThat(districtDTO1).isNotEqualTo(districtDTO2);
        districtDTO2.id = districtDTO1.id;
        assertThat(districtDTO1).isEqualTo(districtDTO2);
        districtDTO2.id = 2L;
        assertThat(districtDTO1).isNotEqualTo(districtDTO2);
        districtDTO1.id = null;
        assertThat(districtDTO1).isNotEqualTo(districtDTO2);
    }
}
