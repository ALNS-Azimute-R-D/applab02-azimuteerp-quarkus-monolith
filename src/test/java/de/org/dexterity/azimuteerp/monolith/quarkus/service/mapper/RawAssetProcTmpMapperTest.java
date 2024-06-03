package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.RawAssetProcTmpAsserts.*;
import static de.org.dexterity.azimuteerp.monolith.quarkus.domain.RawAssetProcTmpTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class RawAssetProcTmpMapperTest {

    @Inject
    RawAssetProcTmpMapper rawAssetProcTmpMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRawAssetProcTmpSample1();
        var actual = rawAssetProcTmpMapper.toEntity(rawAssetProcTmpMapper.toDto(expected));
        assertRawAssetProcTmpAllPropertiesEquals(expected, actual);
    }
}
