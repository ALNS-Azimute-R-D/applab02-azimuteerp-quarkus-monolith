package org.dexterity.darueira.azimuteerp.monolith.quarkus.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.ProductTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.StockLevelTestSamples.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.WarehouseTestSamples.*;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class StockLevelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockLevel.class);
        StockLevel stockLevel1 = getStockLevelSample1();
        StockLevel stockLevel2 = new StockLevel();
        assertThat(stockLevel1).isNotEqualTo(stockLevel2);

        stockLevel2.id = stockLevel1.id;
        assertThat(stockLevel1).isEqualTo(stockLevel2);

        stockLevel2 = getStockLevelSample2();
        assertThat(stockLevel1).isNotEqualTo(stockLevel2);
    }
}
