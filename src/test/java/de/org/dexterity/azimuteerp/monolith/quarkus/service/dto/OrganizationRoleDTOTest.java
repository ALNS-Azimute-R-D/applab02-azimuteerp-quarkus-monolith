package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationRoleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationRoleDTO.class);
        OrganizationRoleDTO organizationRoleDTO1 = new OrganizationRoleDTO();
        organizationRoleDTO1.id = 1L;
        OrganizationRoleDTO organizationRoleDTO2 = new OrganizationRoleDTO();
        assertThat(organizationRoleDTO1).isNotEqualTo(organizationRoleDTO2);
        organizationRoleDTO2.id = organizationRoleDTO1.id;
        assertThat(organizationRoleDTO1).isEqualTo(organizationRoleDTO2);
        organizationRoleDTO2.id = 2L;
        assertThat(organizationRoleDTO1).isNotEqualTo(organizationRoleDTO2);
        organizationRoleDTO1.id = null;
        assertThat(organizationRoleDTO1).isNotEqualTo(organizationRoleDTO2);
    }
}
