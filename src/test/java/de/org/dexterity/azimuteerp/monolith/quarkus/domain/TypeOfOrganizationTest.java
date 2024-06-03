package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.OrganizationTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.TypeOfOrganizationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class TypeOfOrganizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeOfOrganization.class);
        TypeOfOrganization typeOfOrganization1 = getTypeOfOrganizationSample1();
        TypeOfOrganization typeOfOrganization2 = new TypeOfOrganization();
        assertThat(typeOfOrganization1).isNotEqualTo(typeOfOrganization2);

        typeOfOrganization2.id = typeOfOrganization1.id;
        assertThat(typeOfOrganization1).isEqualTo(typeOfOrganization2);

        typeOfOrganization2 = getTypeOfOrganizationSample2();
        assertThat(typeOfOrganization1).isNotEqualTo(typeOfOrganization2);
    }
}
