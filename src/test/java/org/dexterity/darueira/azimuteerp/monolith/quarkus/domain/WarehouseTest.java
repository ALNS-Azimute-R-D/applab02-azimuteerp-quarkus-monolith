package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.InventoryTransactionTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.StockLevelTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.WarehouseTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class WarehouseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Warehouse.class);
        Warehouse warehouse1 = getWarehouseSample1();
        Warehouse warehouse2 = new Warehouse();
        assertThat(warehouse1).isNotEqualTo(warehouse2);

        warehouse2.id = warehouse1.id;
        assertThat(warehouse1).isEqualTo(warehouse2);

        warehouse2 = getWarehouseSample2();
        assertThat(warehouse1).isNotEqualTo(warehouse2);
    }
}
