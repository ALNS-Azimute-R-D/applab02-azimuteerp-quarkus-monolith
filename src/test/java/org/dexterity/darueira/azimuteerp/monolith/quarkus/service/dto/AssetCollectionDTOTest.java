package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class AssetCollectionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetCollectionDTO.class);
        AssetCollectionDTO assetCollectionDTO1 = new AssetCollectionDTO();
        assetCollectionDTO1.id = 1L;
        AssetCollectionDTO assetCollectionDTO2 = new AssetCollectionDTO();
        assertThat(assetCollectionDTO1).isNotEqualTo(assetCollectionDTO2);
        assetCollectionDTO2.id = assetCollectionDTO1.id;
        assertThat(assetCollectionDTO1).isEqualTo(assetCollectionDTO2);
        assetCollectionDTO2.id = 2L;
        assertThat(assetCollectionDTO1).isNotEqualTo(assetCollectionDTO2);
        assetCollectionDTO1.id = null;
        assertThat(assetCollectionDTO1).isNotEqualTo(assetCollectionDTO2);
    }
}
