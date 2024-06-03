package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class LocalityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocalityDTO.class);
        LocalityDTO localityDTO1 = new LocalityDTO();
        localityDTO1.id = 1L;
        LocalityDTO localityDTO2 = new LocalityDTO();
        assertThat(localityDTO1).isNotEqualTo(localityDTO2);
        localityDTO2.id = localityDTO1.id;
        assertThat(localityDTO1).isEqualTo(localityDTO2);
        localityDTO2.id = 2L;
        assertThat(localityDTO1).isNotEqualTo(localityDTO2);
        localityDTO1.id = null;
        assertThat(localityDTO1).isNotEqualTo(localityDTO2);
    }
}
