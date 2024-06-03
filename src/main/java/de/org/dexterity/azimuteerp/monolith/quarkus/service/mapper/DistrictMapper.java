package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.DistrictDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link District} and its DTO {@link DistrictDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { TownCityMapper.class })
public interface DistrictMapper extends EntityMapper<DistrictDTO, District> {
    @Mapping(source = "townCity.id", target = "townCityId")
    @Mapping(source = "townCity.name", target = "townCity")
    DistrictDTO toDto(District district);

    @Mapping(source = "townCityId", target = "townCity")
    @Mapping(target = "personsLists", ignore = true)
    @Mapping(target = "customersLists", ignore = true)
    District toEntity(DistrictDTO districtDTO);

    default District fromId(Long id) {
        if (id == null) {
            return null;
        }
        District district = new District();
        district.id = id;
        return district;
    }
}
