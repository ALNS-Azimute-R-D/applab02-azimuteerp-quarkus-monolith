package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.ArticleTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetCollectionTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class AssetCollectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetCollection.class);
        AssetCollection assetCollection1 = getAssetCollectionSample1();
        AssetCollection assetCollection2 = new AssetCollection();
        assertThat(assetCollection1).isNotEqualTo(assetCollection2);

        assetCollection2.id = assetCollection1.id;
        assertThat(assetCollection1).isEqualTo(assetCollection2);

        assetCollection2 = getAssetCollectionSample2();
        assertThat(assetCollection1).isNotEqualTo(assetCollection2);
    }
}
