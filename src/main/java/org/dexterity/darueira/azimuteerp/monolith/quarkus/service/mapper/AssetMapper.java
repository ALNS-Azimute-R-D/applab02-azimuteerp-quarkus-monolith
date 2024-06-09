package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.AssetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Asset} and its DTO {@link AssetDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { AssetTypeMapper.class, RawAssetProcTmpMapper.class })
public interface AssetMapper extends EntityMapper<AssetDTO, Asset> {
    @Mapping(source = "assetType.id", target = "assetTypeId")
    @Mapping(source = "assetType.name", target = "assetType")
    @Mapping(source = "rawAssetProcTmp.id", target = "rawAssetProcTmpId")
    @Mapping(source = "rawAssetProcTmp.name", target = "rawAssetProcTmp")
    AssetDTO toDto(Asset asset);

    @Mapping(source = "assetTypeId", target = "assetType")
    @Mapping(source = "rawAssetProcTmpId", target = "rawAssetProcTmp")
    @Mapping(target = "assetMetadata", ignore = true)
    @Mapping(target = "assetCollections", ignore = true)
    Asset toEntity(AssetDTO assetDTO);

    default Asset fromId(Long id) {
        if (id == null) {
            return null;
        }
        Asset asset = new Asset();
        asset.id = id;
        return asset;
    }
}
