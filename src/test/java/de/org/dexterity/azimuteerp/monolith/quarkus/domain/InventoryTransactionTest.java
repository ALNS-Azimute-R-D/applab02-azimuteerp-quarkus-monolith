package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.InventoryTransactionTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.ProductTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.SupplierTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.WarehouseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class InventoryTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InventoryTransaction.class);
        InventoryTransaction inventoryTransaction1 = getInventoryTransactionSample1();
        InventoryTransaction inventoryTransaction2 = new InventoryTransaction();
        assertThat(inventoryTransaction1).isNotEqualTo(inventoryTransaction2);

        inventoryTransaction2.id = inventoryTransaction1.id;
        assertThat(inventoryTransaction1).isEqualTo(inventoryTransaction2);

        inventoryTransaction2 = getInventoryTransactionSample2();
        assertThat(inventoryTransaction1).isNotEqualTo(inventoryTransaction2);
    }
}
