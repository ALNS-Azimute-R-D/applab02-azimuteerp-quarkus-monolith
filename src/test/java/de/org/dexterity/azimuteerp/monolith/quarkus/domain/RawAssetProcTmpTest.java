package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssetTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.AssetTypeTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.RawAssetProcTmpTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class RawAssetProcTmpTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RawAssetProcTmp.class);
        RawAssetProcTmp rawAssetProcTmp1 = getRawAssetProcTmpSample1();
        RawAssetProcTmp rawAssetProcTmp2 = new RawAssetProcTmp();
        assertThat(rawAssetProcTmp1).isNotEqualTo(rawAssetProcTmp2);

        rawAssetProcTmp2.id = rawAssetProcTmp1.id;
        assertThat(rawAssetProcTmp1).isEqualTo(rawAssetProcTmp2);

        rawAssetProcTmp2 = getRawAssetProcTmpSample2();
        assertThat(rawAssetProcTmp1).isNotEqualTo(rawAssetProcTmp2);
    }
}
