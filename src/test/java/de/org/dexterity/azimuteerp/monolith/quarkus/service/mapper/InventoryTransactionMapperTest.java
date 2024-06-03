package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.InventoryTransactionAsserts.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.InventoryTransactionTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class InventoryTransactionMapperTest {

    @Inject
    InventoryTransactionMapper inventoryTransactionMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInventoryTransactionSample1();
        var actual = inventoryTransactionMapper.toEntity(inventoryTransactionMapper.toDto(expected));
        assertInventoryTransactionAllPropertiesEquals(expected, actual);
    }
}
