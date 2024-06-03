package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.LocalityAsserts.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.LocalityTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class LocalityMapperTest {

    @Inject
    LocalityMapper localityMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLocalitySample1();
        var actual = localityMapper.toEntity(localityMapper.toDto(expected));
        assertLocalityAllPropertiesEquals(expected, actual);
    }
}
