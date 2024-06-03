package de.org.dexterity.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class TypeOfOrganizationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeOfOrganizationDTO.class);
        TypeOfOrganizationDTO typeOfOrganizationDTO1 = new TypeOfOrganizationDTO();
        typeOfOrganizationDTO1.id = 1L;
        TypeOfOrganizationDTO typeOfOrganizationDTO2 = new TypeOfOrganizationDTO();
        assertThat(typeOfOrganizationDTO1).isNotEqualTo(typeOfOrganizationDTO2);
        typeOfOrganizationDTO2.id = typeOfOrganizationDTO1.id;
        assertThat(typeOfOrganizationDTO1).isEqualTo(typeOfOrganizationDTO2);
        typeOfOrganizationDTO2.id = 2L;
        assertThat(typeOfOrganizationDTO1).isNotEqualTo(typeOfOrganizationDTO2);
        typeOfOrganizationDTO1.id = null;
        assertThat(typeOfOrganizationDTO1).isNotEqualTo(typeOfOrganizationDTO2);
    }
}
