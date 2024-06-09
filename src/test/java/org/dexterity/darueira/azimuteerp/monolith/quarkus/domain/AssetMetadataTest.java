package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetMetadataTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class AssetMetadataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetMetadata.class);
        AssetMetadata assetMetadata1 = getAssetMetadataSample1();
        AssetMetadata assetMetadata2 = new AssetMetadata();
        assertThat(assetMetadata1).isNotEqualTo(assetMetadata2);

        assetMetadata2.id = assetMetadata1.id;
        assertThat(assetMetadata1).isEqualTo(assetMetadata2);

        assetMetadata2 = getAssetMetadataSample2();
        assertThat(assetMetadata1).isNotEqualTo(assetMetadata2);
    }
}
