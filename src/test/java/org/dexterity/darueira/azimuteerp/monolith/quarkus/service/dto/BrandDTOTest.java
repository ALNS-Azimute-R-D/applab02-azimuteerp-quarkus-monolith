package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class BrandDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrandDTO.class);
        BrandDTO brandDTO1 = new BrandDTO();
        brandDTO1.id = 1L;
        BrandDTO brandDTO2 = new BrandDTO();
        assertThat(brandDTO1).isNotEqualTo(brandDTO2);
        brandDTO2.id = brandDTO1.id;
        assertThat(brandDTO1).isEqualTo(brandDTO2);
        brandDTO2.id = 2L;
        assertThat(brandDTO1).isNotEqualTo(brandDTO2);
        brandDTO1.id = null;
        assertThat(brandDTO1).isNotEqualTo(brandDTO2);
    }
}
