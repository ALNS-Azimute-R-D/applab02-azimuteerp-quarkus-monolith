package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.DistrictTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.ProvinceTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.TownCityTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class TownCityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TownCity.class);
        TownCity townCity1 = getTownCitySample1();
        TownCity townCity2 = new TownCity();
        assertThat(townCity1).isNotEqualTo(townCity2);

        townCity2.id = townCity1.id;
        assertThat(townCity1).isEqualTo(townCity2);

        townCity2 = getTownCitySample2();
        assertThat(townCity1).isNotEqualTo(townCity2);
    }
}
