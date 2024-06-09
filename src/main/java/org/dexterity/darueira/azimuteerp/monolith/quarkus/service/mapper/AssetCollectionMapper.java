package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.AssetCollectionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetCollection} and its DTO {@link AssetCollectionDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { AssetMapper.class })
public interface AssetCollectionMapper extends EntityMapper<AssetCollectionDTO, AssetCollection> {
    @Mapping(target = "articles", ignore = true)
    AssetCollection toEntity(AssetCollectionDTO assetCollectionDTO);

    default AssetCollection fromId(Long id) {
        if (id == null) {
            return null;
        }
        AssetCollection assetCollection = new AssetCollection();
        assetCollection.id = id;
        return assetCollection;
    }
}
