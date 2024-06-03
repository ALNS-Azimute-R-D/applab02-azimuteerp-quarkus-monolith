package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.TownCityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TownCity} and its DTO {@link TownCityDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { ProvinceMapper.class })
public interface TownCityMapper extends EntityMapper<TownCityDTO, TownCity> {
    @Mapping(source = "province.id", target = "provinceId")
    @Mapping(source = "province.name", target = "province")
    TownCityDTO toDto(TownCity townCity);

    @Mapping(source = "provinceId", target = "province")
    @Mapping(target = "districtsLists", ignore = true)
    TownCity toEntity(TownCityDTO townCityDTO);

    default TownCity fromId(Long id) {
        if (id == null) {
            return null;
        }
        TownCity townCity = new TownCity();
        townCity.id = id;
        return townCity;
    }
}
