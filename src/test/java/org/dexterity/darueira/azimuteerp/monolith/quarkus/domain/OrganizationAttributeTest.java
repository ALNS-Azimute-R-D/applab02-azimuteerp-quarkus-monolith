package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationAttributeTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationAttributeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationAttribute.class);
        OrganizationAttribute organizationAttribute1 = getOrganizationAttributeSample1();
        OrganizationAttribute organizationAttribute2 = new OrganizationAttribute();
        assertThat(organizationAttribute1).isNotEqualTo(organizationAttribute2);

        organizationAttribute2.id = organizationAttribute1.id;
        assertThat(organizationAttribute1).isEqualTo(organizationAttribute2);

        organizationAttribute2 = getOrganizationAttributeSample2();
        assertThat(organizationAttribute1).isNotEqualTo(organizationAttribute2);
    }
}
