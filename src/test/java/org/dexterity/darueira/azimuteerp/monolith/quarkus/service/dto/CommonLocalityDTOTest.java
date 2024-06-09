package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class CommonLocalityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonLocalityDTO.class);
        CommonLocalityDTO commonLocalityDTO1 = new CommonLocalityDTO();
        commonLocalityDTO1.id = 1L;
        CommonLocalityDTO commonLocalityDTO2 = new CommonLocalityDTO();
        assertThat(commonLocalityDTO1).isNotEqualTo(commonLocalityDTO2);
        commonLocalityDTO2.id = commonLocalityDTO1.id;
        assertThat(commonLocalityDTO1).isEqualTo(commonLocalityDTO2);
        commonLocalityDTO2.id = 2L;
        assertThat(commonLocalityDTO1).isNotEqualTo(commonLocalityDTO2);
        commonLocalityDTO1.id = null;
        assertThat(commonLocalityDTO1).isNotEqualTo(commonLocalityDTO2);
    }
}
