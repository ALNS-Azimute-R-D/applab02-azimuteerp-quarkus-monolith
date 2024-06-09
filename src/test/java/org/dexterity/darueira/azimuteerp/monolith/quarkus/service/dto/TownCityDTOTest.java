package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class TownCityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TownCityDTO.class);
        TownCityDTO townCityDTO1 = new TownCityDTO();
        townCityDTO1.id = 1L;
        TownCityDTO townCityDTO2 = new TownCityDTO();
        assertThat(townCityDTO1).isNotEqualTo(townCityDTO2);
        townCityDTO2.id = townCityDTO1.id;
        assertThat(townCityDTO1).isEqualTo(townCityDTO2);
        townCityDTO2.id = 2L;
        assertThat(townCityDTO1).isNotEqualTo(townCityDTO2);
        townCityDTO1.id = null;
        assertThat(townCityDTO1).isNotEqualTo(townCityDTO2);
    }
}
