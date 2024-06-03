package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.DistrictAsserts.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.DistrictTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class DistrictMapperTest {

    @Inject
    DistrictMapper districtMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDistrictSample1();
        var actual = districtMapper.toEntity(districtMapper.toDto(expected));
        assertDistrictAllPropertiesEquals(expected, actual);
    }
}
