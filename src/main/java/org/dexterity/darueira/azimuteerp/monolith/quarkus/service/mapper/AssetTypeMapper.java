package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.AssetTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetType} and its DTO {@link AssetTypeDTO}.
 */
@Mapper(componentModel = "jakarta", uses = {})
public interface AssetTypeMapper extends EntityMapper<AssetTypeDTO, AssetType> {
    @Mapping(target = "rawAssetsProcsTmps", ignore = true)
    @Mapping(target = "assets", ignore = true)
    AssetType toEntity(AssetTypeDTO assetTypeDTO);

    default AssetType fromId(Long id) {
        if (id == null) {
            return null;
        }
        AssetType assetType = new AssetType();
        assetType.id = id;
        return assetType;
    }
}
