package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class SupplierDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierDTO.class);
        SupplierDTO supplierDTO1 = new SupplierDTO();
        supplierDTO1.id = 1L;
        SupplierDTO supplierDTO2 = new SupplierDTO();
        assertThat(supplierDTO1).isNotEqualTo(supplierDTO2);
        supplierDTO2.id = supplierDTO1.id;
        assertThat(supplierDTO1).isEqualTo(supplierDTO2);
        supplierDTO2.id = 2L;
        assertThat(supplierDTO1).isNotEqualTo(supplierDTO2);
        supplierDTO1.id = null;
        assertThat(supplierDTO1).isNotEqualTo(supplierDTO2);
    }
}
