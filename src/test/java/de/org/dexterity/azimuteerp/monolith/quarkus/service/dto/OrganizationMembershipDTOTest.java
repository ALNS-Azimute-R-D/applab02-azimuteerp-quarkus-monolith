package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationMembershipDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationMembershipDTO.class);
        OrganizationMembershipDTO organizationMembershipDTO1 = new OrganizationMembershipDTO();
        organizationMembershipDTO1.id = 1L;
        OrganizationMembershipDTO organizationMembershipDTO2 = new OrganizationMembershipDTO();
        assertThat(organizationMembershipDTO1).isNotEqualTo(organizationMembershipDTO2);
        organizationMembershipDTO2.id = organizationMembershipDTO1.id;
        assertThat(organizationMembershipDTO1).isEqualTo(organizationMembershipDTO2);
        organizationMembershipDTO2.id = 2L;
        assertThat(organizationMembershipDTO1).isNotEqualTo(organizationMembershipDTO2);
        organizationMembershipDTO1.id = null;
        assertThat(organizationMembershipDTO1).isNotEqualTo(organizationMembershipDTO2);
    }
}
