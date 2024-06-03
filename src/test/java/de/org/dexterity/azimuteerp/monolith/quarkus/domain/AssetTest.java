package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssetMetadataTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssetTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssetTypeTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.RawAssetProcTmpTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class AssetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Asset.class);
        Asset asset1 = getAssetSample1();
        Asset asset2 = new Asset();
        assertThat(asset1).isNotEqualTo(asset2);

        asset2.id = asset1.id;
        assertThat(asset1).isEqualTo(asset2);

        asset2 = getAssetSample2();
        assertThat(asset1).isNotEqualTo(asset2);
    }
}
