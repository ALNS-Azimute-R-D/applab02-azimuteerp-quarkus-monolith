package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetTypeAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetTypeTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class AssetTypeMapperTest {

    @Inject
    AssetTypeMapper assetTypeMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAssetTypeSample1();
        var actual = assetTypeMapper.toEntity(assetTypeMapper.toDto(expected));
        assertAssetTypeAllPropertiesEquals(expected, actual);
    }
}
