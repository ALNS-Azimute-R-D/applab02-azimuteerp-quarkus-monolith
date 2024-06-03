package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.StockLevelAsserts.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.StockLevelTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class StockLevelMapperTest {

    @Inject
    StockLevelMapper stockLevelMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStockLevelSample1();
        var actual = stockLevelMapper.toEntity(stockLevelMapper.toDto(expected));
        assertStockLevelAllPropertiesEquals(expected, actual);
    }
}
