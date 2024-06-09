package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CommonLocalityTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CustomerTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.DistrictTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.PersonTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.TownCityTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class DistrictTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(District.class);
        District district1 = getDistrictSample1();
        District district2 = new District();
        assertThat(district1).isNotEqualTo(district2);

        district2.id = district1.id;
        assertThat(district1).isEqualTo(district2);

        district2 = getDistrictSample2();
        assertThat(district1).isNotEqualTo(district2);
    }
}
