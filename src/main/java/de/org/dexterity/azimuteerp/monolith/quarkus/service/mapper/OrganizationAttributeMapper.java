package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrganizationAttributeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrganizationAttribute} and its DTO {@link OrganizationAttributeDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { OrganizationMapper.class })
public interface OrganizationAttributeMapper extends EntityMapper<OrganizationAttributeDTO, OrganizationAttribute> {
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organization")
    OrganizationAttributeDTO toDto(OrganizationAttribute organizationAttribute);

    @Mapping(source = "organizationId", target = "organization")
    OrganizationAttribute toEntity(OrganizationAttributeDTO organizationAttributeDTO);

    default OrganizationAttribute fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrganizationAttribute organizationAttribute = new OrganizationAttribute();
        organizationAttribute.id = id;
        return organizationAttribute;
    }
}
