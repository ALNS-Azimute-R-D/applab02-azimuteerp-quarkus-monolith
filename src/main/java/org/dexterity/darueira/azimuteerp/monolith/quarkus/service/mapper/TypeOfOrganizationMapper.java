package org.dexterity.darueira.azimuteerp.monolith.quarkus.service.mapper;

import org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.*;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.TypeOfOrganizationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeOfOrganization} and its DTO {@link TypeOfOrganizationDTO}.
 */
@Mapper(componentModel = "jakarta", uses = {})
public interface TypeOfOrganizationMapper extends EntityMapper<TypeOfOrganizationDTO, TypeOfOrganization> {
    @Mapping(target = "organizationsLists", ignore = true)
    TypeOfOrganization toEntity(TypeOfOrganizationDTO typeOfOrganizationDTO);

    default TypeOfOrganization fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeOfOrganization typeOfOrganization = new TypeOfOrganization();
        typeOfOrganization.id = id;
        return typeOfOrganization;
    }
}
