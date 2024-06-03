package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationMemberRoleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationMemberRoleDTO.class);
        OrganizationMemberRoleDTO organizationMemberRoleDTO1 = new OrganizationMemberRoleDTO();
        organizationMemberRoleDTO1.id = 1L;
        OrganizationMemberRoleDTO organizationMemberRoleDTO2 = new OrganizationMemberRoleDTO();
        assertThat(organizationMemberRoleDTO1).isNotEqualTo(organizationMemberRoleDTO2);
        organizationMemberRoleDTO2.id = organizationMemberRoleDTO1.id;
        assertThat(organizationMemberRoleDTO1).isEqualTo(organizationMemberRoleDTO2);
        organizationMemberRoleDTO2.id = 2L;
        assertThat(organizationMemberRoleDTO1).isNotEqualTo(organizationMemberRoleDTO2);
        organizationMemberRoleDTO1.id = null;
        assertThat(organizationMemberRoleDTO1).isNotEqualTo(organizationMemberRoleDTO2);
    }
}
