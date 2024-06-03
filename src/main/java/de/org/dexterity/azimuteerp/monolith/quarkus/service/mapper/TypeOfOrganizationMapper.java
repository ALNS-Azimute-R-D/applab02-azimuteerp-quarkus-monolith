package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.TypeOfOrganizationDTO;
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
