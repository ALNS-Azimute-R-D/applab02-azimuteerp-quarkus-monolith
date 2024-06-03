package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class ProvinceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProvinceDTO.class);
        ProvinceDTO provinceDTO1 = new ProvinceDTO();
        provinceDTO1.id = 1L;
        ProvinceDTO provinceDTO2 = new ProvinceDTO();
        assertThat(provinceDTO1).isNotEqualTo(provinceDTO2);
        provinceDTO2.id = provinceDTO1.id;
        assertThat(provinceDTO1).isEqualTo(provinceDTO2);
        provinceDTO2.id = 2L;
        assertThat(provinceDTO1).isNotEqualTo(provinceDTO2);
        provinceDTO1.id = null;
        assertThat(provinceDTO1).isNotEqualTo(provinceDTO2);
    }
}
