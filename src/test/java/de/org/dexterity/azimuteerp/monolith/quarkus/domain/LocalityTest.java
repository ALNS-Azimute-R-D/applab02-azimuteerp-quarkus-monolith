package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.CountryTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.LocalityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class LocalityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Locality.class);
        Locality locality1 = getLocalitySample1();
        Locality locality2 = new Locality();
        assertThat(locality1).isNotEqualTo(locality2);

        locality2.id = locality1.id;
        assertThat(locality1).isEqualTo(locality2);

        locality2 = getLocalitySample2();
        assertThat(locality1).isNotEqualTo(locality2);
    }
}
