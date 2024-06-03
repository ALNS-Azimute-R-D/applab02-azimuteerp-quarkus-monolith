package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.LocalityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Locality} and its DTO {@link LocalityDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { CountryMapper.class })
public interface LocalityMapper extends EntityMapper<LocalityDTO, Locality> {
    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "country.name", target = "country")
    LocalityDTO toDto(Locality locality);

    @Mapping(source = "countryId", target = "country")
    Locality toEntity(LocalityDTO localityDTO);

    default Locality fromId(Long id) {
        if (id == null) {
            return null;
        }
        Locality locality = new Locality();
        locality.id = id;
        return locality;
    }
}
