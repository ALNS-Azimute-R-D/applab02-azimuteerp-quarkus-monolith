package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class AssetMapperTest {

    @Inject
    AssetMapper assetMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAssetSample1();
        var actual = assetMapper.toEntity(assetMapper.toDto(expected));
        assertAssetAllPropertiesEquals(expected, actual);
    }
}
