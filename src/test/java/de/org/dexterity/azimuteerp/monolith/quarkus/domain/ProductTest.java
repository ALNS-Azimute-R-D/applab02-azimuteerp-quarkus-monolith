package de.org.dexterity.azimuteerp.monolith.quarkus.domain;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.BrandTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.InventoryTransactionTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.ProductTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.StockLevelTestSamples.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.SupplierTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.org.dexterity.azimuteerp.monolith.quarkus.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.id = product1.id;
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }
}
