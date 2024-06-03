package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssetTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssetTypeTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.RawAssetProcTmpTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class AssetTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetType.class);
        AssetType assetType1 = getAssetTypeSample1();
        AssetType assetType2 = new AssetType();
        assertThat(assetType1).isNotEqualTo(assetType2);

        assetType2.id = assetType1.id;
        assertThat(assetType1).isEqualTo(assetType2);

        assetType2 = getAssetTypeSample2();
        assertThat(assetType1).isNotEqualTo(assetType2);
    }
}
