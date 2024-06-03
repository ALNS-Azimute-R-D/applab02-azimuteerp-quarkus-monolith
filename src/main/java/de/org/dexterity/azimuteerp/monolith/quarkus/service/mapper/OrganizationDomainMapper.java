package de.org.dexterity.azimuteerp.monolith.quarkus.service.mapper;

import de.org.dexterity.azimuteerp.monolith.quarkus.domain.*;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.OrganizationDomainDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrganizationDomain} and its DTO {@link OrganizationDomainDTO}.
 */
@Mapper(componentModel = "jakarta", uses = { OrganizationMapper.class })
public interface OrganizationDomainMapper extends EntityMapper<OrganizationDomainDTO, OrganizationDomain> {
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organization")
    OrganizationDomainDTO toDto(OrganizationDomain organizationDomain);

    @Mapping(source = "organizationId", target = "organization")
    OrganizationDomain toEntity(OrganizationDomainDTO organizationDomainDTO);

    default OrganizationDomain fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrganizationDomain organizationDomain = new OrganizationDomain();
        organizationDomain.id = id;
        return organizationDomain;
    }
}
