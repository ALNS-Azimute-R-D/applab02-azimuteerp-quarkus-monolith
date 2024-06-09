package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationDomainTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.OrganizationTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationDomainTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationDomain.class);
        OrganizationDomain organizationDomain1 = getOrganizationDomainSample1();
        OrganizationDomain organizationDomain2 = new OrganizationDomain();
        assertThat(organizationDomain1).isNotEqualTo(organizationDomain2);

        organizationDomain2.id = organizationDomain1.id;
        assertThat(organizationDomain1).isEqualTo(organizationDomain2);

        organizationDomain2 = getOrganizationDomainSample2();
        assertThat(organizationDomain1).isNotEqualTo(organizationDomain2);
    }
}
