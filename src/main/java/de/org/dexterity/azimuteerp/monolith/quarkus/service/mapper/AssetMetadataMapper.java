package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.AssetMetadataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetMetadata} and its DTO {@link AssetMetadataDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { AssetMapper.class })
public interface AssetMetadataMapper extends EntityMapper<AssetMetadataDTO, AssetMetadata> {
    @Mapping(source = "asset.id", target = "assetId")
    @Mapping(source = "asset.name", target = "asset")
    AssetMetadataDTO toDto(AssetMetadata assetMetadata);

    @Mapping(source = "assetId", target = "asset")
    AssetMetadata toEntity(AssetMetadataDTO assetMetadataDTO);

    default AssetMetadata fromId(Long id) {
        if (id == null) {
            return null;
        }
        AssetMetadata assetMetadata = new AssetMetadata();
        assetMetadata.id = id;
        return assetMetadata;
    }
}
