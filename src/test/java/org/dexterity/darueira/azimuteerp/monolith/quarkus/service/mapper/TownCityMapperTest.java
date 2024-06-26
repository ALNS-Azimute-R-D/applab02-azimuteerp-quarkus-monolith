package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.TownCityAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.TownCityTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TownCityMapperTest {

    @Inject
    TownCityMapper townCityMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTownCitySample1();
        var actual = townCityMapper.toEntity(townCityMapper.toDto(expected));
        assertTownCityAllPropertiesEquals(expected, actual);
    }
}
