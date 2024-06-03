package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class RawAssetProcTmpDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RawAssetProcTmpDTO.class);
        RawAssetProcTmpDTO rawAssetProcTmpDTO1 = new RawAssetProcTmpDTO();
        rawAssetProcTmpDTO1.id = 1L;
        RawAssetProcTmpDTO rawAssetProcTmpDTO2 = new RawAssetProcTmpDTO();
        assertThat(rawAssetProcTmpDTO1).isNotEqualTo(rawAssetProcTmpDTO2);
        rawAssetProcTmpDTO2.id = rawAssetProcTmpDTO1.id;
        assertThat(rawAssetProcTmpDTO1).isEqualTo(rawAssetProcTmpDTO2);
        rawAssetProcTmpDTO2.id = 2L;
        assertThat(rawAssetProcTmpDTO1).isNotEqualTo(rawAssetProcTmpDTO2);
        rawAssetProcTmpDTO1.id = null;
        assertThat(rawAssetProcTmpDTO1).isNotEqualTo(rawAssetProcTmpDTO2);
    }
}
