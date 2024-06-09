package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.RawAssetProcTmpDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RawAssetProcTmp} and its DTO {@link RawAssetProcTmpDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { AssetTypeMapper.class })
public interface RawAssetProcTmpMapper extends EntityMapper<RawAssetProcTmpDTO, RawAssetProcTmp> {
    @Mapping(source = "assetType.id", target = "assetTypeId")
    @Mapping(source = "assetType.name", target = "assetType")
    RawAssetProcTmpDTO toDto(RawAssetProcTmp rawAssetProcTmp);

    @Mapping(source = "assetTypeId", target = "assetType")
    @Mapping(target = "assets", ignore = true)
    RawAssetProcTmp toEntity(RawAssetProcTmpDTO rawAssetProcTmpDTO);

    default RawAssetProcTmp fromId(Long id) {
        if (id == null) {
            return null;
        }
        RawAssetProcTmp rawAssetProcTmp = new RawAssetProcTmp();
        rawAssetProcTmp.id = id;
        return rawAssetProcTmp;
    }
}
