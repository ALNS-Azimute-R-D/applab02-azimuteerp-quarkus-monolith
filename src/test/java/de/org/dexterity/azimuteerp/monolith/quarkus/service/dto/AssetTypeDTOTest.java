package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class AssetTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetTypeDTO.class);
        AssetTypeDTO assetTypeDTO1 = new AssetTypeDTO();
        assetTypeDTO1.id = 1L;
        AssetTypeDTO assetTypeDTO2 = new AssetTypeDTO();
        assertThat(assetTypeDTO1).isNotEqualTo(assetTypeDTO2);
        assetTypeDTO2.id = assetTypeDTO1.id;
        assertThat(assetTypeDTO1).isEqualTo(assetTypeDTO2);
        assetTypeDTO2.id = 2L;
        assertThat(assetTypeDTO1).isNotEqualTo(assetTypeDTO2);
        assetTypeDTO1.id = null;
        assertThat(assetTypeDTO1).isNotEqualTo(assetTypeDTO2);
    }
}
