package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class AssetDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetDTO.class);
        AssetDTO assetDTO1 = new AssetDTO();
        assetDTO1.id = 1L;
        AssetDTO assetDTO2 = new AssetDTO();
        assertThat(assetDTO1).isNotEqualTo(assetDTO2);
        assetDTO2.id = assetDTO1.id;
        assertThat(assetDTO1).isEqualTo(assetDTO2);
        assetDTO2.id = 2L;
        assertThat(assetDTO1).isNotEqualTo(assetDTO2);
        assetDTO1.id = null;
        assertThat(assetDTO1).isNotEqualTo(assetDTO2);
    }
}
