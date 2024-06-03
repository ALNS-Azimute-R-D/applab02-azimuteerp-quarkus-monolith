package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationAttributeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationAttributeDTO.class);
        OrganizationAttributeDTO organizationAttributeDTO1 = new OrganizationAttributeDTO();
        organizationAttributeDTO1.id = 1L;
        OrganizationAttributeDTO organizationAttributeDTO2 = new OrganizationAttributeDTO();
        assertThat(organizationAttributeDTO1).isNotEqualTo(organizationAttributeDTO2);
        organizationAttributeDTO2.id = organizationAttributeDTO1.id;
        assertThat(organizationAttributeDTO1).isEqualTo(organizationAttributeDTO2);
        organizationAttributeDTO2.id = 2L;
        assertThat(organizationAttributeDTO1).isNotEqualTo(organizationAttributeDTO2);
        organizationAttributeDTO1.id = null;
        assertThat(organizationAttributeDTO1).isNotEqualTo(organizationAttributeDTO2);
    }
}
