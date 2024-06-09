package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class AssetMetadataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetMetadataDTO.class);
        AssetMetadataDTO assetMetadataDTO1 = new AssetMetadataDTO();
        assetMetadataDTO1.id = 1L;
        AssetMetadataDTO assetMetadataDTO2 = new AssetMetadataDTO();
        assertThat(assetMetadataDTO1).isNotEqualTo(assetMetadataDTO2);
        assetMetadataDTO2.id = assetMetadataDTO1.id;
        assertThat(assetMetadataDTO1).isEqualTo(assetMetadataDTO2);
        assetMetadataDTO2.id = 2L;
        assertThat(assetMetadataDTO1).isNotEqualTo(assetMetadataDTO2);
        assetMetadataDTO1.id = null;
        assertThat(assetMetadataDTO1).isNotEqualTo(assetMetadataDTO2);
    }
}
