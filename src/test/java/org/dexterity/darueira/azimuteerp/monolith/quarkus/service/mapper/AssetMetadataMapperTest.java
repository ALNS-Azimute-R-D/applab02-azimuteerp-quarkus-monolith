package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetMetadataAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetMetadataTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class AssetMetadataMapperTest {

    @Inject
    AssetMetadataMapper assetMetadataMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAssetMetadataSample1();
        var actual = assetMetadataMapper.toEntity(assetMetadataMapper.toDto(expected));
        assertAssetMetadataAllPropertiesEquals(expected, actual);
    }
}
