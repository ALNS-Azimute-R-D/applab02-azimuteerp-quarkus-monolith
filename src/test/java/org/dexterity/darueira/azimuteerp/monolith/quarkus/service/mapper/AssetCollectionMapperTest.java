package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetCollectionAsserts.*;
import static org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.AssetCollectionTestSamples.*;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class AssetCollectionMapperTest {

    @Inject
    AssetCollectionMapper assetCollectionMapper;

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAssetCollectionSample1();
        var actual = assetCollectionMapper.toEntity(assetCollectionMapper.toDto(expected));
        assertAssetCollectionAllPropertiesEquals(expected, actual);
    }
}
