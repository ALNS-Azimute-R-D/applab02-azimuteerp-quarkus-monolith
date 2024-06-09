package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CommonLocalityAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CommonLocalityTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class CommonLocalityMapperTest {

    @Inject
    CommonLocalityMapper commonLocalityMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCommonLocalitySample1();
        var actual = commonLocalityMapper.toEntity(commonLocalityMapper.toDto(expected));
        assertCommonLocalityAllPropertiesEquals(expected, actual);
    }
}
