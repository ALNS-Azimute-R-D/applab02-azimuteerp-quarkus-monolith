package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CountryTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.ProvinceTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.TownCityTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class ProvinceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Province.class);
        Province province1 = getProvinceSample1();
        Province province2 = new Province();
        assertThat(province1).isNotEqualTo(province2);

        province2.id = province1.id;
        assertThat(province1).isEqualTo(province2);

        province2 = getProvinceSample2();
        assertThat(province1).isNotEqualTo(province2);
    }
}
