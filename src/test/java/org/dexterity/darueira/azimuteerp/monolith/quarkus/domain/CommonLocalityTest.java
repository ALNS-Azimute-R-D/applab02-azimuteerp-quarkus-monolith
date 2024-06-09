package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CommonLocalityTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.DistrictTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class CommonLocalityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommonLocality.class);
        CommonLocality commonLocality1 = getCommonLocalitySample1();
        CommonLocality commonLocality2 = new CommonLocality();
        assertThat(commonLocality1).isNotEqualTo(commonLocality2);

        commonLocality2.id = commonLocality1.id;
        assertThat(commonLocality1).isEqualTo(commonLocality2);

        commonLocality2 = getCommonLocalitySample2();
        assertThat(commonLocality1).isNotEqualTo(commonLocality2);
    }
}
