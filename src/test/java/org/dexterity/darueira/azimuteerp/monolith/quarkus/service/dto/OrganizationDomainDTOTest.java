package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationDomainDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationDomainDTO.class);
        OrganizationDomainDTO organizationDomainDTO1 = new OrganizationDomainDTO();
        organizationDomainDTO1.id = 1L;
        OrganizationDomainDTO organizationDomainDTO2 = new OrganizationDomainDTO();
        assertThat(organizationDomainDTO1).isNotEqualTo(organizationDomainDTO2);
        organizationDomainDTO2.id = organizationDomainDTO1.id;
        assertThat(organizationDomainDTO1).isEqualTo(organizationDomainDTO2);
        organizationDomainDTO2.id = 2L;
        assertThat(organizationDomainDTO1).isNotEqualTo(organizationDomainDTO2);
        organizationDomainDTO1.id = null;
        assertThat(organizationDomainDTO1).isNotEqualTo(organizationDomainDTO2);
    }
}
