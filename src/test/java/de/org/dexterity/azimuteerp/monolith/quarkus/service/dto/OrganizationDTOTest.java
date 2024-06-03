package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationDTO.class);
        OrganizationDTO organizationDTO1 = new OrganizationDTO();
        organizationDTO1.id = 1L;
        OrganizationDTO organizationDTO2 = new OrganizationDTO();
        assertThat(organizationDTO1).isNotEqualTo(organizationDTO2);
        organizationDTO2.id = organizationDTO1.id;
        assertThat(organizationDTO1).isEqualTo(organizationDTO2);
        organizationDTO2.id = 2L;
        assertThat(organizationDTO1).isNotEqualTo(organizationDTO2);
        organizationDTO1.id = null;
        assertThat(organizationDTO1).isNotEqualTo(organizationDTO2);
    }
}
