package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class TypeOfPersonDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeOfPersonDTO.class);
        TypeOfPersonDTO typeOfPersonDTO1 = new TypeOfPersonDTO();
        typeOfPersonDTO1.id = 1L;
        TypeOfPersonDTO typeOfPersonDTO2 = new TypeOfPersonDTO();
        assertThat(typeOfPersonDTO1).isNotEqualTo(typeOfPersonDTO2);
        typeOfPersonDTO2.id = typeOfPersonDTO1.id;
        assertThat(typeOfPersonDTO1).isEqualTo(typeOfPersonDTO2);
        typeOfPersonDTO2.id = 2L;
        assertThat(typeOfPersonDTO1).isNotEqualTo(typeOfPersonDTO2);
        typeOfPersonDTO1.id = null;
        assertThat(typeOfPersonDTO1).isNotEqualTo(typeOfPersonDTO2);
    }
}
