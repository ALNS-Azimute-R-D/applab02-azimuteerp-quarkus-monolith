package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class InventoryTransactionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InventoryTransactionDTO.class);
        InventoryTransactionDTO inventoryTransactionDTO1 = new InventoryTransactionDTO();
        inventoryTransactionDTO1.id = 1L;
        InventoryTransactionDTO inventoryTransactionDTO2 = new InventoryTransactionDTO();
        assertThat(inventoryTransactionDTO1).isNotEqualTo(inventoryTransactionDTO2);
        inventoryTransactionDTO2.id = inventoryTransactionDTO1.id;
        assertThat(inventoryTransactionDTO1).isEqualTo(inventoryTransactionDTO2);
        inventoryTransactionDTO2.id = 2L;
        assertThat(inventoryTransactionDTO1).isNotEqualTo(inventoryTransactionDTO2);
        inventoryTransactionDTO1.id = null;
        assertThat(inventoryTransactionDTO1).isNotEqualTo(inventoryTransactionDTO2);
    }
}
