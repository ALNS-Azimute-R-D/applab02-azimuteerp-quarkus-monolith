package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class WarehouseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WarehouseDTO.class);
        WarehouseDTO warehouseDTO1 = new WarehouseDTO();
        warehouseDTO1.id = 1L;
        WarehouseDTO warehouseDTO2 = new WarehouseDTO();
        assertThat(warehouseDTO1).isNotEqualTo(warehouseDTO2);
        warehouseDTO2.id = warehouseDTO1.id;
        assertThat(warehouseDTO1).isEqualTo(warehouseDTO2);
        warehouseDTO2.id = 2L;
        assertThat(warehouseDTO1).isNotEqualTo(warehouseDTO2);
        warehouseDTO1.id = null;
        assertThat(warehouseDTO1).isNotEqualTo(warehouseDTO2);
    }
}
