package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.CommonLocalityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommonLocality} and its DTO {@link CommonLocalityDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { DistrictMapper.class })
public interface CommonLocalityMapper extends EntityMapper<CommonLocalityDTO, CommonLocality> {
    @Mapping(source = "district.id", target = "districtId")
    @Mapping(source = "district.name", target = "district")
    CommonLocalityDTO toDto(CommonLocality commonLocality);

    @Mapping(source = "districtId", target = "district")
    CommonLocality toEntity(CommonLocalityDTO commonLocalityDTO);

    default CommonLocality fromId(Long id) {
        if (id == null) {
            return null;
        }
        CommonLocality commonLocality = new CommonLocality();
        commonLocality.id = id;
        return commonLocality;
    }
}
